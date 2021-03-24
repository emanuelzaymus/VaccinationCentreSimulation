package random

interface IContinuousDistribution {
    /**
     * Returns next random sample from this continuous random distribution generator.
     */
    fun next(): Double
}