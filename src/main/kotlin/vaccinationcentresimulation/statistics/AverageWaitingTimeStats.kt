package vaccinationcentresimulation.statistics

import simulation.statistics.DiscreteStatistics
import vaccinationcentresimulation.events.IOnWaitingStoppedActionListener

class AverageWaitingTimeStats : DiscreteStatistics(), IOnWaitingStoppedActionListener {

    override fun handleOnWaitingStopped(newWaitingElapsed: Double) {
        addSample(newWaitingElapsed)
    }

}