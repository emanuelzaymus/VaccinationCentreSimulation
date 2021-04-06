package random

import random.continuous.ExponentialDistribution
import random.continuous.IContinuousDistribution
import random.continuous.TriangularDistribution
import vaccinationcentresimulation.constants.EXAMINATION_EVENT_DURATION_LAMBDA
import vaccinationcentresimulation.constants.VACCINATION_EVENT_DURATION_MAX
import vaccinationcentresimulation.constants.VACCINATION_EVENT_DURATION_MIN
import vaccinationcentresimulation.constants.VACCINATION_EVENT_DURATION_MODE
import java.io.BufferedWriter
import java.io.File

/**
 * Writes test samples from random number generators.
 */
object RandomDistributionsWriter {

    /**
     * Writes test samples from exponential and triangular random distributions.
     */
    fun writeAll() {
        exponentialDistribution()
        triangularDistribution()
    }

    private fun exponentialDistribution() {
        writeDistribution(
            ExponentialDistribution(EXAMINATION_EVENT_DURATION_LAMBDA),
            fileName = "input_analyzer/expoDistLambda1over260.txt"
        )
    }

    private fun triangularDistribution() {
        writeDistribution(
            TriangularDistribution(
                VACCINATION_EVENT_DURATION_MIN,
                VACCINATION_EVENT_DURATION_MODE,
                VACCINATION_EVENT_DURATION_MAX
            ), fileName = "input_analyzer/triangularDist_20_75_100.txt"
        )
    }

    private fun writeDistribution(
        distribution: IContinuousDistribution, valueCount: Int = 1_000_000, fileName: String
    ) {
        val writer: BufferedWriter = File(fileName).bufferedWriter()

        for (i in 1..valueCount)
            writer.appendln(distribution.next().toString())

        writer.flush()

        println("done")
    }

}