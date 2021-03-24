package vaccinationcentresimulation.entities.waiting

import utils.pool.Pool
import vaccinationcentresimulation.VaccinationCentreSimulation
import vaccinationcentresimulation.entities.Patient
import vaccinationcentresimulation.events.waiting.WaitingEndEvent
import vaccinationcentresimulation.events.waiting.WaitingStartEvent

class WaitingRoom(private val simulation: VaccinationCentreSimulation) {

    private val waitingStartEventPool = Pool { WaitingStartEvent(simulation, this) }
    private val waitingEndEventPool = Pool { WaitingEndEvent(simulation, this) }

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

}