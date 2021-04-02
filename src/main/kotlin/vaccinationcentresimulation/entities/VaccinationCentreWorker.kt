package vaccinationcentresimulation.entities

import utils.busylist.IBusyObject
import vaccinationcentresimulation.events.IBeforeWorkersStateChangedActionListener
import vaccinationcentresimulation.events.IOnWaitingStoppedActionListener
import vaccinationcentresimulation.events.VaccinationCentreActivityStartEvent
import vaccinationcentresimulation.events.VaccinationCentreEvent

abstract class VaccinationCentreWorker : IBusyObject {

    //    private var beforeWorkersStateChangedActionListener: IBeforeWorkersStateChangedActionListener? = null
    private var beforeWorkersStateChangedActionListeners = mutableListOf<IBeforeWorkersStateChangedActionListener>()
    private var lastChange = .0

    protected abstract val startEvent: VaccinationCentreActivityStartEvent
    protected abstract val endEvent: VaccinationCentreEvent

    private var busy: Boolean = false
        set(value) {
            if (field == value) throw IllegalArgumentException("You cannot reassigned this property with the same value: $value.")
            field = value
        }

    fun scheduleStart(patient: Patient, eventTime: Double) {
        startEvent.schedule(patient, eventTime)
    }

    fun scheduleEnd(patient: Patient, eventTime: Double) {
        endEvent.schedule(patient, eventTime)
    }

    override fun isBusy() = busy

    override fun setBusy(busy: Boolean, eventTime: Double, commonTotalTime: Double) {
//        beforeWorkersStateChangedActionListener?.handleBeforeWorkersStateChanged(this.busy, eventTime - lastChange)
        beforeWorkersStateChangedActionListeners.forEach {
            it.handleBeforeWorkersStateChanged(this.busy, eventTime - lastChange, commonTotalTime)
        }
        this.busy = busy
        lastChange = eventTime
    }

    override fun restart() {
        lastChange = .0
    }

    override fun checkFinalState() {
        if (busy) throw IllegalStateException("The worker is still busy.")
    }

    fun setOnWaitingStoppedActionListener(listener: IOnWaitingStoppedActionListener) {
        startEvent.setOnWaitingStoppedActionListener(listener)
    }

    fun setBeforeWorkersStateChangedActionListener(listener: IBeforeWorkersStateChangedActionListener) {
//        beforeWorkersStateChangedActionListener = listener
        beforeWorkersStateChangedActionListeners.add(listener)
    }

}