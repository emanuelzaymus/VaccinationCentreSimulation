package random.discrete

import random.RandomDistribution

/**
 * Discrete random uniform distribution generator that generates numbers from (inclusive) to until (exclusive).
 */
class DiscreteUniformDistribution(private val from: Int = 0, private val until: Int) :
    RandomDistribution(), IDiscreteDistribution {

    private val range = until - from

    init {
        if (from >= until)
            throw IllegalArgumentException("Parameter from must be smaller than until.")
    }

    override fun next(): Int = random.nextInt(range) + from

}