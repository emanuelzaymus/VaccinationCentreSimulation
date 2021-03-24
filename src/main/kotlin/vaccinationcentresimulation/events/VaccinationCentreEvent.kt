package vaccinationcentresimulation.events

import simulation.eventbasedsimulation.Event
import vaccinationcentresimulation.entities.Patient
import vaccinationcentresimulation.VaccinationCentreSimulation

abstract class VaccinationCentreEvent(protected val simulation: VaccinationCentreSimulation) : Event() {
    protected lateinit var patient: Patient

    open fun schedule(patient: Patient, lastEventTime: Double) {
        this.patient = patient
        eventTime = lastEventTime + eventDuration()
        simulation.scheduleEvent(this)
    }

    protected open fun eventDuration(): Double = .0

    override fun toString() = "Patient: $patient, ${super.toString()}"

}