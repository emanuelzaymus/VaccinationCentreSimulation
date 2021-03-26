package vaccinationcentresimulation.entities.waiting

interface IBeforeWaitingPatientCountChangedActionListener {
    fun handleBeforeWaitingPatientsCountChanged(lastCount: Int, timeElapsed: Double)
}