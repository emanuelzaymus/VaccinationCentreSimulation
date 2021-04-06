package vaccinationcentresimulation.events.waiting

import random.continuous.ContinuousUniformDistribution
import utils.pool.IPooledObject
import vaccinationcentresimulation.VaccinationCentreSimulation
import vaccinationcentresimulation.constants.WAITING_EVENT_DURATION_LESS
import vaccinationcentresimulation.constants.WAITING_EVENT_DURATION_MORE
import vaccinationcentresimulation.constants.WAITING_EVENT_LESS_PROBABILITY
import vaccinationcentresimulation.entities.waiting.WaitingRoom
import vaccinationcentresimulation.events.VaccinationCentreEvent

/**
 * End of waiting after vaccination.
 */
class WaitingEndEvent(simulation: VaccinationCentreSimulation, private val waitingRoom: WaitingRoom) :
    VaccinationCentreEvent(simulation), IPooledObject {

    companion object {
        private val waitingRandom = ContinuousUniformDistribution()
    }

    override val toStringTitle = "WAIT_EN"

    override fun execute() {
        waitingRoom.decrementWaitingPatientsCount(eventTime)
        simulation.releasePatient(patient)
        waitingRoom.releaseWaitingEndEvent(this)
    }

    override fun eventDuration(): Double =
        if (waitingRandom.next() < WAITING_EVENT_LESS_PROBABILITY) WAITING_EVENT_DURATION_LESS
        else WAITING_EVENT_DURATION_MORE

}