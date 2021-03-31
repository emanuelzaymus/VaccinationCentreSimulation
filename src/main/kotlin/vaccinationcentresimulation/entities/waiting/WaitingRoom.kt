package vaccinationcentresimulation.entities.waiting

import utils.IReusable
import utils.pool.Pool
import vaccinationcentresimulation.VaccinationCentreSimulation
import vaccinationcentresimulation.entities.Patient
import vaccinationcentresimulation.events.waiting.WaitingEndEvent
import vaccinationcentresimulation.events.waiting.WaitingStartEvent

class WaitingRoom(private val simulation: VaccinationCentreSimulation) : IReusable {

    private val waitingStartEventPool = Pool { WaitingStartEvent(simulation, this) }
    private val waitingEndEventPool = Pool { WaitingEndEvent(simulation, this) }

    private var beforePatientsCountChangedActionListener: IBeforePatientsCountChangedActionListener? = null
    private var lastChange = .0
    var waitingPatientsCount = 0
        private set(value) {
            if (value < 0)
                throw IllegalArgumentException("Waiting patients count cannot be negative.")
            field = value
        }

    fun scheduleStartWaiting(patient: Patient, eventTime: Double) {
        waitingStartEventPool.acquire().schedule(patient, eventTime)
    }

    fun scheduleEndWaiting(patient: Patient, eventTime: Double) {
        waitingEndEventPool.acquire().schedule(patient, eventTime)
    }

    fun releaseWaitingStartEvent(waitingStartEvent: WaitingStartEvent) {
        waitingStartEventPool.release(waitingStartEvent)
    }

    fun releaseWaitingEndEvent(waitingEndEvent: WaitingEndEvent) {
        waitingEndEventPool.release(waitingEndEvent)
    }

    fun incrementWaitingPatientsCount(eventTime: Double) {
        beforePatientsCountChanged(eventTime)
        waitingPatientsCount++
    }

    fun decrementWaitingPatientsCount(eventTime: Double) {
        beforePatientsCountChanged(eventTime)
        waitingPatientsCount--
    }

    fun setBeforePatientsCountChangedActionListener(listener: IBeforePatientsCountChangedActionListener) {
        beforePatientsCountChangedActionListener = listener
    }

    override fun restart() {
        lastChange = .0
    }

    override fun checkFinalState() {
        if (waitingPatientsCount != 0) throw IllegalStateException("Some patients are still waiting in the waiting room.")
    }

    private fun beforePatientsCountChanged(eventTime: Double) {
        beforePatientsCountChangedActionListener?.handleBeforePatientsCountChanged(
                waitingPatientsCount,
                eventTime - lastChange
        )
        lastChange = eventTime
    }

}