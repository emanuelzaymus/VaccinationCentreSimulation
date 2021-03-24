package vaccinationcentresimulation.entities.registration

import vaccinationcentresimulation.VaccinationCentreSimulation
import vaccinationcentresimulation.entities.Patient
import vaccinationcentresimulation.entities.VaccinationCentreEmployee
import vaccinationcentresimulation.events.registration.RegistrationEndEvent
import vaccinationcentresimulation.events.registration.RegistrationStartEvent

class AdministrativeWorker(simulation: VaccinationCentreSimulation) : VaccinationCentreEmployee() {

    private val startRegistrationEvent = RegistrationStartEvent(simulation, this)
    private val endRegistrationEvent = RegistrationEndEvent(simulation, this)

    fun scheduleStartRegistration(patient: Patient, eventTime: Double) {
        startRegistrationEvent.schedule(patient, eventTime)
    }

    fun scheduleEndRegistration(patient: Patient, eventTime: Double) {
        endRegistrationEvent.schedule(patient, eventTime)
    }

    override fun scheduleStart(patient: Patient, eventTime: Double) {
        startRegistrationEvent.schedule(patient, eventTime)
    }

}
