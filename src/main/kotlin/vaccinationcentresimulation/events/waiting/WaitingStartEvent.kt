package vaccinationcentresimulation.events.waiting

import utils.pool.IPooledObject
import vaccinationcentresimulation.VaccinationCentreSimulation
import vaccinationcentresimulation.entities.waiting.WaitingRoom
import vaccinationcentresimulation.events.VaccinationCentreEvent

class WaitingStartEvent(simulation: VaccinationCentreSimulation, private val waitingRoom: WaitingRoom) :
    VaccinationCentreEvent(simulation), IPooledObject {

    override fun execute() {
        patient.startWaiting(eventTime)
        waitingRoom.scheduleEndWaiting(patient, eventTime)
        waitingRoom.releaseWaitingStartEvent(this)
    }

    override fun toString() = "WAIT_ST - ${super.toString()}"

}