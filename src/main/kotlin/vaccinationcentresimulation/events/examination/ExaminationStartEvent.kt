package vaccinationcentresimulation.events.examination

import vaccinationcentresimulation.VaccinationCentreSimulation
import vaccinationcentresimulation.entities.examination.Doctor
import vaccinationcentresimulation.events.VaccinationCentreActivityStartEvent

class ExaminationStartEvent(simulation: VaccinationCentreSimulation, doctor: Doctor) :
    VaccinationCentreActivityStartEvent(simulation, doctor) {

    override val toStringTitle = "EXA_STA"

}