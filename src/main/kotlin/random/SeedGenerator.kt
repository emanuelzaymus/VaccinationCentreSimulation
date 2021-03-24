package random

import java.util.*

/**
 * Pseudo-random seed generator.
 */
object SeedGenerator {

    private val seedGenerator = Random()
    private var lastSeed: Long = 0

    /**
     * returns next random seed for initialization of new random generator. Will not return two same seeds after each other.
     */
    fun nextSeed(): Long {
        while (true) {
            val nextSeed = seedGenerator.nextLong()
            if (nextSeed != lastSeed) {
                lastSeed = nextSeed
                return nextSeed
            }
        }
    }

    /**
     * Sets seed for SeedGenerator.
     */
    fun setSeed(seed: Long) = seedGenerator.setSeed(seed)

}