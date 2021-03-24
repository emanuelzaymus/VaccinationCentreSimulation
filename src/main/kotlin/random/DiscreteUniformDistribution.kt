package random

/**
 * Discrete random uniform distribution generator that generates numbers from (inclusive) to until (exclusive).
 */
class DiscreteUniformDistribution(private val from: Int, private val until: Int) :
    RandomDistribution(), IDiscreteDistribution {

    init {
        if (from >= until)
            throw IllegalArgumentException("Parameter from must be smaller than until.")
    }

    override fun next(): Int = random.nextInt(from, until)

}