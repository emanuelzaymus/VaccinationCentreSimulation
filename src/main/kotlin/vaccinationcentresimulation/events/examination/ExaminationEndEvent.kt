package vaccinationcentresimulation.events.examination

import random.continuous.ExponentialDistribution
import vaccinationcentresimulation.constants.EXAMINATION_EVENT_DURATION_LAMBDA
import vaccinationcentresimulation.VaccinationCentreSimulation
import vaccinationcentresimulation.entities.examination.Doctor
import vaccinationcentresimulation.events.VaccinationCentreActivityEndEvent

class ExaminationEndEvent(simulation: VaccinationCentreSimulation, doctor: Doctor) :
    VaccinationCentreActivityEndEvent(simulation, doctor) {

    companion object {
        private val examinationDurationRandom = ExponentialDistribution(EXAMINATION_EVENT_DURATION_LAMBDA)
    }

    override val toStringTitle = "EXA_END"

    override val previousQueue get() = simulation.examinationQueue
    override val nextQueue get() = simulation.vaccinationQueue
    override val nextRoom get() = simulation.vaccinationRoom

    override fun eventDuration(): Double = examinationDurationRandom.next()

}