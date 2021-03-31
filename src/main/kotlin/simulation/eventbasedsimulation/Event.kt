package simulation.eventbasedsimulation

abstract class Event(protected open val simulation: EventBasedSimulation) : Comparable<Event> {

    var eventTime: Double = -1.0
        private set

    protected open fun schedule(eventTime: Double) {
        this.eventTime = eventTime
        simulation.scheduleEvent(this)
    }

    abstract fun execute()

    fun restart() {
        eventTime = -1.0
    }

    override fun compareTo(other: Event): Int {
        return eventTime.compareTo(other.eventTime)
    }

    override fun toString(): String = "EventTime: $eventTime"

}
