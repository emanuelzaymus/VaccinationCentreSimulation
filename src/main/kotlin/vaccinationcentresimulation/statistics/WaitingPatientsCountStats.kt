package vaccinationcentresimulation.statistics

import simulation.statistics.CommonCumulativeStatistics
import simulation.statistics.CommonTotalTime
import vaccinationcentresimulation.entities.waiting.IBeforePatientsCountChangedActionListener

class WaitingPatientsCountStats(commonTotalTime: CommonTotalTime) : CommonCumulativeStatistics(commonTotalTime),
    IBeforePatientsCountChangedActionListener {

    override fun handleBeforePatientsCountChanged(lastCount: Int, timeElapsed: Double, commonTotalTime: Double) {
        addSample(lastCount, timeElapsed, commonTotalTime)
    }

}