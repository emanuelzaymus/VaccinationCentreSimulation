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

//    override fun execute() {
//        worker.setBusy(false, eventTime)
//
//        patient.startWaiting(eventTime)
//
//        if (simulation.vaccinationRoom.anyWorkerAvailable()) {
//            simulation.vaccinationRoom.scheduleStart(patient, eventTime)
//        } else {
//            simulation.beforeVaccinationQueue.enqueue(patient, eventTime)
//        }
//
//        if (!simulation.beforeExaminationQueue.isEmpty()) {
//            worker.scheduleStart(simulation.beforeExaminationQueue.dequeue(eventTime), eventTime)
//        }
//    }

}