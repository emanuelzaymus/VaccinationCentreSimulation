package simulation.statistics

open class DiscreteStatistics : IStatistics {

    private var average: Double = .0
    private var sampleCount: Int = 0

    fun addSample(newValue: Double) {
        average = (average * sampleCount + newValue) / ++sampleCount
    }

    override fun getAverage(): Double = average

    override fun restart() {
        average = .0
        sampleCount = 0
    }

}