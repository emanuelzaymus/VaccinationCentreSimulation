package random

/**
 * Continuous random uniform distribution generator that generates numbers from (inclusive) to until (exclusive).
 */
class ContinuousUniformDistribution(private val from: Double = .0, private val until: Double = 1.0) :
    RandomDistribution(), IContinuousDistribution {

    init {
        if (from >= until)
            throw IllegalArgumentException("Parameter from must be smaller than until.")
    }

    override fun next(): Double = random.nextDouble(from, until)

}