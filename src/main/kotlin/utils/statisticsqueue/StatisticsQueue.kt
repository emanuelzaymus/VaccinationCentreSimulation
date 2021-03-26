package utils.statisticsqueue

import java.util.*

class StatisticsQueue<T> {

    private val queue: Queue<T> = LinkedList()
    private var beforeQueueLengthChangedActionListener: IBeforeQueueLengthChangedActionListener? = null
    private var lastChange: Double = .0

    fun enqueue(element: T, eventTime: Double) {
        beforeQueueLengthChanged(eventTime)
        queue.add(element)
    }

    fun dequeue(eventTime: Double): T {
        beforeQueueLengthChanged(eventTime)
        return queue.remove()
    }

    fun isEmpty(): Boolean = queue.isEmpty()

    fun clear() {
        queue.clear()
        lastChange = .0
    }

    fun setBeforeQueueLengthChangedActionListener(listener: IBeforeQueueLengthChangedActionListener) {
        beforeQueueLengthChangedActionListener = listener
    }

    private fun beforeQueueLengthChanged(eventTime: Double) {
        beforeQueueLengthChangedActionListener?.handleBeforeQueueLengthChanged(queue.size, eventTime - lastChange)
        lastChange = eventTime
    }

}