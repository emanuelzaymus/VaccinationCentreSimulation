import vaccinationcentresimulation.VaccinationCentreSimulation
import vaccinationcentresimulation.statistics.AverageQueueLengthStats
import vaccinationcentresimulation.statistics.AverageWaitingPatientsCountStats
import vaccinationcentresimulation.statistics.AverageWaitingTimeStats
import vaccinationcentresimulation.statistics.AverageWorkloadStats
import kotlin.system.measureTimeMillis

fun main() {

//    RandomDistributionsWriter.writeAll()

    val elapsed = measureTimeMillis {

        val replicationsCount = 1
        println("Replications: $replicationsCount")

        val simulation = VaccinationCentreSimulation(replicationsCount, 60)

        val avgBeforeRegistrationQueueLen = AverageQueueLengthStats()
        simulation.beforeRegistrationQueue.setBeforeQueueLengthChangedActionListener(avgBeforeRegistrationQueueLen)
        val avgBeforeExaminationQueueLen = AverageQueueLengthStats()
        simulation.beforeExaminationQueue.setBeforeQueueLengthChangedActionListener(avgBeforeExaminationQueueLen)
        val avgBeforeVaccinationQueueLen = AverageQueueLengthStats()
        simulation.beforeVaccinationQueue.setBeforeQueueLengthChangedActionListener(avgBeforeVaccinationQueueLen)

        val avgWaitingTimeRegistrationQueue = AverageWaitingTimeStats()
        simulation.registrationRoom.setOnWaitingStoppedActionListener(avgWaitingTimeRegistrationQueue)
        val avgWaitingTimeExaminationQueue = AverageWaitingTimeStats()
        simulation.examinationRoom.setOnWaitingStoppedActionListener(avgWaitingTimeExaminationQueue)
        val avgWaitingTimeVaccinationQueue = AverageWaitingTimeStats()
        simulation.vaccinationRoom.setOnWaitingStoppedActionListener(avgWaitingTimeVaccinationQueue)

        val averageWorkloadAdministrativeWorkerStats = AverageWorkloadStats()
        simulation.registrationRoom.setBeforeWorkersStateChangedActionListener(averageWorkloadAdministrativeWorkerStats)
        val averageWorkloadDoctorStats = AverageWorkloadStats()
        simulation.examinationRoom.setBeforeWorkersStateChangedActionListener(averageWorkloadDoctorStats)
        val averageWorkloadNurseStats = AverageWorkloadStats()
        simulation.vaccinationRoom.setBeforeWorkersStateChangedActionListener(averageWorkloadNurseStats)

        val averageWaitingPatientsCountStats = AverageWaitingPatientsCountStats()
        simulation.waitingRoom.setBeforePatientsCountChangedActionListener(averageWaitingPatientsCountStats)

        simulation.simulate()

        println()
        println("avg. registration queue length: ${avgBeforeRegistrationQueueLen.getAverage()}")
        println("avg. examination queue length: ${avgBeforeExaminationQueueLen.getAverage()}")
        println("avg. vaccination queue length: ${avgBeforeVaccinationQueueLen.getAverage()}")
        println()
        println("avg. waiting time registration queue: ${avgWaitingTimeRegistrationQueue.getAverage()}")
        println("avg. waiting time examination queue: ${avgWaitingTimeExaminationQueue.getAverage()}")
        println("avg. waiting time vaccination queue: ${avgWaitingTimeVaccinationQueue.getAverage()}")
        println()
        println("avg. workload administrative workers: ${averageWorkloadAdministrativeWorkerStats.getAverage()}")
        println("avg. workload doctors: ${averageWorkloadDoctorStats.getAverage()}")
        println("avg. workload nurses: ${averageWorkloadNurseStats.getAverage()}")
        println()
        println("avg. waiting patients count: ${averageWaitingPatientsCountStats.getAverage()}")

    }
    println("\nElapsed: $elapsed ms")

}