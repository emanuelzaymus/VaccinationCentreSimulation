package newsstandsimulation.events

interface IOnStartServiceActionListener {
    fun handleOnStartService(actionSource: IOnStartServiceActionSource)
}