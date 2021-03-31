package vaccinationcentresimulation

import simulation.eventbasedsimulation.EventBasedSimulation
import utils.pool.Pool
import utils.statisticsqueue.StatisticsQueue
import vaccinationcentresimulation.entities.Patient
import vaccinationcentresimulation.entities.examination.ExaminationRoom
import vaccinationcentresimulation.entities.registration.RegistrationRoom
import vaccinationcentresimulation.entities.vaccination.VaccinationRoom
import vaccinationcentresimulation.entities.waiting.WaitingRoom
import vaccinationcentresimulation.events.patientarrival.PatientArrivalEvent

class VaccinationCentreSimulation(
    replicationsCount: Int,
    private val numberOfPatientsPerReplication: Int,
    numberOfAdminWorkers: Int,
    numberOfDoctors: Int,
    numberOfNurses: Int,
    withAnimation: Boolean
) :
    EventBasedSimulation(replicationsCount, withAnimation = withAnimation) {

    private var animationActionListener: IAnimationActionListener? = null

    private val patientPool = Pool { Patient() }

    val registrationQueue = StatisticsQueue<Patient>()
    val registrationRoom = RegistrationRoom(numberOfAdminWorkers, this)

    val examinationQueue = StatisticsQueue<Patient>()
    val examinationRoom = ExaminationRoom(numberOfDoctors, this)

    val vaccinationQueue = StatisticsQueue<Patient>()
    val vaccinationRoom = VaccinationRoom(numberOfNurses, this)

    val waitingRoom = WaitingRoom(this)

    fun acquirePatient(): Patient = patientPool.acquire()

    fun releasePatient(patient: Patient) = patientPool.release(patient)

    override fun beforeSimulation() {
        super.beforeSimulation()
        Patient.restartPatientIds()
    }

    override fun beforeReplication() {
        super.beforeReplication()
        registrationQueue.restart()
        registrationRoom.restart()
        examinationQueue.restart()
        examinationRoom.restart()
        vaccinationQueue.restart()
        vaccinationRoom.restart()
        waitingRoom.restart()
        scheduleInitEvent()
    }

    override fun afterReplication() {
        super.afterReplication()

        println("Replic: $currentReplicNumber")

        if (!isStopped()) {
            registrationQueue.checkFinalState()
            registrationRoom.checkFinalState()
            examinationQueue.checkFinalState()
            examinationRoom.checkFinalState()
            vaccinationQueue.checkFinalState()
            vaccinationRoom.checkFinalState()
            waitingRoom.checkFinalState()
        }
    }

    override fun afterSimulation() {
        super.afterSimulation()
        animate()
    }

    fun setAnimationActionListener(listener: IAnimationActionListener) {
        animationActionListener = listener
    }

    override fun animate() {
        if (animationActionListener == null) {
            return
        }
        animationActionListener?.updateActualSimulationTime(actualSimulationTime)

        animationActionListener?.updateRegistrationQueueLength(registrationQueue.count())
        animationActionListener?.updateExaminationQueueLength(examinationQueue.count())
        animationActionListener?.updateVaccinationQueueLength(vaccinationQueue.count())

        animationActionListener?.updateRegistrationRoomBusyWorkersCount(registrationRoom.getBusyWorkersCount())
        animationActionListener?.updateExaminationRoomBusyDoctorsCount(examinationRoom.getBusyWorkersCount())
        animationActionListener?.updateVaccinationRoomBusyNursesCount(vaccinationRoom.getBusyWorkersCount())

        animationActionListener?.updateWaitingRoomPatientsCount(waitingRoom.waitingPatientsCount)

        animationActionListener?.updateStatistics()
    }

    private fun scheduleInitEvent() {
        PatientArrivalEvent(this, numberOfPatientsPerReplication)
            .scheduleFirstEvent(acquirePatient(), actualSimulationTime)
    }

}

