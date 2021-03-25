package vaccinationcentresimulation.entities

import utils.busylist.BusyList
import vaccinationcentresimulation.VaccinationCentreSimulation

abstract class VaccinationCentreRoom<T : VaccinationCentreWorker>(
    val simulation: VaccinationCentreSimulation,
    numberOfWorkers: Int,
    init: (Int) -> T
) {
    private val workers = BusyList(numberOfWorkers, init)

    fun anyWorkerAvailable(): Boolean = workers.anyAvailable()

    fun scheduleStart(patient: Patient, eventTime: Double) {
        val worker = workers.getRandomAvailable()
        worker.scheduleStart(patient, eventTime)
    }

}
