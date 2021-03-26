package vaccinationcentresimulation.events

interface IOnWaitingStoppedActionListener {
    fun handleOnWaitingStopped(newWaitingElapsed: Double)
}