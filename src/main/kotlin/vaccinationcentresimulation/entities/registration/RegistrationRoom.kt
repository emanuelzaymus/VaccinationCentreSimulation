package vaccinationcentresimulation.entities.registration

import utils.busylist.BusyList
import vaccinationcentresimulation.VaccinationCentreSimulation
import vaccinationcentresimulation.entities.Patient
import vaccinationcentresimulation.entities.VaccinationCentrePlace

class RegistrationRoom(numberOfWorkers: Int, simulation: VaccinationCentreSimulation) :
    VaccinationCentrePlace(simulation) {

    private val workers = BusyList(numberOfWorkers) { AdministrativeWorker(this) }

    fun anyWorkerAvailable(): Boolean = workers.anyAvailable()

    fun scheduleStartRegistration(patient: Patient, eventTime: Double) {
        val administrativeWorker = workers.getRandomAvailable()
        administrativeWorker.scheduleStartRegistration(patient, eventTime)
    }

}