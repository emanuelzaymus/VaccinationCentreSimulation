package vaccinationcentresimulation.entities.vaccination

import vaccinationcentresimulation.VaccinationCentreSimulation
import vaccinationcentresimulation.entities.Patient
import vaccinationcentresimulation.entities.VaccinationCentreEmployee
import vaccinationcentresimulation.events.vaccination.VaccinationEndEvent
import vaccinationcentresimulation.events.vaccination.VaccinationStartEvent

class Nurse(simulation: VaccinationCentreSimulation) : VaccinationCentreEmployee() {

    private val startVaccinationEvent = VaccinationStartEvent(simulation, this)
    private val endVaccinationEvent = VaccinationEndEvent(simulation, this)

    fun scheduleStartVaccination(patient: Patient, eventTime: Double) {
        startVaccinationEvent.schedule(patient, eventTime)
    }

    fun scheduleEndVaccination(patient: Patient, eventTime: Double) {
        endVaccinationEvent.schedule(patient, eventTime)
    }

    override fun scheduleStart(patient: Patient, eventTime: Double) {
        startVaccinationEvent.schedule(patient, eventTime)
    }

}