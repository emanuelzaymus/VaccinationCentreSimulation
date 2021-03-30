package simulation.eventbasedsimulation

import simulation.montecarlo.MonteCarlo
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

abstract class EventBasedSimulation(
    replicationsCount: Int,
    private val maxSimulationTime: Double = Double.MAX_VALUE,
    withAnimation: Boolean = true
) :
    MonteCarlo(replicationsCount) {

    private val paused = AtomicBoolean(false)
    private val withAnimation = AtomicBoolean(withAnimation)

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
            currentEvent.execute()

            if (isWithAnimation()) {
                println(currentEvent)
                animate()
                afterAnimation()
            }

            while (isPaused() && !isStopped()) {
                Thread.sleep(300)
            }
        }
    }

    protected abstract fun animate()

    protected open fun afterAnimation() {}

    private fun checkEventTime(eventTime: Double): Double {
        if (eventTime < actualSimulationTime)
            throw IllegalStateException("You cannot execute past event.")
        return eventTime
    }

    fun pauseSimulation() {
        paused.set(true)
        animate()
    }

    fun restoreSimulation() = paused.set(false)

    fun isPaused(): Boolean = paused.get()

    protected open fun startAnimation() = withAnimation.set(true)

    protected open fun stopAnimation() = withAnimation.set(false)

    fun isWithAnimation(): Boolean = withAnimation.get()

    fun containsEvent(event: Event) = futureEvents.contains(event)

    protected fun removeEvent(event: Event) = futureEvents.remove(event)

    protected fun eventsCount() = futureEvents.count()

}