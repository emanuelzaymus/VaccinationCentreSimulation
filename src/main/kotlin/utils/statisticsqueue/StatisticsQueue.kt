package utils.statisticsqueue

import utils.IReusable
import java.util.*

class StatisticsQueue<T> : IReusable {

    private val queue: Queue<T> = LinkedList()
    private var beforeQueueLengthChangedActionListener: IBeforeQueueLengthChangedActionListener? = null
    private var lastChange: Double = .0

    fun enqueue(element: T, eventTime: Double, commonTotalTime: Double) {
        beforeQueueLengthChanged(eventTime, commonTotalTime)
        queue.add(element)
    }

    fun dequeue(eventTime: Double, commonTotalTime: Double): T {
        beforeQueueLengthChanged(eventTime, commonTotalTime)
        return queue.remove()
    }

    fun isEmpty(): Boolean = queue.isEmpty()

    fun count(): Int = queue.count()

    override fun restart() {
        lastChange = .0
    }

    override fun checkFinalState() {
        if (!queue.isEmpty()) throw IllegalStateException("Queue is not empty.")
    }

    fun setBeforeQueueLengthChangedActionListener(listener: IBeforeQueueLengthChangedActionListener) {
        beforeQueueLengthChangedActionListener = listener
    }

    private fun beforeQueueLengthChanged(eventTime: Double, commonTotalTime: Double) {
        beforeQueueLengthChangedActionListener?.handleBeforeQueueLengthChanged(
            queue.size, eventTime - lastChange, commonTotalTime
        )
        lastChange = eventTime
    }

}