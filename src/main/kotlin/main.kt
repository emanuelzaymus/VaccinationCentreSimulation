import newsstandsimulation.NewsStandSimulation
import newsstandsimulation.statistics.queuelength.AverageQueueLengthStats
import newsstandsimulation.statistics.waitingtime.AverageWaitingTimeStats

fun main() {

//    RandomDistributionsWriter.writeAll()

    val simulation = NewsStandSimulation(100, 1_000_000.0)

    val avgWaitingTimeStats = AverageWaitingTimeStats()
    simulation.startServiceEvent.setOnStartServiceActionListener(avgWaitingTimeStats)

    val avgQueueLengthStats = AverageQueueLengthStats()
    simulation.customerQueue.setQueueLengthChangedActionListener(avgQueueLengthStats)

    simulation.simulate()

    println("avg. waiting time: ${avgWaitingTimeStats.getAverage()}")
    println("avg. queue length: ${avgQueueLengthStats.getAverage()}")

}