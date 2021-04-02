package vaccinationcentresimulation.events

import utils.statisticsqueue.StatisticsQueue
import vaccinationcentresimulation.VaccinationCentreSimulation
import vaccinationcentresimulation.entities.Patient
import vaccinationcentresimulation.entities.VaccinationCentreRoom
import vaccinationcentresimulation.entities.VaccinationCentreWorker

abstract class VaccinationCentreActivityEndEvent(
    simulation: VaccinationCentreSimulation,
    private val worker: VaccinationCentreWorker
) :
    VaccinationCentreEvent(simulation) {

    protected abstract val previousQueue: StatisticsQueue<Patient>
    protected abstract val nextQueue: StatisticsQueue<Patient>
    protected abstract val nextRoom: VaccinationCentreRoom<*>

    override fun execute() {
        worker.setBusy(false, eventTime, simulation.actualSimulationTime)

        patient.startWaiting(eventTime)

        if (nextRoom.anyWorkerAvailable()) {
            nextRoom.scheduleStart(patient, eventTime)
        } else {
            nextQueue.enqueue(patient, eventTime, simulation.actualSimulationTime)
        }

        if (!previousQueue.isEmpty()) {
            worker.scheduleStart(previousQueue.dequeue(eventTime, simulation.actualSimulationTime), eventTime)
        }
    }

}