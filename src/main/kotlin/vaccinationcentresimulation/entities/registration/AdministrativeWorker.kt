package vaccinationcentresimulation.entities.registration

import vaccinationcentresimulation.VaccinationCentreSimulation
import vaccinationcentresimulation.entities.VaccinationCentreEmployee
import vaccinationcentresimulation.events.registration.RegistrationEndEvent
import vaccinationcentresimulation.events.registration.RegistrationStartEvent

class AdministrativeWorker(simulation: VaccinationCentreSimulation) : VaccinationCentreEmployee() {

    override val startEvent = RegistrationStartEvent(simulation, this)
    override val endEvent = RegistrationEndEvent(simulation, this)

}
