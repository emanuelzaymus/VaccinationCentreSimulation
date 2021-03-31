package vaccinationcentresimulation.events

import simulation.eventbasedsimulation.Event
import utils.secToMin
import vaccinationcentresimulation.VaccinationCentreSimulation

// TODO: Put into abstract EventBasedSimulation class
class DelayEvent(private val simulation: VaccinationCentreSimulation) : Event() {

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

        if (simulation.isWithAnimation()) {
            schedule()
        }
    }

    fun schedule(eventTime: Double) {
        if (!simulation.containsEvent(this)) {
            this.eventTime = eventTime
            simulation.scheduleEvent(this)
        }
    }

    private fun schedule() = schedule(eventTime + delayEverySimMin)

    override fun toString(): String = "DELAY for $delayForMillis ms - ${super.toString()}"

}