package simulation.statistics

import kotlin.IllegalArgumentException

open class ContinuousStatistics : IStatistics {

    private var average: Double = .0
    private var totalWeight: Double = .0

    fun addSample(value: Double, weight: Double) {
        if (weight > 0) {
            average = (average * totalWeight + value * weight) / (totalWeight + weight)
            totalWeight += weight
        } else if (weight < 0)
            throw IllegalArgumentException("Elapsed time cannot be negative.")
    }

    fun addSample(value: Double, weight: Int) = addSample(value, weight.toDouble())

    override fun getAverage(): Double = average

    override fun restart() {
        average = .0
        totalWeight = .0
    }

}