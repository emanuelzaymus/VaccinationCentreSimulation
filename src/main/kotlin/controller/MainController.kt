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
    val delayFor = SimpleIntegerProperty(1000).apply { onChange { millis -> simulation.setDelayForMillis(millis) } }
    val appState = SimpleStringProperty(AppState.READY.name)

    val actualSimTime = SimpleStringProperty("---")

    val regQueueActualLength = SimpleIntegerProperty()
    val regQueueAvgLength = SimpleStringProperty()
    val regQueueAvgWaitingTime = SimpleStringProperty()
    val regRoomBusyWorkers = SimpleIntegerProperty()
    val regRoomWorkload = SimpleStringProperty()

    val examQueueActualLength = SimpleIntegerProperty()
    val examQueueAvgLength = SimpleStringProperty()
    val examQueueAvgWaitingTime = SimpleStringProperty()
    val examRoomBusyWorkers = SimpleIntegerProperty()
    val examRoomWorkload = SimpleStringProperty()

    val vacQueueActualLength = SimpleIntegerProperty()
    val vacQueueAvgLength = SimpleStringProperty()
    val vacQueueAvgWaitingTime = SimpleStringProperty()
    val vacRoomBusyWorkers = SimpleIntegerProperty()
    val vacRoomWorkload = SimpleStringProperty()

    val waitRoomPatientsCount = SimpleIntegerProperty()
    val waitRoomAvgLength = SimpleStringProperty()

    fun startPause() {
        if (!isRunning.get()) {
            start()
        } else {
            if (!simulation.isPaused()) {
                simulation.pauseSimulation()
                appState.value = AppState.PAUSED.name
            } else {
                simulation.restoreSimulation()
                appState.value = AppState.RESTORED.name
            }
        }
    }

    private fun start() {
        if (restart()) {
            isRunning.set(true)
            appState.value = AppState.STARTED.name
            thread(isDaemon = true, name = "SimThread") {
                simulation.simulate()
                isRunning.set(false)
            }
        }
    }

    fun stop() {
        simulation.stop()
        isRunning.set(false)
        appState.value = AppState.STOPPED.name
    }

    override fun handleOnTimeChanged(actualSimulationTime: Double) {
        Platform.runLater { actualSimTime.value = actualSimulationTime.toString() }
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