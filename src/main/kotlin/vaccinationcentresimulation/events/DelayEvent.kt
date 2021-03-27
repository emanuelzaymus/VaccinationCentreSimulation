package vaccinationcentresimulation.events

import simulation.eventbasedsimulation.Event
import vaccinationcentresimulation.VaccinationCentreSimulation

class DelayEvent(private val simulation: VaccinationCentreSimulation) : Event() {

    companion object {
        private const val delayEverySimMin: Double = 1.0
        private const val delayForMillis: Long = 2000
    }

    override fun execute() {
        Thread.sleep(delayForMillis)
        schedule()
    }

    fun schedule(eventTime: Double) {
        this.eventTime = eventTime
        simulation.scheduleEvent(this)
    }

    private fun schedule() = schedule(eventTime + delayEverySimMin)

    override fun toString(): String = "DELAY for $delayForMillis ms - ${super.toString()}"

}