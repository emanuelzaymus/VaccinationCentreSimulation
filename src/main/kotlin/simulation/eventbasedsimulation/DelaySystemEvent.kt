package simulation.eventbasedsimulation

/**
 * System event for simulation delaying.
 */
internal class DelaySystemEvent(simulation: EventBasedSimulation) : Event(simulation) {

    /** How long should the simulation sleep. */
    var delayForMillis: Long = 1000
        @Synchronized get
        @Synchronized set(value) {
            if (value < 0) throw IllegalArgumentException("Attribute delayForMillis cannot be negative.")
            field = value
        }

    /** How often should the simulation schedule this event. */
    var delayEverySimUnits: Double = 1.0
        @Synchronized get
        @Synchronized set(value) {
            if (value < 0) throw IllegalArgumentException("Attribute delayEverySimUnits cannot be negative.")
            field = value
        }

    /** Sleeps the thread for delayForMillis milliseconds and schedules itself again if simulation.withAnimation == ture. */
    override fun execute() {
        Thread.sleep(delayForMillis)

        if (simulation.withAnimation)
            schedule(eventTime + delayEverySimUnits)
    }

    // Exposes this method to everyone - makes it public
    public override fun schedule(eventTime: Double) = super.schedule(eventTime)

    override fun toString(): String = "DELAY for $delayForMillis ms - ${super.toString()}"

}