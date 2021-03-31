package simulation.eventbasedsimulation

import utils.secToMin

internal class DelaySystemEvent(simulation: EventBasedSimulation) : Event(simulation) {

    private var delayEverySimMin: Double = 1.0
    private var delayForMillis: Long = 1000

    @Synchronized
    fun setDelayEverySimMin(seconds: Int) {
        delayEverySimMin = secToMin(seconds)
    }

    @Synchronized
    fun setDelayForMillis(milliseconds: Long) {
        delayForMillis = milliseconds
    }

    override fun execute() {
        Thread.sleep(delayForMillis)

        if (simulation.withAnimation) {
            schedule()
        }
    }

    public override fun schedule(eventTime: Double) = super.schedule(eventTime)

    private fun schedule() = schedule(eventTime + delayEverySimMin)

    override fun toString(): String = "DELAY for $delayForMillis ms - ${super.toString()}"

}