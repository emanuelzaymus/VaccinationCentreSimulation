package vaccinationcentresimulation.statistics

import simulation.statistics.CommonCumulativeStatistics
import simulation.statistics.CommonTotalTime
import vaccinationcentresimulation.events.IBeforeWorkersStateChangedActionListener

class WorkloadStats(commonTotalTime: CommonTotalTime) : CommonCumulativeStatistics(commonTotalTime),
    IBeforeWorkersStateChangedActionListener {

    override fun handleBeforeWorkersStateChanged(
        oldWorkingState: Boolean, timeElapsed: Double, commonTotalTime: Double
    ) {
        addSample(if (oldWorkingState) 1 else 0, timeElapsed, commonTotalTime)
    }

}