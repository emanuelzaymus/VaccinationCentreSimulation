package vaccinationcentresimulation.events.patientarrival

import random.DiscreteUniformDistribution
import vaccinationcentresimulation.VaccinationCentreSimulation
import vaccinationcentresimulation.events.VaccinationCentreEvent
import vaccinationcentresimulation.entities.Patient

class PatientArrivalEvent(simulation: VaccinationCentreSimulation, private val numberOfPatients: Int) :
    VaccinationCentreEvent(simulation) {

    companion object {
        private val notArrivingPatientsRandom = DiscreteUniformDistribution(5, 25)
    }

    private val arrivingPatientNumbers = DiscreteUniformDistribution(until = numberOfPatients)
    private val numberOfNotArrivingPatients = notArrivingPatientsRandom.next()
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
            simulation.beforeRegistrationQueue.enqueue(patient, eventTime)
        }
        schedule(simulation.acquirePatient(), eventTime)
    }

    override fun eventDuration(): Double = 1.0

    override fun toString() = "ARRIVAL - ${super.toString()}"

}