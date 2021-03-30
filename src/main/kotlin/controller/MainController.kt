package controller

import javafx.application.Platform
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.control.Alert
import tornadofx.Controller
import tornadofx.alert
import tornadofx.onChange
import vaccinationcentresimulation.IAnimationActionListener
import vaccinationcentresimulation.VaccinationCentreSimulation
import vaccinationcentresimulation.statistics.AverageQueueLengthStats
import vaccinationcentresimulation.statistics.AverageWaitingPatientsCountStats
import vaccinationcentresimulation.statistics.AverageWaitingTimeStats
import vaccinationcentresimulation.statistics.AverageWorkloadStats
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.concurrent.thread

class MainController : Controller(), IAnimationActionListener {

    // Working time starts at 8:00
    private val startTime = 8 * 60

    private val avgBeforeRegistrationQueueLen = AverageQueueLengthStats()
    private val avgBeforeExaminationQueueLen = AverageQueueLengthStats()
    private val avgBeforeVaccinationQueueLen = AverageQueueLengthStats()

    private val avgWaitingTimeRegistrationQueue = AverageWaitingTimeStats()
    private val avgWaitingTimeExaminationQueue = AverageWaitingTimeStats()
    private val avgWaitingTimeVaccinationQueue = AverageWaitingTimeStats()

    private val averageWorkloadAdministrativeWorkerStats = AverageWorkloadStats()
    private val averageWorkloadDoctorStats = AverageWorkloadStats()
    private val averageWorkloadNurseStats = AverageWorkloadStats()

    private val averageWaitingPatientsCountStats = AverageWaitingPatientsCountStats()

    private var simulation = VaccinationCentreSimulation()

    private val isRunning = AtomicBoolean(false)

    val replicationsCount = SimpleStringProperty("1")
    val skip = SimpleStringProperty("0.3")
    val numberOfPatients = SimpleStringProperty("450")
    val numberOfAdminWorkers = SimpleStringProperty("6")
    val numberOfDoctors = SimpleStringProperty("5")
    val numberOfNurses = SimpleStringProperty("3")

    val withAnimation = SimpleBooleanProperty(true).apply { onChange { b -> simulation.setAnimation(b) } }
    val delayEvery =
        SimpleIntegerProperty(60).apply { onChange { seconds -> simulation.setDelayEverySimMin(seconds) } }
    val delayFor = SimpleIntegerProperty(100).apply { onChange { millis -> simulation.setDelayForMillis(millis) } }

    private var appState = AppState.READY
        set(value) {
            Platform.runLater { state.value = value.name }
            field = value
        }
    val state = SimpleStringProperty(appState.name)

    val actualSimTime = SimpleStringProperty("-")

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
        if (!isRunning.get()) {
            start()
        } else if (!simulation.isPaused()) {
            simulation.pauseSimulation()
            appState = AppState.PAUSED
        } else {
            simulation.restoreSimulation()
            appState = AppState.RESTORED
        }
    }

    private fun start() {
        if (restart()) {
            isRunning.set(true)
            appState = AppState.STARTED
            thread(isDaemon = true, name = "SIM_THREAD") {
                simulation.simulate()
                isRunning.set(false)
                appState = AppState.READY
            }
        }
    }

    fun stop() {
        simulation.stop()
        isRunning.set(false)
        appState = AppState.STOPPED
    }

    override fun handleOnTimeChanged(actualSimulationTime: Double) {
        Platform.runLater {
            actualSimTime.value =
                "${(actualSimulationTime + startTime).minutesToTime()} | (Minutes: $actualSimulationTime)"
        }
    }

    override fun updateRegistrationQueueLength(length: Int) {
        Platform.runLater { regQueueActualLength.value = length }
    }

    override fun updateExaminationQueueLength(length: Int) {
        Platform.runLater { examQueueActualLength.value = length }
    }

    override fun updateVaccinationQueueLength(length: Int) {
        Platform.runLater { vacQueueActualLength.value = length }
    }

    override fun updateRegistrationRoomBusyWorkersCount(busyWorkers: Int) {
        Platform.runLater { regRoomBusyWorkers.value = busyWorkers }
    }

    override fun updateExaminationRoomBusyDoctorsCount(busyDoctors: Int) {
        Platform.runLater { examRoomBusyWorkers.value = busyDoctors }
    }

    override fun updateVaccinationRoomBusyNursesCount(busyNurses: Int) {
        Platform.runLater { vacRoomBusyWorkers.value = busyNurses }
    }

    override fun updateWaitingRoomPatientsCount(patients: Int) {
        Platform.runLater { waitRoomPatientsCount.value = patients }
    }

    override fun updateStatistics() {
        Platform.runLater {
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
            simulation.setDelayEverySimMin(delayEvery.value)
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
            beforeRegistrationQueue.setBeforeQueueLengthChangedActionListener(avgBeforeRegistrationQueueLen)
            beforeExaminationQueue.setBeforeQueueLengthChangedActionListener(avgBeforeExaminationQueueLen)
            beforeVaccinationQueue.setBeforeQueueLengthChangedActionListener(avgBeforeVaccinationQueueLen)

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