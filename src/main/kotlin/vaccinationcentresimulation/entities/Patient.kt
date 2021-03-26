package vaccinationcentresimulation.entities

import utils.pool.IPooledObject
import utils.stopwatch.Stopwatch

class Patient : IPooledObject {

    private val waitingStopwatch = Stopwatch()

    fun startWaiting(actualSimulationTime: Double) = waitingStopwatch.start(actualSimulationTime)

    fun stopWaiting(actualSimulationTime: Double) = waitingStopwatch.stop(actualSimulationTime)

    fun getWaitingTime(): Double = waitingStopwatch.getElapsedTime()

    override fun restart() = waitingStopwatch.restart()

}