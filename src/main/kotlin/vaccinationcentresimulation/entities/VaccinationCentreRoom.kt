package vaccinationcentresimulation.entities

import utils.IReusable
import utils.busylist.BusyList
import vaccinationcentresimulation.VaccinationCentreSimulation
import vaccinationcentresimulation.events.IBeforeWorkersStateChangedActionListener
import vaccinationcentresimulation.events.IOnWaitingStoppedActionListener

/**
 * Common abstract room base for Vaccination Centre.
 */
abstract class VaccinationCentreRoom<T : VaccinationCentreWorker>(
    val simulation: VaccinationCentreSimulation, numberOfWorkers: Int, init: (Int) -> T
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

    fun setBeforeWorkersStateChangedActionListeners(listeners: List<IBeforeWorkersStateChangedActionListener>) {
        if (workers.size != listeners.size)
            throw IllegalArgumentException("Number of listeners is not equal with number of workers.")

        workers.zip(listeners).forEach { pair -> pair.first.setBeforeWorkersStateChangedActionListener(pair.second) }
    }

    fun getBusyWorkersCount(): Int = workers.count { it.isBusy() }

    fun getWorkersState(): List<Boolean> = workers.map { it.isBusy() }

    override fun restart() = workers.restart()

    override fun checkFinalState() = workers.checkFinalState()

}