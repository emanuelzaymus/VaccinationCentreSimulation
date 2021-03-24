package vaccinationcentresimulation.entities.vaccination

import vaccinationcentresimulation.VaccinationCentreSimulation
import vaccinationcentresimulation.entities.VaccinationCentreRoom

class VaccinationRoom(numberOfNurses: Int, simulation: VaccinationCentreSimulation) :
    VaccinationCentreRoom<Nurse>(simulation, numberOfNurses, { Nurse(simulation) })