package vaccinationcentresimulation.entities.vaccination

import vaccinationcentresimulation.VaccinationCentreSimulation
import vaccinationcentresimulation.entities.VaccinationCentreWorker
import vaccinationcentresimulation.events.vaccination.VaccinationEndEvent
import vaccinationcentresimulation.events.vaccination.VaccinationStartEvent

class Nurse(simulation: VaccinationCentreSimulation) : VaccinationCentreWorker() {

    override val startEvent = VaccinationStartEvent(simulation, this)
    override val endEvent = VaccinationEndEvent(simulation, this)

}