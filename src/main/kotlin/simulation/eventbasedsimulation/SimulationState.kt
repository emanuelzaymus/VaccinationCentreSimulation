package simulation.eventbasedsimulation

/**
 * Represent current state of simulation.
 */
enum class SimulationState {
    READY,
    RUNNING,
    PAUSED,
    STOPPED
}