package newsstandsimulation.events

import newsstandsimulation.Customer
import newsstandsimulation.NewsStandSimulation
import simulation.eventbasedsimulation.Event

abstract class NewsStandEvent(protected val simulation: NewsStandSimulation) : Event() {
    protected lateinit var customer: Customer

    fun schedule(customer: Customer, lastEventTime: Double) {
        this.customer = customer
        eventTime = lastEventTime + eventDuration()
        simulation.scheduleEvent(this)
    }

    abstract fun eventDuration(): Double

    override fun toString() = "Customer: $customer, ${super.toString()}"

}