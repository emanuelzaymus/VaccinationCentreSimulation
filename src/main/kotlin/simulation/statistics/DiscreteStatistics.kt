package simulation.statistics

open class DiscreteStatistics(calculateConfidenceInterval: Boolean = false) : Statistics(calculateConfidenceInterval) {

    private var average: Double = .0
    var sampleCount: Int = 0
        private set

    fun addSample(newValue: Double) {
        average = (average * sampleCount + newValue) / ++sampleCount

        addToConfidenceIntervalStatistics(newValue)
    }

    override fun getAverage(): Double = average

    override fun restart() {
        super.restart()
        average = .0
        sampleCount = 0
    }

}