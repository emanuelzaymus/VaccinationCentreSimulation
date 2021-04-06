package simulation.statistics

interface IStatistics {
    /**
     * @return Calculated Average from values
     */
    fun getAverage(): Double

    /**
     * Restarts the statistics.
     */
    fun restart()
}