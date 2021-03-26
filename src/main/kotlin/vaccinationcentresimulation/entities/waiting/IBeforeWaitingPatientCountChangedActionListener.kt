package vaccinationcentresimulation.entities.waiting

interface IBeforeWaitingPatientCountChangedActionListener {

    companion object {
        fun getEmptyImplementation(): IBeforeWaitingPatientCountChangedActionListener =
            EmptyBeforeWaitingPatientCountChangedActionListener()
    }

    fun handleBeforeWaitingPatientsCountChanged(lastCount: Int, timeElapsed: Double)

    class EmptyBeforeWaitingPatientCountChangedActionListener : IBeforeWaitingPatientCountChangedActionListener {
        override fun handleBeforeWaitingPatientsCountChanged(lastCount: Int, timeElapsed: Double) {}
    }

}