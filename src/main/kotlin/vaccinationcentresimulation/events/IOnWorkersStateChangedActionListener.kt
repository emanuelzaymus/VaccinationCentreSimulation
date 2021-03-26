package vaccinationcentresimulation.events

// TODO: rename IBeforeWorkersStateChangedActionListener
interface IOnWorkersStateChangedActionListener {
    fun handleOnWorkersStateChanged(oldWorkingState: Boolean, timeElapsed: Double)
}