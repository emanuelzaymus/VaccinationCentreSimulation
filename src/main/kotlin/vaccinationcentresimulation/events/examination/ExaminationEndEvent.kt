package vaccinationcentresimulation.events.examination

import random.ExponentialDistribution
import vaccinationcentresimulation.VaccinationCentreSimulation
import vaccinationcentresimulation.entities.examination.Doctor
import vaccinationcentresimulation.events.VaccinationCentreActivityEndEvent

class ExaminationEndEvent(simulation: VaccinationCentreSimulation, doctor: Doctor) :
    VaccinationCentreActivityEndEvent(simulation, doctor) {

    companion object {
        private val examinationDurationRandom = ExponentialDistribution(1 / (260 / 60.0))
    }

    override val toStringTitle = "EXA_END"

    override val previousQueue get() = simulation.beforeExaminationQueue
    override val nextQueue get() = simulation.beforeVaccinationQueue
    override val nextRoom get() = simulation.vaccinationRoom

    override fun eventDuration(): Double = examinationDurationRandom.next()

}