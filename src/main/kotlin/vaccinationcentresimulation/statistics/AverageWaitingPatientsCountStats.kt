package vaccinationcentresimulation.statistics

import simulation.statistics.ContinuousStatistics
import vaccinationcentresimulation.entities.waiting.IBeforeWaitingPatientCountChangedActionListener

class AverageWaitingPatientsCountStats : ContinuousStatistics(), IBeforeWaitingPatientCountChangedActionListener {

    override fun handleBeforeWaitingPatientsCountChanged(lastCount: Int, timeElapsed: Double) {
        addSample(lastCount, timeElapsed)
    }

}