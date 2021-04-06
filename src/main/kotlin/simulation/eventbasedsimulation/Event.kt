package simulation.eventbasedsimulation

/**
 * Abstract event of Event-based simulation core.
 */
abstract class Event(protected open val simulation: EventBasedSimulation) : Comparable<Event> {

    /** Time when this event should be executed. */
    var eventTime: Double = -1.0
        private set

    /**
     * Schedule this event at eventTime. Calls this.simulation.scheduleEvent(this).
     * @param eventTime When to schedule this event (in simulation units)
     */
    protected open fun schedule(eventTime: Double) {
        this.eventTime = eventTime
        simulation.scheduleEvent(this)
    }

    /** Event execution. */
    abstract fun execute()

    /** Restarts the event to default state. */
    fun restart() {
        eventTime = -1.0
    }

    override fun compareTo(other: Event): Int {
        return eventTime.compareTo(other.eventTime)
    }

    override fun toString(): String = "EventTime: $eventTime"

}
