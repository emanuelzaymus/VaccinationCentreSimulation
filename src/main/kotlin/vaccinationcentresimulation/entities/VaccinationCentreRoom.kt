package vaccinationcentresimulation.entities

import utils.IReusable
import utils.busylist.BusyList
import vaccinationcentresimulation.VaccinationCentreSimulation
import vaccinationcentresimulation.events.IOnWaitingStoppedActionListener
import vaccinationcentresimulation.events.IBeforeWorkersStateChangedActionListener

abstract class VaccinationCentreRoom<T : VaccinationCentreWorker>(
    val simulation: VaccinationCentreSimulation,
    numberOfWorkers: Int,
    init: (Int) -> T
) : IReusable {
    private val workers = BusyList(numberOfWorkers, init)

    fun anyWorkerAvailable(): Boolean = workers.anyAvailable()

    fun scheduleStart(patient: Patient, eventTime: Double) {
        val worker = workers.getRandomAvailable()
        worker.scheduleStart(patient, eventTime)
    }

    fun setOnWaitingStoppedActionListener(listener: IOnWaitingStoppedActionListener) {
        workers.forEach { it.setOnWaitingStoppedActionListener(listener) }
    }

    fun setBeforeEachWorkersStateChangedActionListener(listener: IBeforeWorkersStateChangedActionListener) {
        workers.forEach { it.setBeforeWorkersStateChangedActionListener(listener) }
    }

    fun setBeforeWorkersStateChangedActionListeners(listeners: List<IBeforeWorkersStateChangedActionListener>) {
        if (workers.size != listeners.size)
            throw IllegalArgumentException("Number of listeners is not equal with number of workers.")

        workers.zip(listeners).forEach { pair -> pair.first.setBeforeWorkersStateChangedActionListener(pair.second) }
//        workers.forEach { it.setBeforeWorkersStateChangedActionListener(listener) }
    }

    fun getBusyWorkersCount(): Int = workers.getBusyWorkersCount()

    override fun restart() = workers.restart()

    override fun checkFinalState() = workers.checkFinalState()

}