package vaccinationcentresimulation

import simulation.eventbasedsimulation.EventBasedSimulation
import utils.pool.Pool
import utils.secToMin
import utils.statisticsqueue.StatisticsQueue
import vaccinationcentresimulation.events.patientarrival.PatientArrivalEvent
import vaccinationcentresimulation.entities.Patient
import vaccinationcentresimulation.entities.waiting.WaitingRoom
import vaccinationcentresimulation.entities.examination.ExaminationRoom
import vaccinationcentresimulation.entities.registration.RegistrationRoom
import vaccinationcentresimulation.entities.vaccination.VaccinationRoom
import vaccinationcentresimulation.events.DelayEvent

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

    val beforeRegistrationQueue = StatisticsQueue<Patient>()
    val registrationRoom = RegistrationRoom(numberOfAdminWorkers, this)

    val beforeExaminationQueue = StatisticsQueue<Patient>()
    val examinationRoom = ExaminationRoom(numberOfDoctors, this)

    val beforeVaccinationQueue = StatisticsQueue<Patient>()
    val vaccinationRoom = VaccinationRoom(numberOfNurses, this)

    val waitingRoom = WaitingRoom(this)

    fun acquirePatient(): Patient = patientPool.acquire()

    fun releasePatient(patient: Patient) = patientPool.release(patient)

    override fun beforeSimulation() {
        Patient.restartPatientIds()
    }

    override fun beforeReplication() {
        super.beforeReplication()
        beforeRegistrationQueue.restart()
        registrationRoom.restart()
        beforeExaminationQueue.restart()
        examinationRoom.restart()
        beforeVaccinationQueue.restart()
        vaccinationRoom.restart()
        waitingRoom.restart()
        scheduleInitEvents()
    }

    override fun afterReplication() {
        super.afterReplication()

        if (!isStopped()) {
            beforeRegistrationQueue.checkFinalState()
            registrationRoom.checkFinalState()
            beforeExaminationQueue.checkFinalState()
            examinationRoom.checkFinalState()
            beforeVaccinationQueue.checkFinalState()
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
        animationActionListener?.handleOnTimeChanged(actualSimulationTime)

        animationActionListener?.updateRegistrationQueueLength(beforeRegistrationQueue.count())
        animationActionListener?.updateExaminationQueueLength(beforeExaminationQueue.count())
        animationActionListener?.updateVaccinationQueueLength(beforeVaccinationQueue.count())

        animationActionListener?.updateRegistrationRoomBusyWorkersCount(registrationRoom.getBusyWorkersCount())
        animationActionListener?.updateExaminationRoomBusyDoctorsCount(examinationRoom.getBusyWorkersCount())
        animationActionListener?.updateVaccinationRoomBusyNursesCount(vaccinationRoom.getBusyWorkersCount())

        animationActionListener?.updateWaitingRoomPatientsCount(waitingRoom.waitingPatientsCount)

        animationActionListener?.updateStatistics()
    }

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
        PatientArrivalEvent(this, numberOfPatientsPerReplication)
            .scheduleFirstEvent(acquirePatient(), actualSimulationTime)

        if (isWithAnimation()) {
            startAnimation()
        }
    }

}

