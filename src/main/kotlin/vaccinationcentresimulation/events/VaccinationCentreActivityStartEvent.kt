package vaccinationcentresimulation.events

import vaccinationcentresimulation.VaccinationCentreSimulation
import vaccinationcentresimulation.entities.VaccinationCentreWorker

abstract class VaccinationCentreActivityStartEvent(
    simulation: VaccinationCentreSimulation,
    private val worker: VaccinationCentreWorker
) :
    VaccinationCentreEvent(simulation) {

    private var waitingStoppedActionListener: IOnWaitingStoppedActionListener? = null

    override fun execute() {
        worker.setBusy(true, eventTime, simulation.actualSimulationTime)
        stopPatientWaiting()
        worker.scheduleEnd(patient, eventTime)
    }

    fun setOnWaitingStoppedActionListener(listener: IOnWaitingStoppedActionListener) {
        waitingStoppedActionListener = listener
    }

    private fun stopPatientWaiting() {
        patient.stopWaiting(eventTime)
        waitingStoppedActionListener?.handleOnWaitingStopped(patient.getWaitingTime())
        patient.restartStopwatch()
    }

}