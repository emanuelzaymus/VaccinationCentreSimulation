package vaccinationcentresimulation.events.waiting

import random.ContinuousUniformDistribution
import utils.pool.IPooledObject
import vaccinationcentresimulation.VaccinationCentreSimulation
import vaccinationcentresimulation.entities.waiting.WaitingRoom
import vaccinationcentresimulation.events.VaccinationCentreEvent

class WaitingEndEvent(simulation: VaccinationCentreSimulation, private val waitingRoom: WaitingRoom) :
    VaccinationCentreEvent(simulation), IPooledObject {

    companion object {
        private val waitingRandom = ContinuousUniformDistribution()
    }

    override fun execute() {
        patient.stopWaiting(eventTime)
        simulation.releasePatient(patient)
        waitingRoom.releaseWaitingEndEvent(this)
    }

    override fun eventDuration(): Double = if (waitingRandom.next() < 0.95) 15.0 else 30.0

    override fun toString() = "WAIT_EN - ${super.toString()}"

}