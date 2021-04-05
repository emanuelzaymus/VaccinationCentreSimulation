package vaccinationcentresimulation.statistics

import simulation.statistics.CommonCumulativeStatistics
import simulation.statistics.CommonTotalTime
import utils.statisticsqueue.IBeforeQueueLengthChangedActionListener

class QueueLengthStats(commonTotalTime: CommonTotalTime, private val collectChartData: Boolean = false) :
    CommonCumulativeStatistics(commonTotalTime), IBeforeQueueLengthChangedActionListener {

    val chartData = mutableListOf<Pair<Double, Int>>()
    var wasRestarted: Boolean = true
        private set

    override fun handleBeforeQueueLengthChanged(newLength: Int, newElapsedTime: Double, commonTotalTime: Double) {
        addSample(newLength, newElapsedTime, commonTotalTime)

        if (collectChartData) {
            chartData.add(Pair(commonTotalTime, newLength))
        }
        wasRestarted = false
    }

    override fun restart() {
        super.restart()
        chartData.clear()
        wasRestarted = true
    }

}