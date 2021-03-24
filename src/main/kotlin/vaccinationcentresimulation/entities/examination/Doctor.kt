package vaccinationcentresimulation.entities.examination

import utils.busylist.BusyObject
import vaccinationcentresimulation.entities.Patient
import vaccinationcentresimulation.events.examination.ExaminationEndEvent
import vaccinationcentresimulation.events.examination.ExaminationStartEvent

class Doctor(place: ExaminationRoom) : BusyObject() {

    private val startExaminationEvent = ExaminationStartEvent(place.simulation, this)
    private val endExaminationEvent = ExaminationEndEvent(place.simulation, this)

    fun scheduleStartExamination(patient: Patient, eventTime: Double) {
        startExaminationEvent.schedule(patient, eventTime)
    }

    fun scheduleEndExamination(patient: Patient, eventTime: Double) {
        endExaminationEvent.schedule(patient, eventTime)
    }

}