package vaccinationcentresimulation

interface IAnimationActionListener {
    fun updateActualSimulationTime(actualSimulationTime: Double)
    fun updateSimulationState(simulationState: String)

    fun updateRegistrationQueueLength(length: Int)
    fun updateExaminationQueueLength(length: Int)
    fun updateVaccinationQueueLength(length: Int)

    fun updateRegistrationRoomBusyWorkersCount(busyWorkers: Int)
    fun updateExaminationRoomBusyDoctorsCount(busyDoctors: Int)
    fun updateVaccinationRoomBusyNursesCount(busyNurses: Int)

    fun updateWaitingRoomPatientsCount(patients: Int)

    fun updateStatistics()
}