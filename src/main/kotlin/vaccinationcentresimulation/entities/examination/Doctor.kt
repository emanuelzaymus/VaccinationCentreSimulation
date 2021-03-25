package vaccinationcentresimulation.entities.examination

import vaccinationcentresimulation.VaccinationCentreSimulation
import vaccinationcentresimulation.entities.VaccinationCentreWorker
import vaccinationcentresimulation.events.examination.ExaminationEndEvent
import vaccinationcentresimulation.events.examination.ExaminationStartEvent

class Doctor(simulation: VaccinationCentreSimulation) : VaccinationCentreWorker() {

    override val startEvent = ExaminationStartEvent(simulation, this)
    override val endEvent = ExaminationEndEvent(simulation, this)

}