package vaccinationcentresimulation.events.patientarrival

import random.DiscreteUniformDistribution
import vaccinationcentresimulation.VaccinationCentreSimulation
import vaccinationcentresimulation.events.VaccinationCentreEvent
import vaccinationcentresimulation.entities.Patient

class PatientArrivalEvent(simulation: VaccinationCentreSimulation) : VaccinationCentreEvent(simulation) {

    companion object {
        private val notArrivingPatientsRandom = DiscreteUniformDistribution(5, 25)
    }

    private val numberOfNotArrivingPatients = notArrivingPatientsRandom.next()
    private val arrivingPatientNumbers = DiscreteUniformDistribution(0, simulation.maxSimulationTime.toInt())

    override fun schedule(patient: Patient, lastEventTime: Double) {
        var eventTimeToBe = lastEventTime
        while (true) {
            if (numberOfNotArrivingPatients < arrivingPatientNumbers.next()) {
                super.schedule(patient, eventTimeToBe)
                return
            } else {
                eventTimeToBe += eventDuration()
            }
        }
    }

    override fun execute() {
        patient.startWaiting(eventTime)

        if (simulation.registrationRoom.anyEmployeeAvailable()) {
            simulation.registrationRoom.scheduleStart(patient, eventTime)
        } else {
            simulation.beforeRegistrationQueue.enqueue(patient, eventTime)
        }
        schedule(simulation.getNewPatient(), eventTime)
    }

    public override fun eventDuration(): Double = 1.0

    override fun toString() = "ARRIVAL - ${super.toString()}"

}