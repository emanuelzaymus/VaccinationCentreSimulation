package utils.stopwatch

class Stopwatch {

    private var beginning: Double = -1.0
    private var end: Double = -1.0

    fun start(time: Double) {
        if (beginning >= 0)
            throw IllegalStateException("The stopwatch has to be restarted before starting.")

        beginning = time
    }

    fun stop(time: Double) {
        if (end >= 0)
            throw IllegalStateException("The stopwatch has to be restarted before stopping.")
        if (beginning < 0)
            throw IllegalStateException("The stopwatch has to be started first.")

        end = time
    }

    fun getElapsedTime(): Double {
        if (beginning < 0) throw IllegalStateException("The stopwatch was not started yet.")
        if (end < 0) throw IllegalStateException("The stopwatch was not stopped yet.")
        if (end < beginning) throw IllegalStateException("Beginning is less than end.")

        val ret = end - beginning

        restart()
        return ret
    }

    fun restart() {
        beginning = -1.0
        end = -1.0
    }

}