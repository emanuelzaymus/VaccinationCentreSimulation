package vaccinationcentresimulation

import simulation.eventbasedsimulation.EventBasedSimulation
import utils.pool.Pool
import utils.StatisticsQueue
import vaccinationcentresimulation.events.patientarrival.PatientArrivalEvent
import vaccinationcentresimulation.entities.Patient
import vaccinationcentresimulation.entities.waiting.WaitingRoom
import vaccinationcentresimulation.entities.examination.ExaminationRoom
import vaccinationcentresimulation.entities.registration.RegistrationRoom
import vaccinationcentresimulation.entities.vaccination.VaccinationRoom

class VaccinationCentreSimulation(replicationsCount: Int, maxSimulationTime: Double) :
    EventBasedSimulation(replicationsCount, maxSimulationTime) {

    private val patientPool = Pool({ Patient() })

    val beforeRegistrationQueue = StatisticsQueue<Patient>()
    val registrationRoom = RegistrationRoom(5, this)

    val beforeExaminationQueue = StatisticsQueue<Patient>()
    val examinationRoom = ExaminationRoom(6, this)

    val beforeVaccinationQueue = StatisticsQueue<Patient>()
    val vaccinationRoom = VaccinationRoom(3, this)

    val waitingRoom = WaitingRoom(this)

    init {
        scheduleInitEvent()
    }

    fun getNewPatient(): Patient = patientPool.acquire()

    fun removePatient(patient: Patient) = patientPool.release(patient)

    private fun scheduleInitEvent() {
        PatientArrivalEvent(this)
            .run {
                schedule(getNewPatient(), actualSimulationTime - eventDuration())
            }
    }

}