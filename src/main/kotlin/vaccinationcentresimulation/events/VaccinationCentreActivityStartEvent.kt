package vaccinationcentresimulation.events

import vaccinationcentresimulation.VaccinationCentreSimulation
import vaccinationcentresimulation.entities.VaccinationCentreWorker

abstract class VaccinationCentreActivityStartEvent(
    simulation: VaccinationCentreSimulation,
    private val worker: VaccinationCentreWorker
) :
    VaccinationCentreEvent(simulation) {

    private var waitingStoppedActionListener = IOnWaitingStoppedActionListener.getEmptyImplementation()
    protected abstract val toStringTitle: String

    override fun execute() {
        worker.busy = true
        stopPatientWaiting()
        worker.scheduleEnd(patient, eventTime)
    }

    fun setOnWaitingStoppedActionListener(listener: IOnWaitingStoppedActionListener) {
        waitingStoppedActionListener = listener
    }

    private fun stopPatientWaiting() {
        patient.stopWaiting(eventTime)
        waitingStoppedActionListener.handleOnWaitingStopped(patient.getWaitingTime())
    }

    override fun toString() = "$toStringTitle - ${super.toString()}"

}