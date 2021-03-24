package vaccinationcentresimulation.entities

import utils.busylist.BusyObject
import vaccinationcentresimulation.events.VaccinationCentreEvent

abstract class VaccinationCentreEmployee : BusyObject() {

    protected abstract val startEvent: VaccinationCentreEvent
    protected abstract val endEvent: VaccinationCentreEvent

    fun scheduleStart(patient: Patient, eventTime: Double) {
        startEvent.schedule(patient, eventTime)
    }

    fun scheduleEnd(patient: Patient, eventTime: Double) {
        endEvent.schedule(patient, eventTime)
    }

}