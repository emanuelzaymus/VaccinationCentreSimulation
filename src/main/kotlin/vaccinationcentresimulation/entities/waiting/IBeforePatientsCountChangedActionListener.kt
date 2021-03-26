package vaccinationcentresimulation.entities.waiting

interface IBeforePatientsCountChangedActionListener {
    fun handleBeforePatientsCountChanged(lastCount: Int, timeElapsed: Double)
}