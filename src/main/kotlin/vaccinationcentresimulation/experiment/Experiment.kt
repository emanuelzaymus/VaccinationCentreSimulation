package vaccinationcentresimulation.experiment

import vaccinationcentresimulation.IAnimationActionListener
import vaccinationcentresimulation.VaccinationCentreSimulation

abstract class Experiment(
    replicationsCount: Int, numberOfPatientsPerReplication: Int, numberOfAdminWorkers: Int,
    numberOfDoctors: Int, numberOfNurses: Int, withAnimation: Boolean
) : IExperimentActionListener, IAnimationActionListener {

    protected open var simulation = VaccinationCentreSimulation(
        replicationsCount,
        numberOfPatientsPerReplication,
        numberOfAdminWorkers,
        numberOfDoctors,
        numberOfNurses,
        withAnimation
    ).also {
        it.setExperimentActionListener(this)
        it.setAnimationActionListener(this)
    }

    open var withAnimation: Boolean
        get() = simulation.withAnimation
        set(value) {
            simulation.withAnimation = value
        }

    fun setDelayEverySimUnits(simulationUnits: Double) = simulation.setDelayEverySimUnits(simulationUnits)

    fun setDelayForMillis(millis: Long) = simulation.setDelayForMillis(millis)

    fun wasStarted() = simulation.wasStarted()

    fun isPaused() = simulation.isPaused()

    fun pause() = simulation.pause()

    fun restore() = simulation.restore()

    open fun start() {
        simulation.simulate()
    }

    open fun stop() = simulation.stop()

    override fun updateActualSimulationTime(actualSimulationTime: Double) {}

    override fun updateSimulationState(simulationState: String) {}

    override fun updateRegistrationQueueLength(length: Int) {}

    override fun updateExaminationQueueLength(length: Int) {}

    override fun updateVaccinationQueueLength(length: Int) {}

    override fun updateRegistrationRoomBusyWorkersCount(busyWorkers: Int) {}

    override fun updateExaminationRoomBusyDoctorsCount(busyDoctors: Int) {}

    override fun updateVaccinationRoomBusyNursesCount(busyNurses: Int) {}

    override fun updateWaitingRoomPatientsCount(patients: Int) {}

    override fun updateStatistics() {}

    override fun updateCurrentReplicNumber(currentReplicNumber: Int) {}

}