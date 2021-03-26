package utils.statisticsqueue

// TODO: rename IBeforeQueueLengthChangedActionListener
interface IQueueLengthChangedActionListener {

    companion object {
        fun getEmptyImplementation(): IQueueLengthChangedActionListener = EmptyQueueLengthChangedActionListener()
    }

    fun handleQueueLengthChanged(newLength: Int, newElapsedTime: Double)

    class EmptyQueueLengthChangedActionListener : IQueueLengthChangedActionListener {
        override fun handleQueueLengthChanged(newLength: Int, newElapsedTime: Double) {}
    }

}