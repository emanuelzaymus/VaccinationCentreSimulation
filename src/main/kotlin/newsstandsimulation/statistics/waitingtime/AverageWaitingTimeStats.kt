package newsstandsimulation.statistics.waitingtime

import newsstandsimulation.events.IOnStartServiceActionListener
import newsstandsimulation.events.IOnStartServiceActionSource
import simulation.statistics.DiscreteStatistics

class AverageWaitingTimeStats : DiscreteStatistics(), IOnStartServiceActionListener {

    override fun handleOnStartService(actionSource: IOnStartServiceActionSource) {
        addSample(actionSource.customer().getWaitingTime())
    }

}