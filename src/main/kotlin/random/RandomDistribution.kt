package random

import java.util.*

/**
 * Random distribution generator.
 */
abstract class RandomDistribution {

    protected val random = Random(SeedGenerator.nextSeed())

    protected fun nextDouble() = random.nextDouble()

}