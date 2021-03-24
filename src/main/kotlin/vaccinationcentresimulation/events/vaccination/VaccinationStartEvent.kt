package vaccinationcentresimulation.events.vaccination

import vaccinationcentresimulation.VaccinationCentreSimulation
import vaccinationcentresimulation.entities.vaccination.Nurse
import vaccinationcentresimulation.events.VaccinationCentreEvent

class VaccinationStartEvent(simulation: VaccinationCentreSimulation, private val nurse: Nurse) :
    VaccinationCentreEvent(simulation) {

    override fun execute() {
        nurse.busy = true
        patient.stopWaiting(eventTime)
        nurse.scheduleEnd(patient, eventTime)
    }

    override fun toString() = "VAC_STA - ${super.toString()}"

}