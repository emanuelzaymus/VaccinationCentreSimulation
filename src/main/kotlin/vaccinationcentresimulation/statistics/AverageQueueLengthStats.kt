package vaccinationcentresimulation.statistics

import simulation.statistics.ContinuousStatistics
import utils.statisticsqueue.IBeforeQueueLengthChangedActionListener

class AverageQueueLengthStats : ContinuousStatistics(), IBeforeQueueLengthChangedActionListener {

    override fun handleBeforeQueueLengthChanged(newLength: Int, newElapsedTime: Double) {
        addSample(newLength, newElapsedTime)
    }

}