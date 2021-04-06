package simulation.eventbasedsimulation

import simulation.montecarlo.MonteCarlo
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Abstract event-based simulation core.
 */
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

    /** Actual simulation time in simulation units. */
    var actualSimulationTime: Double = .0
        private set

    /** Current simulation state. */
    var state = SimulationState.READY
        @Synchronized get
        @Synchronized private set

    /** Whether animation is turned on or off. */
    var withAnimation: Boolean
        @Synchronized get() = animated.get()
        @Synchronized set(value) {
            animated.set(value)
            if (value) scheduleDelayEvent() else removeDelayEvent()
        }

    /** Pause the simulation. */
    open fun pause() {
        paused.set(true)
        state = SimulationState.PAUSED
    }

    /** Restore simulation run after pause. */
    fun restore() {
        paused.set(false)
        state = SimulationState.RUNNING
    }

    /** Current simulation state. */
    fun isPaused(): Boolean = paused.get()

    /** Whether the simulation was started - the current state is RUNNING or PAUSED. */
    fun wasStarted(): Boolean = state == SimulationState.RUNNING || state == SimulationState.PAUSED

    /** Plan next event for execution. */
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

    /** Method for GUI animation. */
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

    /** Sets property indicating how often should DelaySystemEvent be scheduled. */
    fun setDelayEverySimUnits(simulationUnits: Double) {
        delayEvent.delayEverySimUnits = simulationUnits
    }

    /** Sets property indicating how long should DelaySystemEvent sleep. */
    fun setDelayForMillis(millis: Long) {
        delayEvent.delayForMillis = millis
    }

    private fun scheduleDelayEvent() {
        if (!futureEvents.contains(delayEvent))
            delayEvent.schedule(actualSimulationTime)
    }

    private fun removeDelayEvent() = futureEvents.remove(delayEvent)

}