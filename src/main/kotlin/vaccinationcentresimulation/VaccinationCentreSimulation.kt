package vaccinationcentresimulation

import simulation.eventbasedsimulation.EventBasedSimulation
import utils.pool.Pool
import utils.statisticsqueue.StatisticsQueue
import vaccinationcentresimulation.entities.Patient
import vaccinationcentresimulation.entities.examination.ExaminationRoom
import vaccinationcentresimulation.entities.registration.RegistrationRoom
import vaccinationcentresimulation.entities.vaccination.VaccinationRoom
import vaccinationcentresimulation.entities.waiting.WaitingRoom
import vaccinationcentresimulation.events.DelayEvent
import vaccinationcentresimulation.events.patientarrival.PatientArrivalEvent

class VaccinationCentreSimulation(
    replicationsCount: Int = 1,
    private val numberOfPatientsPerReplication: Int = 450,
    numberOfAdminWorkers: Int = 5,
    numberOfDoctors: Int = 6,
    numberOfNurses: Int = 3,
    withAnimation: Boolean = true
) :
    EventBasedSimulation(replicationsCount, withAnimation = withAnimation) {

    private var animationActionListener: IAnimationActionListener? = null

    private val delayEvent = DelayEvent(this)
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
        scheduleInitEvents()
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

    fun setDelayEverySimMin(seconds: Int) = delayEvent.setDelayEverySimMin(seconds)

    fun setDelayForMillis(milliseconds: Int) = delayEvent.setDelayForMillis(milliseconds.toLong())

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

    // TODO: Should be in the ancestor
    override fun afterAnimation() {
        super.afterAnimation()

        if (containsEvent(delayEvent) && eventsCount() == 1) {
            removeEvent(delayEvent)
        }
    }

    // TODO: Should be in the ancestor
    fun setAnimation(animate: Boolean) {
        if (animate)
            startAnimation()
        else
            stopAnimation()
    }

    override fun startAnimation() {
        super.startAnimation()
        delayEvent.schedule(actualSimulationTime)
    }

    override fun stopAnimation() {
        super.stopAnimation()
        removeEvent(delayEvent)
    }

    private fun scheduleInitEvents() {
        // TODO: Maybe can make an attribute from this (then I do not have to remember numberOfPatientsPerReplication)
        PatientArrivalEvent(this, numberOfPatientsPerReplication)
            .scheduleFirstEvent(acquirePatient(), actualSimulationTime)

        if (isWithAnimation()) {
            startAnimation()
        }
    }

}

