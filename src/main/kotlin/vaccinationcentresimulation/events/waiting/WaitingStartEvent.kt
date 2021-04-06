package vaccinationcentresimulation.events.waiting

import utils.pool.IPooledObject
import vaccinationcentresimulation.VaccinationCentreSimulation
import vaccinationcentresimulation.entities.waiting.WaitingRoom
import vaccinationcentresimulation.events.VaccinationCentreEvent

/**
 * Start of waiting after vaccination.
 */
class WaitingStartEvent(simulation: VaccinationCentreSimulation, private val waitingRoom: WaitingRoom) :
    VaccinationCentreEvent(simulation), IPooledObject {

    override val toStringTitle = "WAIT_ST"

    override fun execute() {
        waitingRoom.incrementWaitingPatientsCount(eventTime)
        waitingRoom.scheduleEndWaiting(patient, eventTime)
        waitingRoom.releaseWaitingStartEvent(this)
    }

}