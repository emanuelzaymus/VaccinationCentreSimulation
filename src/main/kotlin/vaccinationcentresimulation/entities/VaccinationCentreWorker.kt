package vaccinationcentresimulation.entities

import utils.busylist.IBusyObject
import vaccinationcentresimulation.events.VaccinationCentreEvent

abstract class VaccinationCentreWorker : IBusyObject {

    override var busy: Boolean = false
        set(value) {
            if (field == value)
                throw IllegalArgumentException("You cannot reassigned this property with the same value: $value.")
            field = value
        }

    protected abstract val startEvent: VaccinationCentreEvent
    protected abstract val endEvent: VaccinationCentreEvent

    fun scheduleStart(patient: Patient, eventTime: Double) {
        startEvent.schedule(patient, eventTime)
    }

    fun scheduleEnd(patient: Patient, eventTime: Double) {
        endEvent.schedule(patient, eventTime)
    }

}