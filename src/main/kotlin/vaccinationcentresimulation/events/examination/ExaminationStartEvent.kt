package vaccinationcentresimulation.events.examination

import vaccinationcentresimulation.VaccinationCentreSimulation
import vaccinationcentresimulation.entities.examination.Doctor
import vaccinationcentresimulation.events.VaccinationCentreEvent

class ExaminationStartEvent(simulation: VaccinationCentreSimulation, private val doctor: Doctor) :
    VaccinationCentreEvent(simulation) {

    override fun execute() {
        doctor.busy = true
        patient.stopWaiting(eventTime)
        doctor.scheduleEnd(patient, eventTime)
    }

    override fun toString() = "EXA_STA - ${super.toString()}"

}