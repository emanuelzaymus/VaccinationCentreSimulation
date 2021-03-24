import random.ExponentialDistribution
import random.RandomDistribution
import random.TriangularDistribution
import java.io.BufferedWriter
import java.io.File

object RandomDistributionsWriter {

    fun writeAll() {
        exponentialDistribution()
        triangularDistribution()
    }

    fun exponentialDistribution() {
        writeDistribution(ExponentialDistribution(1/4.0), 10_000, "expoDistLambda0.25.txt")
    }

    fun triangularDistribution() {
        writeDistribution(TriangularDistribution(20.0, 75.0, 100.0), 10_000, "triangularDist_20_75_100.txt")
    }

    private fun writeDistribution(distribution: RandomDistribution, valueCount: Int, fileName: String) {
        val writer: BufferedWriter = File(fileName).bufferedWriter()

        for (i in 1..valueCount)
            writer.appendLine(distribution.next().toString())

        writer.flush()
    }

}