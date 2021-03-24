package newsstandsimulation

import newsstandsimulation.events.CustomerArrivalEvent
import newsstandsimulation.events.EndServiceEvent
import newsstandsimulation.events.StartServiceEvent
import simulation.eventbasedsimulation.EventBasedSimulation
import utils.pool.Pool
import utils.statisticsqueue.StatisticsQueue

class NewsStandSimulation(replicationsCount: Int, maxSimulationTime: Double) :
    EventBasedSimulation(replicationsCount, maxSimulationTime) {

    private val customerPool = Pool({ Customer() })
    val customerQueue = StatisticsQueue<Customer>()

    val trader = Trader()
    val startServiceEvent = StartServiceEvent(this)
    val endServiceEvent = EndServiceEvent(this)

    init {
        scheduleInitEvent()
    }

    fun getNewCustomer(): Customer = customerPool.acquire()

    fun removeCustomer(customer: Customer) = customerPool.release(customer)

    override fun beforeReplication() {
        super.beforeReplication()
        customerQueue.clear()
        trader.restart()
        startServiceEvent.restart()
        endServiceEvent.restart()
        scheduleInitEvent()
    }

    private fun scheduleInitEvent() {
        CustomerArrivalEvent(this)
            .run {
                schedule(getNewCustomer(), actualSimulationTime)
            }
    }

}