package random

import kotlin.random.Random

/**
 * Random distribution generator.
 */
abstract class RandomDistribution {

    protected val random = Random(SeedGenerator.nextSeed())

    protected fun nextDouble() = random.nextDouble()

}