package vaccinationcentresimulation.entities.examination

import vaccinationcentresimulation.VaccinationCentreSimulation
import vaccinationcentresimulation.entities.Patient
import vaccinationcentresimulation.entities.VaccinationCentreEmployee
import vaccinationcentresimulation.events.examination.ExaminationEndEvent
import vaccinationcentresimulation.events.examination.ExaminationStartEvent

class Doctor(simulation: VaccinationCentreSimulation) : VaccinationCentreEmployee() {

    private val startExaminationEvent = ExaminationStartEvent(simulation, this)
    private val endExaminationEvent = ExaminationEndEvent(simulation, this)

    fun scheduleStartExamination(patient: Patient, eventTime: Double) {
        startExaminationEvent.schedule(patient, eventTime)
    }

    fun scheduleEndExamination(patient: Patient, eventTime: Double) {
        endExaminationEvent.schedule(patient, eventTime)
    }

    override fun scheduleStart(patient: Patient, eventTime: Double) {
        startExaminationEvent.schedule(patient, eventTime)
    }

}