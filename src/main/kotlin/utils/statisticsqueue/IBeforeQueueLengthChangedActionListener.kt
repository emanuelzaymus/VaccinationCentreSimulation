package utils.statisticsqueue

interface IBeforeQueueLengthChangedActionListener {
    fun handleBeforeQueueLengthChanged(newLength: Int, newElapsedTime: Double)
}