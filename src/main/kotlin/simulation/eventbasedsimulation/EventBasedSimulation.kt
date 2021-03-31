package simulation.eventbasedsimulation

import simulation.montecarlo.MonteCarlo
import utils.secToMin
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

abstract class EventBasedSimulation(
    replicationsCount: Int,
    private val maxSimulationTime: Double = Double.MAX_VALUE,
    withAnimation: Boolean
) :
    MonteCarlo(replicationsCount) {

    private val paused = AtomicBoolean(false)
    private val animated = AtomicBoolean(withAnimation)
    private val delayEvent by lazy { DelaySystemEvent(this) }

    private val futureEvents: PriorityQueue<Event> = PriorityQueue()
    protected var actualSimulationTime: Double = .0
        private set

    var state = SimulationState.READY
        @Synchronized get
        @Synchronized private set

    var withAnimation: Boolean
        @Synchronized get() = animated.get()
        @Synchronized set(value) {
            animated.set(value)
            if (value) scheduleDelayEvent() else removeDelayEvent()
        }

    open fun pause() {
        paused.set(true)
        state = SimulationState.PAUSED
    }

    fun restore() {
        paused.set(false)
        state = SimulationState.RUNNING
    }

    fun isPaused(): Boolean = paused.get()

    fun isRunning(): Boolean = state == SimulationState.RUNNING

    fun scheduleEvent(event: Event) {
        if (event.eventTime < actualSimulationTime)
            throw IllegalArgumentException("Cannot plan past event.")
        if (futureEvents.contains(event))
            throw IllegalArgumentException("Event is already scheduled.")

        futureEvents.add(event)
    }

    override fun beforeSimulation() {
        state = SimulationState.RUNNING
    }

    override fun beforeReplication() {
        futureEvents.clear()
        actualSimulationTime = .0

        if (withAnimation)
            scheduleDelayEvent()
    }

    override fun doReplication() {
        while (actualSimulationTime <= maxSimulationTime && !isStopped() && futureEvents.isNotEmpty()) {

            val currentEvent = futureEvents.remove()
            actualSimulationTime = checkEventTime(currentEvent.eventTime)
            currentEvent.execute()

            if (withAnimation) {
                println(currentEvent)
                animate()
                checkUnnecessaryDelayEvent()
            }

            while (isPaused() && !isStopped())
                Thread.sleep(300)
        }
    }

    override fun afterSimulation() {
        state = if (isStopped()) SimulationState.STOPPED else SimulationState.READY
    }

    protected open fun animate() {}

    private fun checkUnnecessaryDelayEvent() {
        // If there is only one event planed in the futureEvents and this event is delayEvent -> simulation has ended
        if (futureEvents.count() == 1 && futureEvents.contains(delayEvent))
            removeDelayEvent()
    }

    private fun checkEventTime(eventTime: Double): Double {
        if (eventTime < actualSimulationTime)
            throw IllegalStateException("You cannot execute past event.")
        return eventTime
    }

    fun setDelayEverySimSeconds(seconds: Int) {
        delayEvent.delayEverySimMin = secToMin(seconds)
    }

    fun setDelayForMillis(millis: Long) {
        delayEvent.delayForMillis = millis
    }

    private fun scheduleDelayEvent() {
        if (!futureEvents.contains(delayEvent))
            delayEvent.schedule(actualSimulationTime)
    }

    private fun removeDelayEvent() = futureEvents.remove(delayEvent)

}