package vaccinationcentresimulation.entities

import utils.busylist.BusyObject

abstract class VaccinationCentreEmployee() : BusyObject() {
    abstract fun scheduleStart(patient: Patient, eventTime: Double)
}