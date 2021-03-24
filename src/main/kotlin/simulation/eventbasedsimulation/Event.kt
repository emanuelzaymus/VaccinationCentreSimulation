package simulation.eventbasedsimulation

abstract class Event : Comparable<Event> {

    var eventTime: Double = -1.0
        protected set

    abstract fun execute()

    fun restart() {
        eventTime = -1.0
    }

    override fun compareTo(other: Event): Int {
        return eventTime.compareTo(other.eventTime)
    }

    override fun toString(): String = "EventTime: $eventTime"

}
