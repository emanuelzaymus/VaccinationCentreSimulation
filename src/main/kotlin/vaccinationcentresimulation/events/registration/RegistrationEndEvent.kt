package vaccinationcentresimulation.events.registration

import random.continuous.ContinuousUniformDistribution
import vaccinationcentresimulation.constants.REGISTRATION_EVENT_DURATION_MAX
import vaccinationcentresimulation.constants.REGISTRATION_EVENT_DURATION_MIN
import vaccinationcentresimulation.VaccinationCentreSimulation
import vaccinationcentresimulation.entities.registration.AdministrativeWorker
import vaccinationcentresimulation.events.VaccinationCentreActivityEndEvent

/**
 * Start of registration.
 */
class RegistrationEndEvent(simulation: VaccinationCentreSimulation, worker: AdministrativeWorker) :
    VaccinationCentreActivityEndEvent(simulation, worker) {

    companion object {
        private val registrationDurationRandom =
            ContinuousUniformDistribution(REGISTRATION_EVENT_DURATION_MIN, REGISTRATION_EVENT_DURATION_MAX)
    }

    override val toStringTitle = "REG_END"

    override val previousQueue get() = simulation.registrationQueue
    override val nextQueue get() = simulation.examinationQueue
    override val nextRoom get() = simulation.examinationRoom

    override fun eventDuration(): Double = registrationDurationRandom.next()

}