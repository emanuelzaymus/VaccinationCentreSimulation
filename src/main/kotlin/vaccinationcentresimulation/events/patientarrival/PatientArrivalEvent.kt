package vaccinationcentresimulation.events.patientarrival

import random.discrete.DiscreteUniformDistribution
import vaccinationcentresimulation.VaccinationCentreSimulation
import vaccinationcentresimulation.events.VaccinationCentreEvent
import vaccinationcentresimulation.entities.Patient

class PatientArrivalEvent(simulation: VaccinationCentreSimulation, private val numberOfPatients: Int) :
    VaccinationCentreEvent(simulation) {

    companion object {
        /** Maximal simulation tim in minutes. (Working time is 9 hours = 540 minutes) */
        private const val MAX_TIME = 540.0

        // TODO: Should be set proportionally based on numberOfPatients (540 patients => 5-25 notArriving)
        private val notArrivingPatientsRandom = DiscreteUniformDistribution(5, 25)
    }

    override val toStringTitle = "ARRIVAL"

    private val arrivingPatientNumbers = DiscreteUniformDistribution(until = numberOfPatients)
    private val numberOfNotArrivingPatients = notArrivingPatientsRandom.next()
    private val eventDuration = MAX_TIME / numberOfPatients
    private var executedArrivals = 0

    fun scheduleFirstEvent(patient: Patient, lastEventTime: Double) {
        schedule(patient, lastEventTime - eventDuration())
    }

    override fun schedule(patient: Patient, lastEventTime: Double) {
        var eventTimeToBe = lastEventTime
        while (true) {
            if (executedArrivals >= numberOfPatients) {
                break
            }
            executedArrivals++
            if (numberOfNotArrivingPatients < arrivingPatientNumbers.next()) {
                super.schedule(patient, eventTimeToBe)
                break
            } else {
                eventTimeToBe += eventDuration()
            }
        }
    }

    override fun execute() {
        patient.startWaiting(eventTime)

        if (simulation.registrationRoom.anyWorkerAvailable()) {
            simulation.registrationRoom.scheduleStart(patient, eventTime)
        } else {
            simulation.registrationQueue.enqueue(patient, eventTime)
        }
        schedule(simulation.acquirePatient(), eventTime)
    }

    override fun eventDuration(): Double = eventDuration

}