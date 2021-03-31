package random

import java.io.BufferedWriter
import java.io.File

object RandomDistributionsWriter {

    fun writeAll() {
        exponentialDistribution()
        triangularDistribution()
    }

    private fun exponentialDistribution() {
        writeDistribution(
            ExponentialDistribution(1 / 260.0), 1_000_000, "input_analyzer/expoDistLambda0.25.txt"
        )
    }

    private fun triangularDistribution() {
        writeDistribution(
            TriangularDistribution(20.0, 75.0, 100.0),
            1_000_000,
            "input_analyzer/triangularDist_20_75_100.txt"
        )
    }

    private fun writeDistribution(distribution: IContinuousDistribution, valueCount: Int, fileName: String) {
        val writer: BufferedWriter = File(fileName).bufferedWriter()

        for (i in 1..valueCount)
            writer.appendln(distribution.next().toString())

        writer.flush()

        println("done")
    }

}