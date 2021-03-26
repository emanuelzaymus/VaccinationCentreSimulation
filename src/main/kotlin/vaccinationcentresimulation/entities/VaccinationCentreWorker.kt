package vaccinationcentresimulation.entities

import utils.busylist.IBusyObject
import utils.stopwatch.Stopwatch
import vaccinationcentresimulation.events.IOnWaitingStoppedActionListener
import vaccinationcentresimulation.events.IBeforeWorkersStateChangedActionListener
import vaccinationcentresimulation.events.VaccinationCentreActivityStartEvent
import vaccinationcentresimulation.events.VaccinationCentreEvent

abstract class VaccinationCentreWorker : IBusyObject {

    private val workingStopwatch = Stopwatch()
    private var beforeWorkersStateChangedActionListener: IBeforeWorkersStateChangedActionListener? = null

    protected abstract val startEvent: VaccinationCentreActivityStartEvent
    protected abstract val endEvent: VaccinationCentreEvent

    private var busy: Boolean = false
        set(value) {
            if (field == value)
                throw IllegalArgumentException("You cannot reassigned this property with the same value: $value.")
            field = value
        }

    fun scheduleStart(patient: Patient, eventTime: Double) {
        startEvent.schedule(patient, eventTime)
    }

    fun scheduleEnd(patient: Patient, eventTime: Double) {
        endEvent.schedule(patient, eventTime)
    }

    override fun isBusy() = busy

    override fun setBusy(busy: Boolean, eventTime: Double) {
        workingStopwatch.stop(eventTime)
        beforeWorkersStateChangedActionListener?.handleBeforeWorkersStateChanged(
            this.busy,
            workingStopwatch.getElapsedTime()
        )
        this.busy = busy
        workingStopwatch.start(eventTime)
    }

    fun setOnWaitingStoppedActionListener(listener: IOnWaitingStoppedActionListener) {
        startEvent.setOnWaitingStoppedActionListener(listener)
    }

    fun setBeforeWorkersStateChangedActionListener(listener: IBeforeWorkersStateChangedActionListener) {
        beforeWorkersStateChangedActionListener = listener
    }

}