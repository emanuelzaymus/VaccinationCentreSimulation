package controller

import javafx.application.Platform
import javafx.beans.property.*
import javafx.collections.ObservableList
import javafx.scene.chart.XYChart
import javafx.scene.control.Alert
import tornadofx.Controller
import tornadofx.alert
import tornadofx.observableList
import tornadofx.onChange
import vaccinationcentresimulation.IAnimationActionListener
import vaccinationcentresimulation.experiment.VaccinationCentreExperiment
import vaccinationcentresimulation.statistics.WorkloadStats
import kotlin.concurrent.thread

class MainController : Controller(), IAnimationActionListener {

    private val initReplicationsCount = 1
    private val initNumberOfPatients = 540
    private val initNumberOfAdminWorkers = 6
    private val initNumberOfDoctors = 5
    private val initNumberOfNurses = 3
    private val initWithAnimation = true

    // Working time starts at 8:00:00
    private val startTime = 8 * 60 * 60.0

    private var experiment = VaccinationCentreExperiment(
        initReplicationsCount,
        initNumberOfPatients,
        initNumberOfAdminWorkers,
        initNumberOfDoctors,
        initNumberOfNurses,
        initWithAnimation
    )

    val replicationsCount = SimpleStringProperty(initReplicationsCount.toString())
    val skip = SimpleStringProperty("0.3")
    val numberOfPatients = SimpleStringProperty(initNumberOfPatients.toString())
    val numberOfAdminWorkers = SimpleStringProperty(initNumberOfAdminWorkers.toString())
    val numberOfDoctors = SimpleStringProperty(initNumberOfDoctors.toString())
    val numberOfNurses = SimpleStringProperty(initNumberOfNurses.toString())

    val withAnimation = SimpleBooleanProperty(initWithAnimation)
        .apply { onChange { b -> experiment.simulation.withAnimation = b } }
    val delayEvery = SimpleIntegerProperty(60)
        .apply { onChange { seconds -> experiment.simulation.setDelayEverySimUnits(seconds.toDouble()) } }
    val delayFor = SimpleLongProperty(100)
        .apply { onChange { millis -> experiment.simulation.setDelayForMillis(millis) } }

    val state = SimpleStringProperty("READY")
    val actualSimTime = SimpleStringProperty(startTime.secondsToTime())
    val actualSimSeconds = SimpleDoubleProperty(.0)
    val currentReplicNumber = SimpleIntegerProperty(1)

    private val initVal = 0.0.roundToString()
    val regQueueActualLength = SimpleIntegerProperty()
    val regQueueAvgLength = SimpleStringProperty(initVal)
    val regQueueAvgWaitingTimeInHours = SimpleStringProperty(initVal)
    val regQueueAvgWaitingTime = SimpleStringProperty(initVal)
    val regRoomBusyWorkers = SimpleIntegerProperty()
    val regRoomWorkload = SimpleStringProperty(initVal)
    val regRoomPersonalWorkloads = observableList<Worker>()

    val examQueueActualLength = SimpleIntegerProperty()
    val examQueueAvgLength = SimpleStringProperty(initVal)
    val examQueueAvgWaitingTimeInHours = SimpleStringProperty(initVal)
    val examQueueAvgWaitingTime = SimpleStringProperty(initVal)
    val examRoomBusyWorkers = SimpleIntegerProperty()
    val examRoomWorkload = SimpleStringProperty(initVal)
    val examRoomPersonalWorkloads = observableList<Worker>()

    val vacQueueActualLength = SimpleIntegerProperty()
    val vacQueueAvgLength = SimpleStringProperty(initVal)
    val vacQueueAvgWaitingTimeInHours = SimpleStringProperty(initVal)
    val vacQueueAvgWaitingTime = SimpleStringProperty(initVal)
    val vacRoomBusyWorkers = SimpleIntegerProperty()
    val vacRoomWorkload = SimpleStringProperty(initVal)
    val vacRoomPersonalWorkloads = observableList<Worker>()

    val waitRoomPatientsCount = SimpleIntegerProperty()
    val waitRoomAvgLength = SimpleStringProperty(initVal)

    //////////

    val allRegQueueAvgLength = SimpleStringProperty(initVal)
    val allRegQueueAvgWaitingTimeInHours = SimpleStringProperty(initVal)
    val allRegQueueAvgWaitingTime = SimpleStringProperty(initVal)
    val allRegRoomWorkload = SimpleStringProperty(initVal)

    val allExamQueueAvgLength = SimpleStringProperty(initVal)
    val allExamQueueAvgWaitingTimeInHours = SimpleStringProperty(initVal)
    val allExamQueueAvgWaitingTime = SimpleStringProperty(initVal)
    val allExamRoomWorkload = SimpleStringProperty(initVal)

    val allVacQueueAvgLength = SimpleStringProperty(initVal)
    val allVacQueueAvgWaitingTimeInHours = SimpleStringProperty(initVal)
    val allVacQueueAvgWaitingTime = SimpleStringProperty(initVal)
    val allVacRoomWorkload = SimpleStringProperty(initVal)

    private val dash = "-"
    val allWaitRoomAvgLength = SimpleStringProperty(initVal)
    val lowerBoundConfInterval = SimpleStringProperty(dash)
    val upperBoundConfInterval = SimpleStringProperty(dash)

    //////////

    val examinationQueueChartData = observableList<XYChart.Data<Number, Number>>() @Synchronized get

    fun startPause() {
        if (!experiment.simulation.wasStarted())
            start()
        else if (!experiment.simulation.isPaused())
            experiment.simulation.pause()
        else
            experiment.simulation.restore()
    }

    private fun start() {
        if (restart()) {
            thread(isDaemon = true, name = "SIM_THREAD") { experiment.simulation.simulate() }
        }
    }

    fun stop() = experiment.simulation.stop()

    override fun updateActualSimulationTime(actualSimulationTime: Double) = Platform.runLater {
        actualSimTime.value = (actualSimulationTime + startTime).secondsToTime()
        actualSimSeconds.value = actualSimulationTime
    }

    override fun updateSimulationState(simulationState: String) = Platform.runLater { state.value = simulationState }

    override fun updateRegistrationQueueLength(length: Int) = Platform.runLater { regQueueActualLength.value = length }

    override fun updateExaminationQueueLength(length: Int) = Platform.runLater { examQueueActualLength.value = length }

    override fun updateVaccinationQueueLength(length: Int) = Platform.runLater { vacQueueActualLength.value = length }

    override fun updateRegistrationRoomBusyWorkersCount(busyWorkers: Int) =
        Platform.runLater { regRoomBusyWorkers.value = busyWorkers }

    override fun updateExaminationRoomBusyDoctorsCount(busyDoctors: Int) =
        Platform.runLater { examRoomBusyWorkers.value = busyDoctors }

    override fun updateVaccinationRoomBusyNursesCount(busyNurses: Int) =
        Platform.runLater { vacRoomBusyWorkers.value = busyNurses }

    override fun updateWaitingRoomPatientsCount(patients: Int) =
        Platform.runLater { waitRoomPatientsCount.value = patients }

    override fun updateStatistics() = Platform.runLater {
        with(experiment) {
            regQueueAvgLength.value = registrationQueueLength.getAverage().roundToString()
            regQueueAvgWaitingTimeInHours.value = registrationWaitingTime.getAverage().secondsToTime()
            regQueueAvgWaitingTime.value = registrationWaitingTime.getAverage().roundToString()
            regRoomWorkload.value = averageAdminWorkersWorkload.roundToString()
            updateWorkers(
                regRoomPersonalWorkloads, adminWorkersPersonalWorkloads, simulation.registrationRoom.getWorkersState()
            )

            examQueueAvgLength.value = examinationQueueLength.getAverage().roundToString()
            examQueueAvgWaitingTimeInHours.value = examinationWaitingTime.getAverage().secondsToTime()
            examQueueAvgWaitingTime.value = examinationWaitingTime.getAverage().roundToString()
            examRoomWorkload.value = averageDoctorsWorkload.roundToString()
            updateWorkers(
                examRoomPersonalWorkloads, doctorsPersonalWorkloads, simulation.examinationRoom.getWorkersState()
            )

            vacQueueAvgLength.value = vaccinationQueueLength.getAverage().roundToString()
            vacQueueAvgWaitingTimeInHours.value = vaccinationWaitingTime.getAverage().secondsToTime()
            vacQueueAvgWaitingTime.value = vaccinationWaitingTime.getAverage().roundToString()
            vacRoomWorkload.value = averageNursesWorkload.roundToString()
            updateWorkers(
                vacRoomPersonalWorkloads, nursesPersonalWorkloads, simulation.vaccinationRoom.getWorkersState()
            )

            waitRoomAvgLength.value = waitingPatientsCount.getAverage().roundToString()

            //////////

            allRegQueueAvgLength.value = allRegistrationQueueLengths.getAverage().roundToString()
            allRegQueueAvgWaitingTimeInHours.value = allRegistrationWaitingTimes.getAverage().secondsToTime()
            allRegQueueAvgWaitingTime.value = allRegistrationWaitingTimes.getAverage().roundToString()
            allRegRoomWorkload.value = allAdminWorkersWorkloads.getAverage().roundToString()

            allExamQueueAvgLength.value = allExaminationQueueLengths.getAverage().roundToString()
            allExamQueueAvgWaitingTimeInHours.value = allExaminationWaitingTimes.getAverage().secondsToTime()
            allExamQueueAvgWaitingTime.value = allExaminationWaitingTimes.getAverage().roundToString()
            allExamRoomWorkload.value = allDoctorsWorkloads.getAverage().roundToString()

            allVacQueueAvgLength.value = allVaccinationQueueLengths.getAverage().roundToString()
            allVacQueueAvgWaitingTimeInHours.value = allVaccinationWaitingTimes.getAverage().secondsToTime()
            allVacQueueAvgWaitingTime.value = allVaccinationWaitingTimes.getAverage().roundToString()
            allVacRoomWorkload.value = allNursesWorkloads.getAverage().roundToString()

            allWaitRoomAvgLength.value = allWaitingPatientsCounts.getAverage().roundToString()

            val lowerBound: Double = allWaitingPatientsCounts.lowerBoundOfConfidenceInterval()
            val upperBound: Double = allWaitingPatientsCounts.upperBoundOfConfidenceInterval()
            if (!lowerBound.isNaN() && !upperBound.isNaN()) {
                lowerBoundConfInterval.value = lowerBound.roundToString()
                upperBoundConfInterval.value = upperBound.roundToString()
            } else {
                lowerBoundConfInterval.value = dash
                upperBoundConfInterval.value = dash
            }

            ///////////

            if (examinationQueueLength.wasRestarted) {
                examinationQueueChartData.clear()
            }
            if (examinationQueueLength.chartData.isNotEmpty()) {
                examinationQueueChartData.addAll(examinationQueueLength.chartData.map {
                    XYChart.Data<Number, Number>(it.first, it.second)
                })
                examinationQueueLength.chartData.clear()
            }
        }
    }

    override fun updateCurrentReplicNumber(currentReplicNumber: Int) = Platform.runLater {
        this.currentReplicNumber.value = currentReplicNumber + 1
    }

    private fun updateWorkers(
        obsList: ObservableList<Worker>, workloadStats: List<WorkloadStats>, workersState: List<Boolean>
    ) {
        obsList.clear()
        val list = workloadStats.zip(workersState).mapIndexed { i, x -> Worker(i + 1, x.second, x.first.getAverage()) }
        obsList.addAll(list)
    }

    private fun restart(): Boolean {
        try {
            val replicCount = replicationsCount.value.toInt()
            val patients = numberOfPatients.value.toInt()
            val workers = numberOfAdminWorkers.value.toInt()
            val doctors = numberOfDoctors.value.toInt()
            val nurses = numberOfNurses.value.toInt()
            val withAnimation: Boolean = withAnimation.value

            experiment = VaccinationCentreExperiment(replicCount, patients, workers, doctors, nurses, withAnimation)
            experiment.simulation.setDelayEverySimUnits(delayEvery.value.toDouble())
            experiment.simulation.setDelayForMillis(delayFor.value)
            experiment.simulation.setAnimationActionListener(this)

            return true
        } catch (e: NumberFormatException) {
            alert(
                Alert.AlertType.ERROR,
                "Invalid Inputs",
                "Make sure you put valid inputs, please.",
                title = "Attention"
            )
        }
        return false
    }

}