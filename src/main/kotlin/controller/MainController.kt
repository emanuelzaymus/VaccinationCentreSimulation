package controller

import javafx.application.Platform
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleLongProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.control.Alert
import tornadofx.Controller
import tornadofx.alert
import tornadofx.onChange
import vaccinationcentresimulation.IAnimationActionListener
import vaccinationcentresimulation.VaccinationCentreSimulation
import vaccinationcentresimulation.statistics.QueueLengthStats
import vaccinationcentresimulation.statistics.WaitingPatientsCountStats
import vaccinationcentresimulation.statistics.WaitingTimeStats
import vaccinationcentresimulation.statistics.WorkloadStats
import kotlin.concurrent.thread

class MainController : Controller(), IAnimationActionListener {

    // Working time starts at 8:00
    private val startTime = 8 * 60.0

    private val avgBeforeRegistrationQueueLen = QueueLengthStats()
    private val avgBeforeExaminationQueueLen = QueueLengthStats()
    private val avgBeforeVaccinationQueueLen = QueueLengthStats()

    private val avgWaitingTimeRegistrationQueue = WaitingTimeStats()
    private val avgWaitingTimeExaminationQueue = WaitingTimeStats()
    private val avgWaitingTimeVaccinationQueue = WaitingTimeStats()

    private val averageWorkloadAdministrativeWorkerStats = WorkloadStats()
    private val averageWorkloadDoctorStats = WorkloadStats()
    private val averageWorkloadNurseStats = WorkloadStats()

    private val averageWaitingPatientsCountStats = WaitingPatientsCountStats()

    private var simulation = VaccinationCentreSimulation(1, 540, 6, 5, 3, true)

    val replicationsCount = SimpleStringProperty("1")
    val skip = SimpleStringProperty("0.3")
    val numberOfPatients = SimpleStringProperty("540")
    val numberOfAdminWorkers = SimpleStringProperty("6")
    val numberOfDoctors = SimpleStringProperty("5")
    val numberOfNurses = SimpleStringProperty("3")

    val withAnimation = SimpleBooleanProperty(true).apply {
        onChange { b -> simulation.withAnimation = b }
    }
    val delayEvery = SimpleIntegerProperty(60).apply {
        onChange { seconds -> simulation.setDelayEverySimSeconds(seconds) }
    }
    val delayFor = SimpleLongProperty(100).apply {
        onChange { millis -> simulation.setDelayForMillis(millis) }
    }

    val state = SimpleStringProperty("READY")
    val actualSimTime = SimpleStringProperty(startTime.minutesToTime())

    private val initVal = 0.0.roundToString()
    val regQueueActualLength = SimpleIntegerProperty()
    val regQueueAvgLength = SimpleStringProperty(initVal)
    val regQueueAvgWaitingTime = SimpleStringProperty(initVal)
    val regRoomBusyWorkers = SimpleIntegerProperty()
    val regRoomWorkload = SimpleStringProperty(initVal)

    val examQueueActualLength = SimpleIntegerProperty()
    val examQueueAvgLength = SimpleStringProperty(initVal)
    val examQueueAvgWaitingTime = SimpleStringProperty(initVal)
    val examRoomBusyWorkers = SimpleIntegerProperty()
    val examRoomWorkload = SimpleStringProperty(initVal)

    val vacQueueActualLength = SimpleIntegerProperty()
    val vacQueueAvgLength = SimpleStringProperty(initVal)
    val vacQueueAvgWaitingTime = SimpleStringProperty(initVal)
    val vacRoomBusyWorkers = SimpleIntegerProperty()
    val vacRoomWorkload = SimpleStringProperty(initVal)

    val waitRoomPatientsCount = SimpleIntegerProperty()
    val waitRoomAvgLength = SimpleStringProperty(initVal)

    fun startPause() {
        if (!simulation.isRunning())
            start()
        else if (!simulation.isPaused())
            simulation.pause()
        else
            simulation.restore()
    }

    private fun start() {
        if (restart()) {
            thread(isDaemon = true, name = "SIM_THREAD") { simulation.simulate() }
        }
    }

    fun stop() = simulation.stop()

    override fun updateActualSimulationTime(actualSimulationTime: Double) = Platform.runLater {
        actualSimTime.value = "${(actualSimulationTime + startTime).minutesToTime()} | (Minutes: $actualSimulationTime)"
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
        regQueueAvgLength.value = avgBeforeRegistrationQueueLen.getAverage().roundToString()
        regQueueAvgWaitingTime.value = avgWaitingTimeRegistrationQueue.getAverage().roundToString()
        regRoomWorkload.value = averageWorkloadAdministrativeWorkerStats.getAverage().roundToString()

        examQueueAvgLength.value = avgBeforeExaminationQueueLen.getAverage().roundToString()
        examQueueAvgWaitingTime.value = avgWaitingTimeExaminationQueue.getAverage().roundToString()
        examRoomWorkload.value = averageWorkloadDoctorStats.getAverage().roundToString()

        vacQueueAvgLength.value = avgBeforeVaccinationQueueLen.getAverage().roundToString()
        vacQueueAvgWaitingTime.value = avgWaitingTimeVaccinationQueue.getAverage().roundToString()
        vacRoomWorkload.value = averageWorkloadNurseStats.getAverage().roundToString()

        waitRoomAvgLength.value = averageWaitingPatientsCountStats.getAverage().roundToString()
    }

    private fun restart(): Boolean {
        try {
            val replicCount = replicationsCount.value.toInt()
            val patients = numberOfPatients.value.toInt()
            val workers = numberOfAdminWorkers.value.toInt()
            val doctors = numberOfDoctors.value.toInt()
            val nurses = numberOfNurses.value.toInt()
            val withAnimation: Boolean = withAnimation.value

            simulation = VaccinationCentreSimulation(replicCount, patients, workers, doctors, nurses, withAnimation)
            simulation.setDelayEverySimSeconds(delayEvery.value)
            simulation.setDelayForMillis(delayFor.value)

            restartStatistics()
            setAllActionListeners()

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

    private fun restartStatistics() {
        avgBeforeRegistrationQueueLen.restart()
        avgBeforeExaminationQueueLen.restart()
        avgBeforeVaccinationQueueLen.restart()

        avgWaitingTimeRegistrationQueue.restart()
        avgWaitingTimeExaminationQueue.restart()
        avgWaitingTimeVaccinationQueue.restart()

        averageWorkloadAdministrativeWorkerStats.restart()
        averageWorkloadDoctorStats.restart()
        averageWorkloadNurseStats.restart()

        averageWaitingPatientsCountStats.restart()
    }

    private fun setAllActionListeners() {
        with(simulation) {
            registrationQueue.setBeforeQueueLengthChangedActionListener(avgBeforeRegistrationQueueLen)
            examinationQueue.setBeforeQueueLengthChangedActionListener(avgBeforeExaminationQueueLen)
            vaccinationQueue.setBeforeQueueLengthChangedActionListener(avgBeforeVaccinationQueueLen)

            registrationRoom.setOnWaitingStoppedActionListener(avgWaitingTimeRegistrationQueue)
            examinationRoom.setOnWaitingStoppedActionListener(avgWaitingTimeExaminationQueue)
            vaccinationRoom.setOnWaitingStoppedActionListener(avgWaitingTimeVaccinationQueue)

            registrationRoom.setBeforeWorkersStateChangedActionListener(averageWorkloadAdministrativeWorkerStats)
            examinationRoom.setBeforeWorkersStateChangedActionListener(averageWorkloadDoctorStats)
            vaccinationRoom.setBeforeWorkersStateChangedActionListener(averageWorkloadNurseStats)

            waitingRoom.setBeforePatientsCountChangedActionListener(averageWaitingPatientsCountStats)
        }
        simulation.setAnimationActionListener(this)
    }

}