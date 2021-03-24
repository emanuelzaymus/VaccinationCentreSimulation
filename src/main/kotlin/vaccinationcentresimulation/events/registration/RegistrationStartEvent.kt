package vaccinationcentresimulation.events.registration

import vaccinationcentresimulation.VaccinationCentreSimulation
import vaccinationcentresimulation.events.VaccinationCentreEvent
import vaccinationcentresimulation.entities.registration.AdministrativeWorker

class RegistrationStartEvent(simulation: VaccinationCentreSimulation, private val worker: AdministrativeWorker) :
    VaccinationCentreEvent(simulation) {

    override fun execute() {
        worker.busy = true
        patient.stopWaiting(eventTime)
        worker.scheduleEnd(patient, eventTime)
    }

    // TODO: make open fun eventDuration() -> default method
    override fun eventDuration(): Double = .0

}