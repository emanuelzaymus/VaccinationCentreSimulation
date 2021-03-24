package newsstandsimulation.events

import newsstandsimulation.Customer
import newsstandsimulation.NewsStandSimulation

class StartServiceEvent(simulation: NewsStandSimulation) : NewsStandEvent(simulation), IOnStartServiceActionSource {

    private var onStartServiceActionListener = IOnStartServiceActionListener.getEmptyImplementation()

    override fun execute() {
        customer.stopWaiting(eventTime)
        onStartServiceActionListener.handleOnStartService(this)

        simulation.trader.busy = true
        simulation.endServiceEvent.schedule(customer, eventTime)
    }

    override fun eventDuration(): Double = .0

    override fun setOnStartServiceActionListener(listener: IOnStartServiceActionListener) {
        onStartServiceActionListener = listener
    }

    override fun customer(): Customer = customer

    override fun toString() = "START_S - " + super.toString()

}