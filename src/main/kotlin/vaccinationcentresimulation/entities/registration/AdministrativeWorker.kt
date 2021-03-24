package vaccinationcentresimulation.entities.registration

import utils.busylist.BusyObject
import vaccinationcentresimulation.entities.Patient
import vaccinationcentresimulation.entities.VaccinationCentrePlace
import vaccinationcentresimulation.events.registration.RegistrationEndEvent
import vaccinationcentresimulation.events.registration.RegistrationStartEvent

class AdministrativeWorker(place: VaccinationCentrePlace) : BusyObject() {

    private val startRegistrationEvent = RegistrationStartEvent(place.simulation, this)
    private val endRegistrationEvent = RegistrationEndEvent(place.simulation, this)

    fun scheduleStartRegistration(patient: Patient, eventTime: Double) {
        startRegistrationEvent.schedule(patient, eventTime)
    }

    fun scheduleEndRegistration(patient: Patient, eventTime: Double) {
        endRegistrationEvent.schedule(patient, eventTime)
    }

}
