package vaccinationcentresimulation.experiment

import simulation.statistics.CommonTotalTime
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
    val allRegistrationQueueLengths = DiscreteStatistics()
    val examinationQueueLength = QueueLengthStats(commonTotalTime, collectChartData = true)
    val allExaminationQueueLengths = DiscreteStatistics()
    val vaccinationQueueLength = QueueLengthStats(commonTotalTime)
    val allVaccinationQueueLengths = DiscreteStatistics()

    val registrationWaitingTime = WaitingTimeStats()
    val allRegistrationWaitingTimes = DiscreteStatistics()
    val examinationWaitingTime = WaitingTimeStats()
    val allExaminationWaitingTimes = DiscreteStatistics()
    val vaccinationWaitingTime = WaitingTimeStats()
    val allVaccinationWaitingTimes = DiscreteStatistics()

    val adminWorkersPersonalWorkloads = List(numberOfAdminWorkers) { WorkloadStats(commonTotalTime) }
    val allAdminWorkersWorkloads = DiscreteStatistics()
    val doctorsPersonalWorkloads = List(numberOfDoctors) { WorkloadStats(commonTotalTime) }
    val allDoctorsWorkloads = DiscreteStatistics()
    val nursesPersonalWorkloads = List(numberOfNurses) { WorkloadStats(commonTotalTime) }
    val allNursesWorkloads = DiscreteStatistics()

    val waitingPatientsCount = WaitingPatientsCountStats(commonTotalTime)
    val allWaitingPatientsCounts = DiscreteStatistics(calculateConfidenceInterval = true)

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
        allRegistrationQueueLengths.addSample(registrationQueueLength.getAverage())
        allExaminationQueueLengths.addSample(examinationQueueLength.getAverage())
        allVaccinationQueueLengths.addSample(vaccinationQueueLength.getAverage())

        allRegistrationWaitingTimes.addSample(registrationWaitingTime.getAverage())
        allExaminationWaitingTimes.addSample(examinationWaitingTime.getAverage())
        allVaccinationWaitingTimes.addSample(vaccinationWaitingTime.getAverage())

        adminWorkersPersonalWorkloads.forEach { allAdminWorkersWorkloads.addSample(it.getAverage()) }
        doctorsPersonalWorkloads.forEach { allDoctorsWorkloads.addSample(it.getAverage()) }
        nursesPersonalWorkloads.forEach { allNursesWorkloads.addSample(it.getAverage()) }

        allWaitingPatientsCounts.addSample(waitingPatientsCount.getAverage())
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