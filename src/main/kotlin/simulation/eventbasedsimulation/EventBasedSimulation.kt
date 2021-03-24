package simulation.eventbasedsimulation

import simulation.montecarlo.MonteCarlo
import java.util.*

abstract class EventBasedSimulation(replicationsCount: Int, val maxSimulationTime: Double) :
    MonteCarlo(replicationsCount) {

    private val futureEvents: PriorityQueue<Event> = PriorityQueue()
    protected var actualSimulationTime: Double = .0
        private set

    fun scheduleEvent(event: Event) {
        if (event.eventTime < actualSimulationTime)
            throw IllegalArgumentException("Cannot plan past event.")
        if (futureEvents.contains(event))
            throw IllegalArgumentException("Event is already scheduled.")

        futureEvents.add(event)
    }

    override fun beforeReplication() {
        futureEvents.clear()
        actualSimulationTime = .0
    }

    override fun doReplication() {
        while (actualSimulationTime <= maxSimulationTime && !isStopped() && futureEvents.isNotEmpty()) {
            val currentEvent = futureEvents.remove()
            actualSimulationTime = checkEventTime(currentEvent.eventTime)
//            println(currentEvent)
            currentEvent.execute()
        }
    }

    private fun checkEventTime(eventTime: Double): Double {
        if (eventTime < actualSimulationTime)
            throw IllegalStateException("You cannot execute past event.")
        return eventTime
    }

}