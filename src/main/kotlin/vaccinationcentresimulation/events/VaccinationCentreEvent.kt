package vaccinationcentresimulation.events

import simulation.eventbasedsimulation.Event
import vaccinationcentresimulation.entities.Patient
import vaccinationcentresimulation.VaccinationCentreSimulation

/**
 * Abstract event for Vaccination Centre.
 */
abstract class VaccinationCentreEvent(override val simulation: VaccinationCentreSimulation) : Event(simulation) {
    protected lateinit var patient: Patient

    /** Name of this event for debug purpose. */
    protected abstract val toStringTitle: String

    /**
     * Schedules this event.
     * @param patient Patient for this event
     * @param lastEventTime Event time of the last event
     */
    open fun schedule(patient: Patient, lastEventTime: Double) {
        this.patient = patient
        super.schedule(lastEventTime + eventDuration())
    }

    /** @return After what time should be scheduled this event. */
    protected open fun eventDuration(): Double = .0

    override fun toString() = "$toStringTitle - Patient: $patient, ${super.toString()}"

}