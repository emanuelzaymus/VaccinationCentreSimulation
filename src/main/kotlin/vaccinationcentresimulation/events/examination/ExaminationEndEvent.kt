package vaccinationcentresimulation.events.examination

import random.ExponentialDistribution
import vaccinationcentresimulation.VaccinationCentreSimulation
import vaccinationcentresimulation.entities.examination.Doctor
import vaccinationcentresimulation.events.VaccinationCentreEvent

class ExaminationEndEvent(simulation: VaccinationCentreSimulation, private val doctor: Doctor) :
    VaccinationCentreEvent(simulation) {

    private val examinationDurationRandom = ExponentialDistribution(1 / (260 / 60.0))

    override fun execute() {
        doctor.busy = false

        patient.startWaiting(eventTime)

        if (simulation.vaccinationRoom.anyEmployeeAvailable()) {
            simulation.vaccinationRoom.scheduleStart(patient, eventTime)
        } else {
            simulation.beforeVaccinationQueue.enqueue(patient, eventTime)
        }

        if (!simulation.beforeRegistrationQueue.isEmpty()) {
            doctor.scheduleStart(simulation.beforeExaminationQueue.dequeue(eventTime), eventTime)
        }
    }

    override fun eventDuration(): Double = examinationDurationRandom.next()

}