package vaccinationcentresimulation.events.vaccination

import random.continuous.TriangularDistribution
import vaccinationcentresimulation.constants.VACCINATION_EVENT_DURATION_MAX
import vaccinationcentresimulation.constants.VACCINATION_EVENT_DURATION_MIN
import vaccinationcentresimulation.constants.VACCINATION_EVENT_DURATION_MODE
import vaccinationcentresimulation.VaccinationCentreSimulation
import vaccinationcentresimulation.entities.vaccination.Nurse
import vaccinationcentresimulation.events.VaccinationCentreEvent

class VaccinationEndEvent(simulation: VaccinationCentreSimulation, private val nurse: Nurse) :
    VaccinationCentreEvent(simulation) {

    companion object {
        private val vaccinationDurationRandom = TriangularDistribution(
            VACCINATION_EVENT_DURATION_MIN, VACCINATION_EVENT_DURATION_MODE, VACCINATION_EVENT_DURATION_MAX
        )
    }

    override val toStringTitle = "VAC_END"

    override fun execute() {
        nurse.setBusy(false, eventTime)

        simulation.waitingRoom.scheduleStartWaiting(patient, eventTime)

        if (!simulation.vaccinationQueue.isEmpty()) {
            nurse.scheduleStart(simulation.vaccinationQueue.dequeue(eventTime), eventTime)
        }
    }

    override fun eventDuration(): Double = vaccinationDurationRandom.next()

}