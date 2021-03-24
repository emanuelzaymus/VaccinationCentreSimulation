package vaccinationcentresimulation.entities

import utils.busylist.BusyList
import vaccinationcentresimulation.VaccinationCentreSimulation

abstract class VaccinationCentreRoom<T : VaccinationCentreEmployee>(
    val simulation: VaccinationCentreSimulation,
    numberOfEmployees: Int,
    init: (Int) -> T
) {
    private val employees = BusyList(numberOfEmployees, init)

    fun anyEmployeeAvailable(): Boolean = employees.anyAvailable()

    fun scheduleStart(patient: Patient, eventTime: Double) {
        val employee = employees.getRandomAvailable()
        employee.scheduleStart(patient, eventTime)
    }

}
