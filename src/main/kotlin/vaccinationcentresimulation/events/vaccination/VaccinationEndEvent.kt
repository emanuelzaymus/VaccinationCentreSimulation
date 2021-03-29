package vaccinationcentresimulation.events.vaccination

import random.TriangularDistribution
import utils.secToMin
import vaccinationcentresimulation.VaccinationCentreSimulation
import vaccinationcentresimulation.entities.vaccination.Nurse
import vaccinationcentresimulation.events.VaccinationCentreEvent

class VaccinationEndEvent(simulation: VaccinationCentreSimulation, private val nurse: Nurse) :
    VaccinationCentreEvent(simulation) {

    companion object {
        private val vaccinationDurationRandom = TriangularDistribution(secToMin(20), secToMin(75), secToMin(100))
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