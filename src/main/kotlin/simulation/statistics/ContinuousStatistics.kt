package simulation.statistics

open class ContinuousStatistics : IStatistics {

    private var average: Double = .0
    private var totalTime: Double = .0

    fun addSample(newValue: Int, newElapsedTime: Double) {
        average =
            (average * totalTime + newValue * newElapsedTime) / (totalTime + newElapsedTime)
        totalTime += newElapsedTime
    }

    override fun getAverage(): Double = average

    override fun restart() {
        average = .0
        totalTime = .0
    }

}