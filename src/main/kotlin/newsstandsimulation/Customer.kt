package newsstandsimulation

import utils.pool.IPooledObject

class Customer : IPooledObject {

    private var beginningOfWaiting: Double = -1.0
    private var endOfWaiting: Double = -1.0

    fun startWaiting(actualSimulationTime: Double) {
        beginningOfWaiting = actualSimulationTime
    }

    fun stopWaiting(actualSimulationTime: Double) {
        endOfWaiting = actualSimulationTime
    }

    fun getWaitingTime(): Double {
        if (beginningOfWaiting < 0)
            throw IllegalStateException("Waiting was not started yet.")
        if (endOfWaiting < 0)
            throw IllegalStateException("Waiting was not stopped yet.")

        return endOfWaiting - beginningOfWaiting
    }

    override fun restart() {
        beginningOfWaiting = -1.0
        endOfWaiting = -1.0
    }

}