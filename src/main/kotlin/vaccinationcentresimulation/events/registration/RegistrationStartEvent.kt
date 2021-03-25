package vaccinationcentresimulation.events.registration

import vaccinationcentresimulation.VaccinationCentreSimulation
import vaccinationcentresimulation.events.VaccinationCentreEvent
import vaccinationcentresimulation.entities.registration.AdministrativeWorker
import vaccinationcentresimulation.statistics.IOnWaitingStoppedActionListener

class RegistrationStartEvent(simulation: VaccinationCentreSimulation, private val worker: AdministrativeWorker) :
    VaccinationCentreEvent(simulation) {

    private var waitingStoppedActionListener = IOnWaitingStoppedActionListener.getEmptyImplementation()

    override fun execute() {
        worker.busy = true
        patient.stopWaiting(eventTime)
        worker.scheduleEnd(patient, eventTime)
    }

    fun setOnWaitingStoppedActionListener(listener: IOnWaitingStoppedActionListener) {
        waitingStoppedActionListener = listener
    }

    override fun toString() = "REG_STA - ${super.toString()}"

}