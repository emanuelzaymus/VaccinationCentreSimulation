package vaccinationcentresimulation.entities.registration

import vaccinationcentresimulation.VaccinationCentreSimulation
import vaccinationcentresimulation.entities.VaccinationCentreRoom

class RegistrationRoom(numberOfWorkers: Int, simulation: VaccinationCentreSimulation) :
    VaccinationCentreRoom<AdministrativeWorker>(simulation, numberOfWorkers, { AdministrativeWorker(simulation) })