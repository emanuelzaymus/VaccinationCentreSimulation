package vaccinationcentresimulation.entities.vaccination

import utils.busylist.BusyObject
import vaccinationcentresimulation.entities.Patient
import vaccinationcentresimulation.events.vaccination.VaccinationEndEvent
import vaccinationcentresimulation.events.vaccination.VaccinationStartEvent

class Nurse(place: VaccinationRoom) : BusyObject() {

    private val startVaccinationEvent = VaccinationStartEvent(place.simulation, this)
    private val endVaccinationEvent = VaccinationEndEvent(place.simulation, this)

    fun scheduleStartVaccination(patient: Patient, eventTime: Double) {
        startVaccinationEvent.schedule(patient, eventTime)
    }

    fun scheduleEndVaccination(patient: Patient, eventTime: Double) {
        endVaccinationEvent.schedule(patient, eventTime)
    }

}