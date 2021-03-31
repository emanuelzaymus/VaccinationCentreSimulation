package random

import kotlin.math.ln

/**
 * Random exponential distribution generator with specific parameter lambda.
 */
class ExponentialDistribution(private val lambda: Double) : RandomDistribution(), IContinuousDistribution {

    // https://en.wikipedia.org/wiki/Exponential_distribution
    override fun next(): Double = -ln(1 - nextDouble()) / lambda

}