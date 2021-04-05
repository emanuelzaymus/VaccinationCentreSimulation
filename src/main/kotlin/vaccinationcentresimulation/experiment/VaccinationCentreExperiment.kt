package vaccinationcentresimulation.experiment

import simulation.statistics.CommonTotalTime
import simulation.statistics.ContinuousStatistics
import simulation.statistics.DiscreteStatistics
import vaccinationcentresimulation.VaccinationCentreSimulation
import vaccinationcentresimulation.statistics.QueueLengthStats
import vaccinationcentresimulation.statistics.WaitingPatientsCountStats
import vaccinationcentresimulation.statistics.WaitingTimeStats
import vaccinationcentresimulation.statistics.WorkloadStats

class VaccinationCentreExperiment(
    replicationsCount: Int, numberOfPatientsPerReplication: Int, numberOfAdminWorkers: Int,
    numberOfDoctors: Int, numberOfNurses: Int, withAnimation: Boolean
) : Experiment(
    replicationsCount, numberOfPatientsPerReplication, numberOfAdminWorkers,
    numberOfDoctors, numberOfNurses, withAnimation
) {

    private var experimentActionListener: IVaccinationCentreExperimentActionListener? = null

    public override var simulation: VaccinationCentreSimulation
        get() = super.simulation
        protected set(value) {
            super.simulation = value
        }

    val commonTotalTime = CommonTotalTime()

    val registrationQueueLength = QueueLengthStats(commonTotalTime)
    val allRegistrationQueueLengths = ContinuousStatistics()
    val examinationQueueLength = QueueLengthStats(commonTotalTime, collectChartData = true)
    val allExaminationQueueLengths = ContinuousStatistics()
    val vaccinationQueueLength = QueueLengthStats(commonTotalTime)
    val allVaccinationQueueLengths = ContinuousStatistics()

    val registrationWaitingTime = WaitingTimeStats()
    val allRegistrationWaitingTimes = ContinuousStatistics()
    val examinationWaitingTime = WaitingTimeStats()
    val allExaminationWaitingTimes = ContinuousStatistics()
    val vaccinationWaitingTime = WaitingTimeStats()
    val allVaccinationWaitingTimes = ContinuousStatistics()

    val adminWorkersPersonalWorkloads = List(numberOfAdminWorkers) { WorkloadStats(commonTotalTime) }
    val allAdminWorkersWorkloads = ContinuousStatistics()
    val doctorsPersonalWorkloads = List(numberOfDoctors) { WorkloadStats(commonTotalTime) }
    val allDoctorsWorkloads = ContinuousStatistics()
    val nursesPersonalWorkloads = List(numberOfNurses) { WorkloadStats(commonTotalTime) }
    val allNursesWorkloads = ContinuousStatistics()

    val waitingPatientsCount = WaitingPatientsCountStats(commonTotalTime)
    val allWaitingPatientsCounts = DiscreteStatistics(calculateConfidenceInterval = true)
    //  TODO: make others DiscreteStatistics as well

    val averageAdminWorkersWorkload: Double get() = adminWorkersPersonalWorkloads.map { it.getAverage() }.average()
    val averageDoctorsWorkload: Double get() = doctorsPersonalWorkloads.map { it.getAverage() }.average()
    val averageNursesWorkload: Double get() = nursesPersonalWorkloads.map { it.getAverage() }.average()

    init {
        with(simulation) {
            registrationQueue.setBeforeQueueLengthChangedActionListener(registrationQueueLength)
            examinationQueue.setBeforeQueueLengthChangedActionListener(examinationQueueLength)
            vaccinationQueue.setBeforeQueueLengthChangedActionListener(vaccinationQueueLength)

            registrationRoom.setOnWaitingStoppedActionListener(registrationWaitingTime)
            examinationRoom.setOnWaitingStoppedActionListener(examinationWaitingTime)
            vaccinationRoom.setOnWaitingStoppedActionListener(vaccinationWaitingTime)

            registrationRoom.setBeforeWorkersStateChangedActionListeners(adminWorkersPersonalWorkloads)
            examinationRoom.setBeforeWorkersStateChangedActionListeners(doctorsPersonalWorkloads)
            vaccinationRoom.setBeforeWorkersStateChangedActionListeners(nursesPersonalWorkloads)

            waitingRoom.setBeforePatientsCountChangedActionListener(waitingPatientsCount)
        }
    }

    override fun onBeforeReplication() = restart()

    override fun onAfterReplication() {
        allRegistrationQueueLengths.addSample(registrationQueueLength.getAverage(), registrationQueueLength.totalTime)
        allExaminationQueueLengths.addSample(examinationQueueLength.getAverage(), examinationQueueLength.totalTime)
        allVaccinationQueueLengths.addSample(vaccinationQueueLength.getAverage(), vaccinationQueueLength.totalTime)

        allRegistrationWaitingTimes.addSample(registrationWaitingTime.getAverage(), registrationWaitingTime.sampleCount)
        allExaminationWaitingTimes.addSample(examinationWaitingTime.getAverage(), examinationWaitingTime.sampleCount)
        allVaccinationWaitingTimes.addSample(vaccinationWaitingTime.getAverage(), vaccinationWaitingTime.sampleCount)

        adminWorkersPersonalWorkloads.forEach { allAdminWorkersWorkloads.addSample(it.getAverage(), it.totalTime) }
        doctorsPersonalWorkloads.forEach { allDoctorsWorkloads.addSample(it.getAverage(), it.totalTime) }
        nursesPersonalWorkloads.forEach { allNursesWorkloads.addSample(it.getAverage(), it.totalTime) }

        allWaitingPatientsCounts.addSample(waitingPatientsCount.getAverage()) //, waitingPatientsCount.totalTime)
    }

    private fun restart() {
        commonTotalTime.restart()

        registrationQueueLength.restart()
        examinationQueueLength.restart()
        vaccinationQueueLength.restart()

        registrationWaitingTime.restart()
        examinationWaitingTime.restart()
        vaccinationWaitingTime.restart()

        adminWorkersPersonalWorkloads.forEach { it.restart() }
        doctorsPersonalWorkloads.forEach { it.restart() }
        nursesPersonalWorkloads.forEach { it.restart() }

        waitingPatientsCount.restart()
    }

    fun setVaccinationCentreExperimentActionListener(listener: IVaccinationCentreExperimentActionListener) {
        experimentActionListener = listener
    }

    override fun updateActualSimulationTime(actualSimulationTime: Double) {
        experimentActionListener?.updateActualSimulationTime(actualSimulationTime)
    }

    override fun updateSimulationState(simulationState: String) {
        experimentActionListener?.updateSimulationState(simulationState)
    }

    override fun updateRegistrationQueueLength(length: Int) {
        experimentActionListener?.updateRegistrationQueueLength(length)
    }

    override fun updateExaminationQueueLength(length: Int) {
        experimentActionListener?.updateExaminationQueueLength(length)
    }

    override fun updateVaccinationQueueLength(length: Int) {
        experimentActionListener?.updateVaccinationQueueLength(length)
    }

    override fun updateRegistrationRoomBusyWorkersCount(busyWorkers: Int) {
        experimentActionListener?.updateRegistrationRoomBusyWorkersCount(busyWorkers)
    }

    override fun updateExaminationRoomBusyDoctorsCount(busyDoctors: Int) {
        experimentActionListener?.updateExaminationRoomBusyDoctorsCount(busyDoctors)
    }

    override fun updateVaccinationRoomBusyNursesCount(busyNurses: Int) {
        experimentActionListener?.updateVaccinationRoomBusyNursesCount(busyNurses)
    }

    override fun updateWaitingRoomPatientsCount(patients: Int) {
        experimentActionListener?.updateWaitingRoomPatientsCount(patients)
    }

    override fun updateStatistics() {
        experimentActionListener?.updateStatistics()
    }

    override fun updateCurrentReplicNumber(currentReplicNumber: Int) {
        experimentActionListener?.updateCurrentReplicNumber(currentReplicNumber)
    }

}