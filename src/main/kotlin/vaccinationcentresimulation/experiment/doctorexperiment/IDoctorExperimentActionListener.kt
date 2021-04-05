package vaccinationcentresimulation.experiment.doctorexperiment

interface IDoctorExperimentActionListener {
    fun updateSimulationState(simulationState: String)
    fun updateCurrentReplicNumber(currentReplicNumber: Int)
    fun updateStatistics()
}