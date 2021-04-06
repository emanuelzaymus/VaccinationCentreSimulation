package simulation.statistics

import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * Abstract statistics which can collect data for calculation 95% confidence interval when calculateConfidenceInterval == true.
 */
abstract class Statistics(private val calculateConfidenceInterval: Boolean) : IStatistics {

    companion object {
        private const val T_ALPHA_95: Double = 1.96
    }

    private var sum: Double = .0
    private var sumSquared: Double = .0
    private var numberOfSamples: Double = .0

    override fun restart() {
        sum = .0
        sumSquared = .0
        numberOfSamples = .0
    }

    /** Adds parameter value into statistics for calculation 95% confidence interval. */
    protected fun addToConfidenceIntervalStatistics(value: Double) {
        if (calculateConfidenceInterval) {
            sum += value
            sumSquared += value.pow(2)
            numberOfSamples++
        }
    }

    /** @return Calculated lower bound of 95% confidence interval */
    fun lowerBoundOfConfidenceInterval(): Double {
        if (!calculateConfidenceInterval)
            throw Exception("Calculation of confidence interval was not set in the constructor.")

        if (numberOfSamples < 30) {
            return Double.NaN
        }
        return getAverage() - calculateHalfInterval()
    }

    /** @return Calculated upper bound of 95% confidence interval */
    fun upperBoundOfConfidenceInterval(): Double {
        if (!calculateConfidenceInterval)
            throw Exception("Calculation of confidence interval was not set in the constructor.")

        if (numberOfSamples < 30) {
            return Double.NaN
        }
        return getAverage() + calculateHalfInterval()
    }

    private fun calculateHalfInterval(): Double =
        (calculateStandardDeviation() * T_ALPHA_95) / sqrt(numberOfSamples)

    private fun calculateStandardDeviation(): Double = sqrt(
        abs((1 / (numberOfSamples - 1) * sumSquared) - (1 / (numberOfSamples - 1) * sum).pow(2))
    )

}