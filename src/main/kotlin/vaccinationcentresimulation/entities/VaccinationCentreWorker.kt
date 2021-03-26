package vaccinationcentresimulation.entities

import utils.busylist.IBusyObject
import utils.stopwatch.Stopwatch
import vaccinationcentresimulation.events.IOnWaitingStoppedActionListener
import vaccinationcentresimulation.events.IOnWorkersStateChangedActionListener
import vaccinationcentresimulation.events.VaccinationCentreActivityStartEvent
import vaccinationcentresimulation.events.VaccinationCentreEvent

abstract class VaccinationCentreWorker : IBusyObject {

    private val workingStopwatch = Stopwatch()
    private var onWorkersStateChangedActionListener = IOnWorkersStateChangedActionListener.getEmptyImplementation()

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
        onWorkersStateChangedActionListener.handleOnWorkersStateChanged(this.busy, workingStopwatch.getElapsedTime())
        this.busy = busy
        workingStopwatch.start(eventTime)
    }

    fun setOnWaitingStoppedActionListener(listener: IOnWaitingStoppedActionListener) {
        startEvent.setOnWaitingStoppedActionListener(listener)
    }

    fun setOnWorkersStateChangedActionListener(listener: IOnWorkersStateChangedActionListener) {
        onWorkersStateChangedActionListener = listener
    }

}