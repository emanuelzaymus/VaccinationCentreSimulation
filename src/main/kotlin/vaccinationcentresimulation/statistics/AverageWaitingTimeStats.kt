package vaccinationcentresimulation.statistics

import simulation.statistics.DiscreteStatistics
import vaccinationcentresimulation.events.IOnWaitingStoppedActionListener

class AverageWaitingTimeStats : DiscreteStatistics(), IOnWaitingStoppedActionListener {

    override fun handleOnWaitingStopped(newWaitingElapsed: Double) {
        if (newWaitingElapsed >= 0)
            addSample(newWaitingElapsed)
        else
            throw IllegalArgumentException("Elapsed waiting time cannot be negative.")
    }

}