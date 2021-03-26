package vaccinationcentresimulation.events.vaccination

import random.TriangularDistribution
import vaccinationcentresimulation.VaccinationCentreSimulation
import vaccinationcentresimulation.entities.vaccination.Nurse
import vaccinationcentresimulation.events.VaccinationCentreEvent

class VaccinationEndEvent(simulation: VaccinationCentreSimulation, private val nurse: Nurse) :
    VaccinationCentreEvent(simulation) {

    companion object {
        private val vaccinationDurationRandom = TriangularDistribution(20.0, 75.0, 100.0)
    }

    override val toStringTitle = "VAC_END"

    override fun execute() {
        nurse.setBusy(false, eventTime)

        simulation.waitingRoom.scheduleStartWaiting(patient, eventTime)

        if (!simulation.beforeVaccinationQueue.isEmpty()) {
            nurse.scheduleStart(simulation.beforeVaccinationQueue.dequeue(eventTime), eventTime)
        }
    }

    override fun eventDuration(): Double = vaccinationDurationRandom.next()

}