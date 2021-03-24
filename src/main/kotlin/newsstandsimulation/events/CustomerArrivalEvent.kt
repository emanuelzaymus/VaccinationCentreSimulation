package newsstandsimulation.events

import newsstandsimulation.NewsStandSimulation
import random.ExponentialDistribution

class CustomerArrivalEvent(simulation: NewsStandSimulation) : NewsStandEvent(simulation) {

    companion object {
        private val customerArrivalRandom = ExponentialDistribution(1.0 / 5)
    }

    override fun execute() {
        customer.startWaiting(eventTime)

        if (!simulation.trader.busy) {
            simulation.startServiceEvent.schedule(customer, eventTime)
        } else {
            simulation.customerQueue.enqueue(customer, eventTime)
        }
        schedule(simulation.getNewCustomer(), eventTime)
    }

    override fun eventDuration(): Double = customerArrivalRandom.next()

    override fun toString() = "ARRIVAL - " + super.toString()

}