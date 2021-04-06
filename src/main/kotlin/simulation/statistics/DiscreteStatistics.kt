package simulation.statistics

/**
 * Discrete statistics which does not take any weight for values.
 */
open class DiscreteStatistics(calculateConfidenceInterval: Boolean = false) : Statistics(calculateConfidenceInterval) {

    private var average: Double = .0
    var sampleCount: Int = 0
        private set

    /** Adds sample to the statistics. */
    fun addSample(newValue: Double) {
        average = (average * sampleCount + newValue) / ++sampleCount

        addToConfidenceIntervalStatistics(newValue)
    }

    override fun getAverage(): Double = average

    override fun restart() {
        super.restart()
        average = .0
        sampleCount = 0
    }

}