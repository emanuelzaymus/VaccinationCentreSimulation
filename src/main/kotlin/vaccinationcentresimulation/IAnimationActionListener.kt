package vaccinationcentresimulation

interface IAnimationActionListener {
    fun handleOnTimeChanged(actualSimulationTime: Double)

    fun updateRegistrationQueueLength(length: Int)
    fun updateExaminationQueueLength(length: Int)
    fun updateVaccinationQueueLength(length: Int)

    fun updateRegistrationRoomBusyWorkersCount(busyWorkers: Int)
    fun updateExaminationRoomBusyDoctorsCount(busyDoctors: Int)
    fun updateVaccinationRoomBusyNursesCount(busyNurses: Int)

    fun updateWaitingRoomPatientsCount(patients: Int)

    fun updateStatistics()
}