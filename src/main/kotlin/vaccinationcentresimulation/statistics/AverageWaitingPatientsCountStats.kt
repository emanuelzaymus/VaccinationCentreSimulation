package vaccinationcentresimulation.statistics

import simulation.statistics.ContinuousStatistics
import vaccinationcentresimulation.entities.waiting.IBeforePatientsCountChangedActionListener

class AverageWaitingPatientsCountStats : ContinuousStatistics(), IBeforePatientsCountChangedActionListener {

    override fun handleBeforePatientsCountChanged(lastCount: Int, timeElapsed: Double) {
        addSample(lastCount, timeElapsed)
    }

}