package vaccinationcentresimulation.statistics

import simulation.statistics.ContinuousStatistics
import utils.statisticsqueue.IQueueLengthChangedActionListener

class AverageQueueLengthStats : ContinuousStatistics(), IQueueLengthChangedActionListener {

    override fun handleQueueLengthChanged(newLength: Int, newElapsedTime: Double) {
        addSample(newLength, newElapsedTime)
    }

}