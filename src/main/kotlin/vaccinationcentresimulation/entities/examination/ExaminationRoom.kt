package vaccinationcentresimulation.entities.examination

import utils.busylist.BusyList
import vaccinationcentresimulation.VaccinationCentreSimulation
import vaccinationcentresimulation.entities.Patient
import vaccinationcentresimulation.entities.VaccinationCentrePlace

class ExaminationRoom(numberOfDoctors: Int, simulation: VaccinationCentreSimulation) :
    VaccinationCentrePlace(simulation) {

    private val doctors = BusyList(numberOfDoctors) { Doctor(this) }

    fun anyDoctorAvailable(): Boolean = doctors.anyAvailable()

    fun scheduleStartExamination(patient: Patient, eventTime: Double) {
        val doctor = doctors.getRandomAvailable()
        doctor.scheduleStartExamination(patient, eventTime)
    }

}