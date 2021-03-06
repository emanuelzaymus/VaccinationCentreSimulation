package random.discrete

interface IDiscreteDistribution {
    /**
     * Returns next random sample from this discrete random distribution generator.
     */
    fun next(): Int
}