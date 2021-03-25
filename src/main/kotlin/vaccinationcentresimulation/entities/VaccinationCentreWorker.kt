package vaccinationcentresimulation.entities

import utils.busylist.IBusyObject
import vaccinationcentresimulation.events.IOnWaitingStoppedActionListener
import vaccinationcentresimulation.events.VaccinationCentreActivityStartEvent
import vaccinationcentresimulation.events.VaccinationCentreEvent

abstract class VaccinationCentreWorker : IBusyObject {

    override var busy: Boolean = false
        set(value) {
            if (field == value)
                throw IllegalArgumentException("You cannot reassigned this property with the same value: $value.")
            field = value
        }

    protected abstract val startEvent: VaccinationCentreActivityStartEvent
    protected abstract val endEvent: VaccinationCentreEvent

    fun scheduleStart(patient: Patient, eventTime: Double) {
        startEvent.schedule(patient, eventTime)
    }

    fun scheduleEnd(patient: Patient, eventTime: Double) {
        endEvent.schedule(patient, eventTime)
    }

    fun setOnWaitingStoppedActionListener(listener: IOnWaitingStoppedActionListener) {
        startEvent.setOnWaitingStoppedActionListener(listener)
    }

}