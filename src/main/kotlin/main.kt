import vaccinationcentresimulation.VaccinationCentreSimulation
import vaccinationcentresimulation.statistics.AverageQueueLengthStats
import vaccinationcentresimulation.statistics.AverageWaitingPatientsCountStats
import vaccinationcentresimulation.statistics.AverageWaitingTimeStats
import vaccinationcentresimulation.statistics.AverageWorkloadStats

fun main() {

//    RandomDistributionsWriter.writeAll()


//    val simulation = NewsStandSimulation(100, 1_000_000.0)
//
//    val avgWaitingTimeStats = AverageWaitingTimeStats()
//    simulation.startServiceEvent.setOnStartServiceActionListener(avgWaitingTimeStats)
//
//    val avgQueueLengthStats = AverageQueueLengthStats()
//    simulation.customerQueue.setQueueLengthChangedActionListener(avgQueueLengthStats)
//
//    simulation.simulate()
//
//    println("avg. waiting time: ${avgWaitingTimeStats.getAverage()}")
//    println("avg. queue length: ${avgQueueLengthStats.getAverage()}")

    val simulation = VaccinationCentreSimulation(1, 60)

    val avgBeforeRegistrationQueueLen = AverageQueueLengthStats()
    simulation.beforeRegistrationQueue.setQueueLengthChangedActionListener(avgBeforeRegistrationQueueLen)
    val avgBeforeExaminationQueueLen = AverageQueueLengthStats()
    simulation.beforeExaminationQueue.setQueueLengthChangedActionListener(avgBeforeExaminationQueueLen)
    val avgBeforeVaccinationQueueLen = AverageQueueLengthStats()
    simulation.beforeVaccinationQueue.setQueueLengthChangedActionListener(avgBeforeVaccinationQueueLen)

    val avgWaitingTimeRegistrationQueue = AverageWaitingTimeStats()
    simulation.registrationRoom.setOnWaitingStoppedActionListener(avgWaitingTimeRegistrationQueue)
    val avgWaitingTimeExaminationQueue = AverageWaitingTimeStats()
    simulation.examinationRoom.setOnWaitingStoppedActionListener(avgWaitingTimeExaminationQueue)
    val avgWaitingTimeVaccinationQueue = AverageWaitingTimeStats()
    simulation.vaccinationRoom.setOnWaitingStoppedActionListener(avgWaitingTimeVaccinationQueue)

    val averageWorkloadAdministrativeWorkerStats = AverageWorkloadStats()
    simulation.registrationRoom.setOnWorkersStateChangedActionListener(averageWorkloadAdministrativeWorkerStats)
    val averageWorkloadDoctorStats = AverageWorkloadStats()
    simulation.examinationRoom.setOnWorkersStateChangedActionListener(averageWorkloadDoctorStats)
    val averageWorkloadNurseStats = AverageWorkloadStats()
    simulation.vaccinationRoom.setOnWorkersStateChangedActionListener(averageWorkloadNurseStats)

    val averageWaitingPatientsCountStats = AverageWaitingPatientsCountStats()
    simulation.waitingRoom.setBeforeWaitingPatientsCountChangedActionListener(averageWaitingPatientsCountStats)

    simulation.simulate()

    println()
    println("avg. before registration queue length: ${avgBeforeRegistrationQueueLen.getAverage()}")
    println("avg. before examination queue length: ${avgBeforeExaminationQueueLen.getAverage()}")
    println("avg. before vaccination queue length: ${avgBeforeVaccinationQueueLen.getAverage()}")
    println()
    println("avg. waiting time registration queue length: ${avgWaitingTimeRegistrationQueue.getAverage()}")
    println("avg. waiting time examination queue length: ${avgWaitingTimeExaminationQueue.getAverage()}")
    println("avg. waiting time vaccination queue length: ${avgWaitingTimeVaccinationQueue.getAverage()}")
    println()
    println("avg. workload administrative workers: ${averageWorkloadAdministrativeWorkerStats.getAverage()}")
    println("avg. workload doctors: ${averageWorkloadDoctorStats.getAverage()}")
    println("avg. workload nurses: ${averageWorkloadNurseStats.getAverage()}")
    println()
    println("avg. waiting patients count: ${averageWaitingPatientsCountStats.getAverage()}")

}