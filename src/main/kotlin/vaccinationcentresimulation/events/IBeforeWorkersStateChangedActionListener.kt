package vaccinationcentresimulation.events

interface IBeforeWorkersStateChangedActionListener {
    fun handleBeforeWorkersStateChanged(oldWorkingState: Boolean, timeElapsed: Double, commonTotalTime: Double)
}