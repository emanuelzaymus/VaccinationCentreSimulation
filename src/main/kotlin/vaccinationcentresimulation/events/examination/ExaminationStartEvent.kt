package vaccinationcentresimulation.events.examination

import vaccinationcentresimulation.VaccinationCentreSimulation
import vaccinationcentresimulation.entities.examination.Doctor
import vaccinationcentresimulation.events.VaccinationCentreActivityStartEvent

/**
 * Start of patient examination.
 */
class ExaminationStartEvent(simulation: VaccinationCentreSimulation, doctor: Doctor) :
    VaccinationCentreActivityStartEvent(simulation, doctor) {

    override val toStringTitle = "EXA_STA"

}