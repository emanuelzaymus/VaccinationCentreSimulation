package vaccinationcentresimulation.events.vaccination

import vaccinationcentresimulation.VaccinationCentreSimulation
import vaccinationcentresimulation.entities.vaccination.Nurse
import vaccinationcentresimulation.events.VaccinationCentreEvent

class VaccinationStartEvent(simulation: VaccinationCentreSimulation, private val nurse: Nurse) :
    VaccinationCentreEvent(simulation) {

    override fun execute() {
        nurse.busy = true
        patient.stopWaiting(eventTime)
        nurse.scheduleEndVaccination(patient, eventTime)
    }

    override fun eventDuration(): Double = .0

}