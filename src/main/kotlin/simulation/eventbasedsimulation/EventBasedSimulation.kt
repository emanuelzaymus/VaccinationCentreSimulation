package simulation.eventbasedsimulation

import simulation.montecarlo.MonteCarlo
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

    fun pause() {
        paused.set(true)
        state = SimulationState.PAUSED
    }

    fun restore() {
        paused.set(false)
        state = SimulationState.RUNNING
    }

    fun isPaused(): Boolean = paused.get()

    fun isRunning(): Boolean = state == SimulationState.RUNNING

    override fun stop() {
        super.stop()
        state = SimulationState.STOPPED
    }

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
                afterAnimation()
            }

            while (isPaused() && !isStopped())
                Thread.sleep(300)
        }
    }

    override fun afterSimulation() {
        state = SimulationState.READY
    }

    protected open fun animate() {}

    private fun afterAnimation() {
        // If there is only one event planed in the futureEvents and this event is delayEvent -> simulation has ended
        if (futureEvents.count() == 1 && futureEvents.contains(delayEvent))
            removeDelayEvent()
    }

    private fun checkEventTime(eventTime: Double): Double {
        if (eventTime < actualSimulationTime)
            throw IllegalStateException("You cannot execute past event.")
        return eventTime
    }

    // TODO: Is called SimMin and the parameter is in seconds??????
    fun setDelayEverySimMin(seconds: Int) = delayEvent.setDelayEverySimMin(seconds)

    fun setDelayForMillis(milliseconds: Int) = delayEvent.setDelayForMillis(milliseconds.toLong())

    private fun scheduleDelayEvent() {
        if (!futureEvents.contains(delayEvent))
            delayEvent.schedule(actualSimulationTime)
    }

    private fun removeDelayEvent() = futureEvents.remove(delayEvent)

}