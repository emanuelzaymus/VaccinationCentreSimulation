package utils.stopwatch

class Stopwatch {

    private var beginning: Double = .0
    private var end: Double = .0

    fun start(time: Double) {
        beginning = time
    }

    fun stop(time: Double) {
        end = time
    }

    fun getElapsedTime(): Double {
        if (end < beginning)
            throw IllegalStateException("The stopwatch was not stopped yet.")

        return end - beginning
    }

    fun restart() {
        beginning = .0
        end = .0
    }

}