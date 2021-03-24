package vaccinationcentresimulation.events.examination

import vaccinationcentresimulation.VaccinationCentreSimulation
import vaccinationcentresimulation.entities.examination.Doctor
import vaccinationcentresimulation.events.VaccinationCentreEvent

class ExaminationStartEvent(simulation: VaccinationCentreSimulation, private val doctor: Doctor) :
    VaccinationCentreEvent(simulation) {

    override fun execute() {
        doctor.busy = true
        patient.stopWaiting(eventTime)
        doctor.scheduleEndExamination(patient, eventTime)
    }

    override fun eventDuration(): Double = .0

}