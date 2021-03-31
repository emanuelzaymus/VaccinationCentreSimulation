package simulation.montecarlo

/**
 * Monte Carlo simulation base class.
 * @param replicationsCount Number of replication to execute the simulation
 */
abstract class MonteCarlo(private val replicationsCount: Int) {

    protected var currentReplicNumber = 0
        private set

    private var stopped = false

    /**
     * @return Whether the simulation is stopped
     */
    @Synchronized
    fun isStopped(): Boolean = stopped

    /**
     * Stops execution of the simulation.
     */
    @Synchronized
    open fun stop() {
        stopped = true
    }

    /**
     * Starts execution of the simulation.
     */
    fun simulate() {
        beforeSimulation()
        for (i in 0 until replicationsCount) {
            currentReplicNumber = i
            beforeReplication()
            doReplication()
            afterReplication()
            if (isStopped())
                break
        }
        afterSimulation()
    }

    /**
     * Is called only once before execution of the simulation.
     */
    protected open fun beforeSimulation() {}

    /**
     * Is Called before every single replication - before doReplication() method is called. Is called replicationsCount times.
     */
    protected open fun beforeReplication() {}

    /**
     * Atomic replication of the simulation. Is called replicationsCount times.
     */
    protected abstract fun doReplication()

    /**
     * Is Called after every replication - after doReplication() method is called. Is called replicationsCount times.
     */
    protected open fun afterReplication() {}

    /**
     * Is called only once after execution of the simulation.
     */
    protected open fun afterSimulation() {}

}