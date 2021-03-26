package vaccinationcentresimulation.entities.waiting

import utils.pool.Pool
import vaccinationcentresimulation.VaccinationCentreSimulation
import vaccinationcentresimulation.entities.Patient
import vaccinationcentresimulation.events.waiting.WaitingEndEvent
import vaccinationcentresimulation.events.waiting.WaitingStartEvent

class WaitingRoom(private val simulation: VaccinationCentreSimulation) {

    private val waitingStartEventPool = Pool { WaitingStartEvent(simulation, this) }
    private val waitingEndEventPool = Pool { WaitingEndEvent(simulation, this) }

    private var beforeWaitingPatientsCountChangedActionListener =
        IBeforeWaitingPatientCountChangedActionListener.getEmptyImplementation()
    private var lastChange = .0
    private var waitingPatientsCount = 0
        set(value) {
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
        beforeWaitingPatientsCountChanged(eventTime)
        waitingPatientsCount++
    }

    fun decrementWaitingPatientsCount(eventTime: Double) {
        beforeWaitingPatientsCountChanged(eventTime)
        waitingPatientsCount--
    }

    fun setBeforeWaitingPatientsCountChangedActionListener(listener: IBeforeWaitingPatientCountChangedActionListener) {
        beforeWaitingPatientsCountChangedActionListener = listener
    }

    private fun beforeWaitingPatientsCountChanged(eventTime: Double) {
        beforeWaitingPatientsCountChangedActionListener.handleBeforeWaitingPatientsCountChanged(
            waitingPatientsCount,
            eventTime - lastChange
        )
        lastChange = eventTime
    }

}