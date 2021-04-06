package simulation.statistics

/**
 * Represents common total time for statistics CommonCumulativeStatistics.
 */
class CommonTotalTime {
    /** Common total time. */
    var totalTime: Double = .0

    /** Restarts totalTime. */
    fun restart() {
        totalTime = .0
    }
}