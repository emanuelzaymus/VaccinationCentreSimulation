package vaccinationcentresimulation.statistics

import simulation.statistics.DiscreteStatistics

class AverageWaitingTimeStats : DiscreteStatistics(), IOnWaitingStoppedActionListener {

    override fun handleOnWaitingStopped(newWaitingElapsed: Double) {
        addSample(newWaitingElapsed)
    }

}