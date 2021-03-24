package utils

import newsstandsimulation.statistics.queuelength.IQueueLengthChangedActionListener
import newsstandsimulation.statistics.queuelength.IQueueLengthChangedActionSource
import java.util.*

class StatisticsQueue<T> : IQueueLengthChangedActionSource {

    private val queue: Queue<T> = LinkedList()
    private var queueLengthChangedActionListener = IQueueLengthChangedActionListener.getEmptyImplementation()
    private var lastChange: Double = .0

    fun enqueue(element: T, eventTime: Double) {
        queueLengthChanged(eventTime)
        queue.add(element)
    }

    fun dequeue(eventTime: Double): T {
        queueLengthChanged(eventTime)
        return queue.remove()
    }

    fun isEmpty(): Boolean = queue.isEmpty()

    fun clear() {
        queue.clear()
        lastChange = .0
    }

    override fun setQueueLengthChangedActionListener(listener: IQueueLengthChangedActionListener) {
        queueLengthChangedActionListener = listener
    }

    private fun queueLengthChanged(eventTime: Double) {
        queueLengthChangedActionListener.handleQueueLengthChanged(queue.size, eventTime - lastChange)
        lastChange = eventTime
    }

}