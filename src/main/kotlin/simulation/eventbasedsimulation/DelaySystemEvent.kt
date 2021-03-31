package simulation.eventbasedsimulation

internal class DelaySystemEvent(simulation: EventBasedSimulation) : Event(simulation) {

    var delayForMillis: Long = 1000
        @Synchronized get
        @Synchronized set(value) {
            if (value < 0) throw IllegalArgumentException("Attribute delayForMillis cannot be negative.")
            field = value
        }

    var delayEverySimMin: Double = 1.0
        @Synchronized get
        @Synchronized set(value) {
            if (value < 0) throw IllegalArgumentException("Attribute delayEverySimMin cannot be negative.")
            field = value
        }

    override fun execute() {
        Thread.sleep(delayForMillis)

        if (simulation.withAnimation)
            schedule(eventTime + delayEverySimMin)
    }

    public override fun schedule(eventTime: Double) = super.schedule(eventTime)

    override fun toString(): String = "DELAY for $delayForMillis ms - ${super.toString()}"

}