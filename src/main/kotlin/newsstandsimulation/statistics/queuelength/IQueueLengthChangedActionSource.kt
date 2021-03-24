package newsstandsimulation.statistics.queuelength

interface IQueueLengthChangedActionSource {
    fun setQueueLengthChangedActionListener(listener: IQueueLengthChangedActionListener)
}