package vaccinationcentresimulation.experiment.doctorexperiment

import simulation.statistics.CommonTotalTime
import simulation.statistics.ContinuousStatistics
import vaccinationcentresimulation.VaccinationCentreSimulation
import vaccinationcentresimulation.experiment.Experiment
import vaccinationcentresimulation.statistics.QueueLengthStats
import java.util.concurrent.atomic.AtomicBoolean

class DoctorsExperiment(
    private val replicationsCountPerExperiment: Int,
    private val numberOfPatientsPerReplication: Int,
    private val numberOfAdminWorkers: Int,
    private val fromDoctors: Int,
    private val toDoctors: Int,
    private val numberOfNurses: Int
) : Experiment(
    replicationsCountPerExperiment, numberOfPatientsPerReplication, numberOfAdminWorkers,
    fromDoctors, numberOfNurses, withAnimation = false
) {

    private var doctorExperimentActionListener: IDoctorExperimentActionListener? = null
    private val isStopped = AtomicBoolean(false)

    private val commonTotalTime = CommonTotalTime()
    private val examinationQueueLength = QueueLengthStats(commonTotalTime, collectChartData = true)
    private val allExaminationQueueLengths = ContinuousStatistics()
    val overallDoctorsExamQueueChartData = mutableListOf<Pair<Int, Double>>()

    override fun start() {
        isStopped.set(false)

        for (doctors in fromDoctors..toDoctors) {

            simulation = VaccinationCentreSimulation(
                replicationsCountPerExperiment,
                numberOfPatientsPerReplication,
                numberOfAdminWorkers,
                doctors,
                numberOfNurses,
                withAnimation = false
            ).also {
                it.setExperimentActionListener(this)
                it.setAnimationActionListener(this)
                it.examinationQueue.setBeforeQueueLengthChangedActionListener(examinationQueueLength)
            }
            super.start()

            overallDoctorsExamQueueChartData.add(Pair(doctors, allExaminationQueueLengths.getAverage()))
            doctorExperimentActionListener?.updateStatistics()

            if (isStopped.get())
                break
        }
    }

    override var withAnimation: Boolean
        get() = false
        set(_) = println("Cannot play animation while executing DoctorsExperiment.")

    override fun stop() {
        isStopped.set(true)
        super.stop()
    }

    override fun onBeforeReplication() = restart()

    override fun onAfterReplication() {
        allExaminationQueueLengths.addSample(examinationQueueLength.getAverage(), examinationQueueLength.totalTime)
    }

    private fun restart() {
        commonTotalTime.restart()
        examinationQueueLength.restart()
    }

    fun setDoctorExperimentActionListener(listener: IDoctorExperimentActionListener) {
        doctorExperimentActionListener = listener
    }

    override fun updateSimulationState(simulationState: String) {
        doctorExperimentActionListener?.updateSimulationState(simulationState)
    }

    override fun updateCurrentReplicNumber(currentReplicNumber: Int) {
        doctorExperimentActionListener?.updateCurrentReplicNumber(currentReplicNumber)
    }

}