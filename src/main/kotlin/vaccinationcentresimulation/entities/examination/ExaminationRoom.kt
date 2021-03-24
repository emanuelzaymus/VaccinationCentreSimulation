package vaccinationcentresimulation.entities.examination

import vaccinationcentresimulation.VaccinationCentreSimulation
import vaccinationcentresimulation.entities.VaccinationCentreRoom

class ExaminationRoom(numberOfDoctors: Int, simulation: VaccinationCentreSimulation) :
    VaccinationCentreRoom<Doctor>(simulation, numberOfDoctors, { Doctor(simulation) })