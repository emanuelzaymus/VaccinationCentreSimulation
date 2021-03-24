package vaccinationcentresimulation.entities.vaccination

import utils.busylist.BusyList
import vaccinationcentresimulation.VaccinationCentreSimulation
import vaccinationcentresimulation.entities.Patient
import vaccinationcentresimulation.entities.VaccinationCentrePlace

class VaccinationRoom(numberOfNurses: Int, simulation: VaccinationCentreSimulation) :
    VaccinationCentrePlace(simulation) {

    private val nurses = BusyList(numberOfNurses) { Nurse(this) }

    fun anyNurseAvailable(): Boolean = nurses.anyAvailable()

    fun scheduleStartVaccination(patient: Patient, eventTime: Double) {
        val nurse = nurses.getRandomAvailable()
        nurse.scheduleStartVaccination(patient, eventTime)
    }

}