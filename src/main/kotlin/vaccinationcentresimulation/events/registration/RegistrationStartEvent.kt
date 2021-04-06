package vaccinationcentresimulation.events.registration

import vaccinationcentresimulation.VaccinationCentreSimulation
import vaccinationcentresimulation.entities.registration.AdministrativeWorker
import vaccinationcentresimulation.events.VaccinationCentreActivityStartEvent

/**
 * Start of registration.
 */
class RegistrationStartEvent(simulation: VaccinationCentreSimulation, worker: AdministrativeWorker) :
    VaccinationCentreActivityStartEvent(simulation, worker) {

    override val toStringTitle = "REG_STA"

}