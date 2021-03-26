package vaccinationcentresimulation.statistics

import simulation.statistics.ContinuousStatistics
import vaccinationcentresimulation.events.IBeforeWorkersStateChangedActionListener

class AverageWorkloadStats : ContinuousStatistics(), IBeforeWorkersStateChangedActionListener {

    override fun handleBeforeWorkersStateChanged(oldWorkingState: Boolean, timeElapsed: Double) {
        addSample(if (oldWorkingState) 1 else 0, timeElapsed)
    }

}