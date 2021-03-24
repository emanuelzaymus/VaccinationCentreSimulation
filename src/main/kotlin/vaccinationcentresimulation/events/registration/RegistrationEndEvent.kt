package vaccinationcentresimulation.events.registration

import random.ContinuousUniformDistribution
import vaccinationcentresimulation.VaccinationCentreSimulation
import vaccinationcentresimulation.events.VaccinationCentreEvent
import vaccinationcentresimulation.entities.registration.AdministrativeWorker

class RegistrationEndEvent(simulation: VaccinationCentreSimulation, private val worker: AdministrativeWorker) :
    VaccinationCentreEvent(simulation) {

    companion object {
        private val registrationDurationRandom = ContinuousUniformDistribution(140 / 60.0, 220 / 60.0)
    }

    override fun execute() {
        worker.busy = false

        patient.startWaiting(eventTime)

        if (simulation.examinationRoom.anyEmployeeAvailable()) {
            simulation.examinationRoom.scheduleStart(patient, eventTime)
        } else {
            simulation.beforeExaminationQueue.enqueue(patient, eventTime)
        }

        if (!simulation.beforeRegistrationQueue.isEmpty()) {
            worker.scheduleStart(simulation.beforeRegistrationQueue.dequeue(eventTime), eventTime)
        }
    }

    override fun eventDuration(): Double = registrationDurationRandom.next()

    override fun toString() = "REG_END - ${super.toString()}"

}