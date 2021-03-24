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

class VaccinationCentreSimulation(replicationsCount: Int, private val numberOfPatientsPerReplication: Int) :
    EventBasedSimulation(replicationsCount) {

    private val patientPool = Pool { Patient() }

    val beforeRegistrationQueue = StatisticsQueue<Patient>()
    val registrationRoom = RegistrationRoom(5, this)

    val beforeExaminationQueue = StatisticsQueue<Patient>()
    val examinationRoom = ExaminationRoom(6, this)

    val beforeVaccinationQueue = StatisticsQueue<Patient>()
    val vaccinationRoom = VaccinationRoom(3, this)

    val waitingRoom = WaitingRoom(this)

    fun acquirePatient(): Patient = patientPool.acquire()

    fun releasePatient(patient: Patient) = patientPool.release(patient)

    override fun beforeReplication() {
        super.beforeReplication()
        scheduleInitEvent()
    }

    private fun scheduleInitEvent() {
        PatientArrivalEvent(this, numberOfPatientsPerReplication)
            .run {
                scheduleFirstEvent(acquirePatient(), actualSimulationTime)
            }
    }

}