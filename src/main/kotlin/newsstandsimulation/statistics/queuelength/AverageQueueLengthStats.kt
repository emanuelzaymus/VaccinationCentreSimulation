package newsstandsimulation.statistics.queuelength

import simulation.statistics.ContinuousStatistics

class AverageQueueLengthStats : ContinuousStatistics(), IQueueLengthChangedActionListener {

    override fun handleQueueLengthChanged(newLength: Int, newElapsedTime: Double) {
        addSample(newLength, newElapsedTime)
    }

}