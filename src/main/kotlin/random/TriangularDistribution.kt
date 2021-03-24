package random

import kotlin.math.sqrt

/**
 * Random triangular distribution generator with specific parameter minimum, maximum and mode.
 */
class TriangularDistribution(private val min: Double, private val mode: Double, private val max: Double) :
    RandomDistribution(), IContinuousDistribution {

    private val fMode: Double
    private val moreThanFModeCoefficient: Double
    private val lessThanFModeCoefficient: Double

    init {
        if (min >= mode)
            throw IllegalArgumentException("Parameter min cannot be greater nor equal than mode.")

        if (mode >= max)
            throw  IllegalArgumentException("Parameter max must be greater than mode.")

        fMode = (mode - min) / (max - min)
        moreThanFModeCoefficient = (max - min) * (mode - min)
        lessThanFModeCoefficient = (max - min) * (max - mode)
    }

    override fun next(): Double {
        // https://en.wikipedia.org/wiki/Triangular_distribution
        val random = nextDouble()
        return if (random < fMode)
            min + sqrt(random * moreThanFModeCoefficient)
        else
            max - sqrt((1 - random) * lessThanFModeCoefficient)
    }

}