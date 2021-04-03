package simulation.statistics

open class ContinuousStatistics(calculateConfidenceInterval: Boolean = false) :
    Statistics(calculateConfidenceInterval) {

    private var average: Double = .0
    private var totalWeight: Double = .0

    fun addSample(value: Double, weight: Double) {
        if (weight > 0) {
            average = (average * totalWeight + value * weight) / (totalWeight + weight)
            totalWeight += weight
        } else if (weight < 0)
            throw IllegalArgumentException("Elapsed time cannot be negative.")

        addToConfidenceIntervalStatistics(value * weight)
    }

    fun addSample(value: Double, weight: Int) = addSample(value, weight.toDouble())

    override fun getAverage(): Double = average

    override fun restart() {
        super.restart()
        average = .0
        totalWeight = .0
    }

}