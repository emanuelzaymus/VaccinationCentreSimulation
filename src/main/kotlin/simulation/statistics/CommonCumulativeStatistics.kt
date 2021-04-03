package simulation.statistics

open class CommonCumulativeStatistics(private val commonTotalTime: CommonTotalTime) : IStatistics {

    private var sum: Double = .0

    val totalTime: Double get() = commonTotalTime.totalTime

    fun addSample(newValue: Int, elapsedTime: Double, updatedCommonTotalTime: Double) {
        if (elapsedTime >= 0) {
            sum += newValue * elapsedTime
            commonTotalTime.totalTime = updatedCommonTotalTime
        } else if (elapsedTime < 0)
            throw IllegalArgumentException("Elapsed time cannot be negative.")
    }

    override fun getAverage(): Double {
        if (totalTime == .0) {
            return .0
        }
        return sum / totalTime
    }

    override fun restart() {
        sum = .0
    }

}