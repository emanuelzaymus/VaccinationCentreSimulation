package vaccinationcentresimulation

import vaccinationcentresimulation.statistics.QueueLengthStats
import vaccinationcentresimulation.statistics.WaitingPatientsCountStats
import vaccinationcentresimulation.statistics.WaitingTimeStats
import vaccinationcentresimulation.statistics.WorkloadStats

class VaccinationCentreExperiment(
    replicationsCount: Int,
    numberOfPatientsPerReplication: Int,
    numberOfAdminWorkers: Int,
    numberOfDoctors: Int,
    numberOfNurses: Int,
    withAnimation: Boolean
) {

    val simulation = VaccinationCentreSimulation(
        replicationsCount,
        numberOfPatientsPerReplication,
        numberOfAdminWorkers,
        numberOfDoctors,
        numberOfNurses,
        withAnimation
    )

    val registrationQueueLength = QueueLengthStats()
    val examinationQueueLength = QueueLengthStats()
    val vaccinationQueueLength = QueueLengthStats()

    val registrationWaitingTime = WaitingTimeStats()
    val examinationWaitingTime = WaitingTimeStats()
    val vaccinationWaitingTime = WaitingTimeStats()

    val adminWorkersWorkload = WorkloadStats()
    val doctorsWorkload = WorkloadStats()
    val nursesWorkload = WorkloadStats()

    var adminWorkersPersonalWorkload = List(numberOfAdminWorkers) { WorkloadStats() }
        private set
    var doctorsPersonalWorkload = List(numberOfDoctors) { WorkloadStats() }
        private set
    var nursesPersonalWorkload = List(numberOfNurses) { WorkloadStats() }
        private set

    val waitingPatientsCount = WaitingPatientsCountStats()

    init {
        with(simulation) {
            registrationQueue.setBeforeQueueLengthChangedActionListener(registrationQueueLength)
            examinationQueue.setBeforeQueueLengthChangedActionListener(examinationQueueLength)
            vaccinationQueue.setBeforeQueueLengthChangedActionListener(vaccinationQueueLength)

            registrationRoom.setOnWaitingStoppedActionListener(registrationWaitingTime)
            examinationRoom.setOnWaitingStoppedActionListener(examinationWaitingTime)
            vaccinationRoom.setOnWaitingStoppedActionListener(vaccinationWaitingTime)

            registrationRoom.setBeforeEachWorkersStateChangedActionListener(adminWorkersWorkload)
            examinationRoom.setBeforeEachWorkersStateChangedActionListener(doctorsWorkload)
            vaccinationRoom.setBeforeEachWorkersStateChangedActionListener(nursesWorkload)

            registrationRoom.setBeforeWorkersStateChangedActionListeners(adminWorkersPersonalWorkload)
            examinationRoom.setBeforeWorkersStateChangedActionListeners(doctorsPersonalWorkload)
            vaccinationRoom.setBeforeWorkersStateChangedActionListeners(nursesPersonalWorkload)

            waitingRoom.setBeforePatientsCountChangedActionListener(waitingPatientsCount)
        }
    }

}