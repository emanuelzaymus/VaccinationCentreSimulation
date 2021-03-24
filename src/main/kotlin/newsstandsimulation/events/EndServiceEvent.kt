package newsstandsimulation.events

import newsstandsimulation.NewsStandSimulation
import random.ExponentialDistribution

class EndServiceEvent(simulation: NewsStandSimulation) : NewsStandEvent(simulation) {

    companion object {
        private val serviceDurationRandom = ExponentialDistribution(1.0 / 4)
    }

    override fun execute() {
        simulation.removeCustomer(customer)
        simulation.trader.busy = false

        if (!simulation.customerQueue.isEmpty()) {
            simulation.startServiceEvent.schedule(simulation.customerQueue.dequeue(eventTime), eventTime)
        }
    }

    override fun eventDuration(): Double = serviceDurationRandom.next()

    override fun toString() = "END_SER - " + super.toString()

}