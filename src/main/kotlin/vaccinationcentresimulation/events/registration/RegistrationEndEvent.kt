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

//    override fun execute() {
//        worker.setBusy(false, eventTime)
//
//        patient.startWaiting(eventTime)
//
//        if (simulation.examinationRoom.anyWorkerAvailable()) {
//            simulation.examinationRoom.scheduleStart(patient, eventTime)
//        } else {
//            simulation.beforeExaminationQueue.enqueue(patient, eventTime)
//        }
//
//        if (!simulation.beforeRegistrationQueue.isEmpty()) {
//            worker.scheduleStart(simulation.beforeRegistrationQueue.dequeue(eventTime), eventTime)
//        }
//    }

    override fun eventDuration(): Double = registrationDurationRandom.next()

}