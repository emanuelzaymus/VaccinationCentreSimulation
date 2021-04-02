package vaccinationcentresimulation.statistics

import simulation.statistics.CommonCumulativeStatistics
import simulation.statistics.CommonTotalTime
import utils.statisticsqueue.IBeforeQueueLengthChangedActionListener

class QueueLengthStats(commonTotalTime: CommonTotalTime) : CommonCumulativeStatistics(commonTotalTime),
    IBeforeQueueLengthChangedActionListener {

    override fun handleBeforeQueueLengthChanged(newLength: Int, newElapsedTime: Double, commonTotalTime: Double) {
        addSample(newLength, newElapsedTime, commonTotalTime)
    }

}