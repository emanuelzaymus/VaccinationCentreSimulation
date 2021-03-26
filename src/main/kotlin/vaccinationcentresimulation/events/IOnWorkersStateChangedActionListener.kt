package vaccinationcentresimulation.events

// TODO: rename IBeforeWorkersStateChangedActionListener
interface IOnWorkersStateChangedActionListener {

    companion object {
        fun getEmptyImplementation(): IOnWorkersStateChangedActionListener = EmptyOnWorkersStateChangedActionListener()
    }

    fun handleOnWorkersStateChanged(oldWorkingState: Boolean, timeElapsed: Double)

    class EmptyOnWorkersStateChangedActionListener : IOnWorkersStateChangedActionListener {
        override fun handleOnWorkersStateChanged(oldWorkingState: Boolean, timeElapsed: Double) {}
    }
}