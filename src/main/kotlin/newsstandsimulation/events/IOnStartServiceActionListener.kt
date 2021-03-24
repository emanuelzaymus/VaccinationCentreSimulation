package newsstandsimulation.events

interface IOnStartServiceActionListener {

    companion object {
        fun getEmptyImplementation(): IOnStartServiceActionListener = EmptyOnStartServiceActionListener()
    }

    fun handleOnStartService(actionSource: IOnStartServiceActionSource)

    class EmptyOnStartServiceActionListener : IOnStartServiceActionListener {
        override fun handleOnStartService(actionSource: IOnStartServiceActionSource) {}
    }

}