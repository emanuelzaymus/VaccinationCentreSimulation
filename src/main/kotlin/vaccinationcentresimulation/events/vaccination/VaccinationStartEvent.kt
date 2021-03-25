package vaccinationcentresimulation.events.vaccination

import vaccinationcentresimulation.VaccinationCentreSimulation
import vaccinationcentresimulation.entities.vaccination.Nurse
import vaccinationcentresimulation.events.VaccinationCentreActivityStartEvent

class VaccinationStartEvent(simulation: VaccinationCentreSimulation, nurse: Nurse) :
    VaccinationCentreActivityStartEvent(simulation, nurse) {

    override val toStringTitle = "VAC_STA"

}