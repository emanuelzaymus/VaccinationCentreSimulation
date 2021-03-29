package vaccinationcentresimulation.entities

import utils.pool.IPooledObject
import utils.stopwatch.Stopwatch

class Patient : IPooledObject {

    companion object {
        private var nextId = 1

        fun restartPatientIds() {
            nextId = 1
        }
    }

    private var id = nextId++
    private val waitingStopwatch = Stopwatch()

    fun startWaiting(actualSimulationTime: Double) = waitingStopwatch.start(actualSimulationTime)

    fun stopWaiting(actualSimulationTime: Double) = waitingStopwatch.stop(actualSimulationTime)

    fun getWaitingTime(): Double = waitingStopwatch.getElapsedTime()

    override fun restart() {
        id = nextId++
        waitingStopwatch.restart()
    }

    override fun toString() = id.toString()

}