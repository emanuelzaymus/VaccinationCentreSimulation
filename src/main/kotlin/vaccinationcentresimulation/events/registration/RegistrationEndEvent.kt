package vaccinationcentresimulation.events.registration

import random.ContinuousUniformDistribution
import vaccinationcentresimulation.VaccinationCentreSimulation
import vaccinationcentresimulation.entities.registration.AdministrativeWorker
import vaccinationcentresimulation.events.VaccinationCentreActivityEndEvent

class RegistrationEndEvent(simulation: VaccinationCentreSimulation, worker: AdministrativeWorker) :
    VaccinationCentreActivityEndEvent(simulation, worker) {

    companion object {
        private val registrationDurationRandom = ContinuousUniformDistribution(140 / 60.0, 220 / 60.0)
    }

    override val toStringTitle = "REG_END"

    override val previousQueue get() = simulation.beforeRegistrationQueue
    override val nextQueue get() = simulation.beforeExaminationQueue
    override val nextRoom get() = simulation.examinationRoom

    override fun eventDuration(): Double = registrationDurationRandom.next()

}