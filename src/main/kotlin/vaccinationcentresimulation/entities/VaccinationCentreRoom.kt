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

    fun setBeforeWorkersStateChangedActionListener(listener: IBeforeWorkersStateChangedActionListener) {
        workers.forEach { it.setBeforeWorkersStateChangedActionListener(listener) }
    }

    fun getBusyWorkersCount(): Int = workers.getBusyWorkersCount()

    override fun restart() = workers.restart()

    override fun checkFinalState() = workers.checkFinalState()

}