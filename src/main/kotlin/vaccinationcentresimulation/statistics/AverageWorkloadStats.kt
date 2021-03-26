package vaccinationcentresimulation.statistics

import simulation.statistics.ContinuousStatistics
import vaccinationcentresimulation.events.IOnWorkersStateChangedActionListener

class AverageWorkloadStats : ContinuousStatistics(), IOnWorkersStateChangedActionListener {

    override fun handleOnWorkersStateChanged(oldWorkingState: Boolean, timeElapsed: Double) {
        addSample(if (oldWorkingState) 1 else 0, timeElapsed)
    }

}