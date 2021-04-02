package controller

import javafx.application.Platform
import javafx.beans.property.*
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

    private val initReplicationsCount = 1
    private val initNumberOfPatients = 540
    private val initNumberOfAdminWorkers = 6
    private val initNumberOfDoctors = 5
    private val initNumberOfNurses = 3
    private val initWithAnimation = true

    // Working time starts at 8:00:00
    private val startTime = 8 * 60 * 60.0

    private val registrationQueueLength = QueueLengthStats()
    private val examinationQueueLength = QueueLengthStats()
    private val vaccinationQueueLength = QueueLengthStats()

    private val registrationWaitingTime = WaitingTimeStats()
    private val examinationWaitingTime = WaitingTimeStats()
    private val vaccinationWaitingTime = WaitingTimeStats()

    private val adminWorkersWorkload = WorkloadStats()
    private val doctorsWorkload = WorkloadStats()
    private val nursesWorkload = WorkloadStats()

    private var adminWorkersPersonalWorkload = List(initNumberOfAdminWorkers) { WorkloadStats() }
    private var doctorsPersonalWorkload = List(initNumberOfDoctors) { WorkloadStats() }
    private var nursesPersonalWorkload = List(initNumberOfNurses) { WorkloadStats() }

    private val waitingPatientsCount = WaitingPatientsCountStats()

    private var simulation = VaccinationCentreSimulation(
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
        onChange { b -> simulation.withAnimation = b }
    }
    val delayEvery = SimpleIntegerProperty(60).apply {
        onChange { seconds -> simulation.setDelayEverySimUnits(seconds.toDouble()) }
    }
    val delayFor = SimpleLongProperty(100).apply {
        onChange { millis -> simulation.setDelayForMillis(millis) }
    }

    val state = SimpleStringProperty("READY")
    val actualSimTime = SimpleStringProperty(startTime.secondsToTime())

    private val initVal = 0.0.roundToString()
    val regQueueActualLength = SimpleIntegerProperty()
    val regQueueAvgLength = SimpleStringProperty(initVal)
    val regQueueAvgWaitingTime = SimpleStringProperty(initVal)
    val regRoomBusyWorkers = SimpleIntegerProperty()
    val regRoomWorkload = SimpleStringProperty(initVal)
    val regRoomPersonalWorkload = SimpleListProperty<Unit>()

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
        regQueueAvgLength.value = registrationQueueLength.getAverage().roundToString()
        regQueueAvgWaitingTime.value = registrationWaitingTime.getAverage().roundToString()
        regRoomWorkload.value = adminWorkersWorkload.getAverage().roundToString()
//        adminWorkersPersonalWorkload

        examQueueAvgLength.value = examinationQueueLength.getAverage().roundToString()
        examQueueAvgWaitingTime.value = examinationWaitingTime.getAverage().roundToString()
        examRoomWorkload.value = doctorsWorkload.getAverage().roundToString()

        vacQueueAvgLength.value = vaccinationQueueLength.getAverage().roundToString()
        vacQueueAvgWaitingTime.value = vaccinationWaitingTime.getAverage().roundToString()
        vacRoomWorkload.value = nursesWorkload.getAverage().roundToString()

        waitRoomAvgLength.value = waitingPatientsCount.getAverage().roundToString()
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
            simulation.setDelayEverySimUnits(delayEvery.value.toDouble())
            simulation.setDelayForMillis(delayFor.value)

            restartStatistics(workers, doctors, nurses)
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

    private fun restartStatistics(numOfWorkers: Int, numOfDoctors: Int, numOfNurses: Int) {
        registrationQueueLength.restart()
        examinationQueueLength.restart()
        vaccinationQueueLength.restart()

        registrationWaitingTime.restart()
        examinationWaitingTime.restart()
        vaccinationWaitingTime.restart()

        adminWorkersWorkload.restart()
        doctorsWorkload.restart()
        nursesWorkload.restart()

        adminWorkersPersonalWorkload = List(numOfWorkers) { WorkloadStats() }
        doctorsPersonalWorkload = List(numOfDoctors) { WorkloadStats() }
        nursesPersonalWorkload = List(numOfNurses) { WorkloadStats() }

        waitingPatientsCount.restart()
    }

    private fun setAllActionListeners() {
        with(simulation) {
            registrationQueue.setBeforeQueueLengthChangedActionListener(registrationQueueLength)
            examinationQueue.setBeforeQueueLengthChangedActionListener(examinationQueueLength)
            vaccinationQueue.setBeforeQueueLengthChangedActionListener(vaccinationQueueLength)

            registrationRoom.setOnWaitingStoppedActionListener(registrationWaitingTime)
            examinationRoom.setOnWaitingStoppedActionListener(examinationWaitingTime)
            vaccinationRoom.setOnWaitingStoppedActionListener(vaccinationWaitingTime)

            registrationRoom.setBeforeEachWorkersStateChangedActionListener(adminWorkersWorkload)
            examinationRoom.setBeforeEachWorkersStateChangedActionListener(doctorsWorkload)
            vaccinationRoom.setBeforeEachWorkersStateChangedActionListener(nursesWorkload)

            registrationRoom.setBeforeWorkersStateChangedActionListeners(adminWorkersPersonalWorkload)
            examinationRoom.setBeforeWorkersStateChangedActionListeners(doctorsPersonalWorkload)
            vaccinationRoom.setBeforeWorkersStateChangedActionListeners(nursesPersonalWorkload)

            waitingRoom.setBeforePatientsCountChangedActionListener(waitingPatientsCount)
        }
        simulation.setAnimationActionListener(this)
    }

}