package vaccinationcentresimulation.events

import simulation.eventbasedsimulation.Event
import vaccinationcentresimulation.entities.Patient
import vaccinationcentresimulation.VaccinationCentreSimulation

abstract class VaccinationCentreEvent(override val simulation: VaccinationCentreSimulation) : Event(simulation) {
    protected lateinit var patient: Patient

    protected abstract val toStringTitle: String

    open fun schedule(patient: Patient, lastEventTime: Double) {
        this.patient = patient
        super.schedule(lastEventTime + eventDuration())
    }

    protected open fun eventDuration(): Double = .0

    override fun toString() = "$toStringTitle - Patient: $patient, ${super.toString()}"

}