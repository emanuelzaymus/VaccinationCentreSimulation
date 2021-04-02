package controller

import javafx.application.Platform
import javafx.beans.property.*
import javafx.scene.control.Alert
import tornadofx.Controller
import tornadofx.alert
import tornadofx.onChange
import vaccinationcentresimulation.IAnimationActionListener
import vaccinationcentresimulation.VaccinationCentreExperiment
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

    val withAnimation = SimpleBooleanProperty(initWithAnimation).apply {
        onChange { b -> experiment.simulation.withAnimation = b }
    }
    val delayEvery = SimpleIntegerProperty(60).apply {
        onChange { seconds -> experiment.simulation.setDelayEverySimUnits(seconds.toDouble()) }
    }
    val delayFor = SimpleLongProperty(100).apply {
        onChange { millis -> experiment.simulation.setDelayForMillis(millis) }
    }

    val state = SimpleStringProperty("READY")
    val actualSimTime = SimpleStringProperty(startTime.secondsToTime())

    private val initVal = 0.0.roundToString()
    val regQueueActualLength = SimpleIntegerProperty()
    val regQueueAvgLength = SimpleStringProperty(initVal)
    val regQueueAvgWaitingTimeInHours = SimpleStringProperty(initVal)
    val regQueueAvgWaitingTime = SimpleStringProperty(initVal)
    val regRoomBusyWorkers = SimpleIntegerProperty()
    val regRoomWorkload = SimpleStringProperty(initVal)
    val regRoomPersonalWorkload = SimpleListProperty<Unit>()

    val examQueueActualLength = SimpleIntegerProperty()
    val examQueueAvgLength = SimpleStringProperty(initVal)
    val examQueueAvgWaitingTimeInHours = SimpleStringProperty(initVal)
    val examQueueAvgWaitingTime = SimpleStringProperty(initVal)
    val examRoomBusyWorkers = SimpleIntegerProperty()
    val examRoomWorkload = SimpleStringProperty(initVal)

    val vacQueueActualLength = SimpleIntegerProperty()
    val vacQueueAvgLength = SimpleStringProperty(initVal)
    val vacQueueAvgWaitingTimeInHours = SimpleStringProperty(initVal)
    val vacQueueAvgWaitingTime = SimpleStringProperty(initVal)
    val vacRoomBusyWorkers = SimpleIntegerProperty()
    val vacRoomWorkload = SimpleStringProperty(initVal)

    val waitRoomPatientsCount = SimpleIntegerProperty()
    val waitRoomAvgLength = SimpleStringProperty(initVal)

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
        actualSimTime.value = "${(actualSimulationTime + startTime).secondsToTime()} | (Seconds: $actualSimulationTime)"
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
            regRoomWorkload.value = adminWorkersWorkload.getAverage().roundToString()
//        adminWorkersPersonalWorkload

            examQueueAvgLength.value = examinationQueueLength.getAverage().roundToString()
            examQueueAvgWaitingTimeInHours.value = examinationWaitingTime.getAverage().secondsToTime()
            examQueueAvgWaitingTime.value = examinationWaitingTime.getAverage().roundToString()
            examRoomWorkload.value = doctorsWorkload.getAverage().roundToString()

            vacQueueAvgLength.value = vaccinationQueueLength.getAverage().roundToString()
            vacQueueAvgWaitingTimeInHours.value = vaccinationWaitingTime.getAverage().secondsToTime()
            vacQueueAvgWaitingTime.value = vaccinationWaitingTime.getAverage().roundToString()
            vacRoomWorkload.value = nursesWorkload.getAverage().roundToString()

            waitRoomAvgLength.value = waitingPatientsCount.getAverage().roundToString()
        }
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