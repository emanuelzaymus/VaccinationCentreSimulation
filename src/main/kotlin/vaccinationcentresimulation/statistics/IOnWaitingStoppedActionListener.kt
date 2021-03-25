package vaccinationcentresimulation.statistics

interface IOnWaitingStoppedActionListener {

    companion object {
        fun getEmptyImplementation(): IOnWaitingStoppedActionListener = EmptyOnWaitingStoppedActionListener()
    }

    fun handleOnWaitingStopped(newWaitingElapsed: Double)

    class EmptyOnWaitingStoppedActionListener : IOnWaitingStoppedActionListener {
        override fun handleOnWaitingStopped(newWaitingElapsed: Double) {}
    }

}