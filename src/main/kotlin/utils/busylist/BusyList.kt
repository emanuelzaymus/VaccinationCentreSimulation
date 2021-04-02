package utils.busylist

import random.discrete.DiscreteUniformDistribution
import utils.IReusable

class BusyList<T : IBusyObject>(numberOfBusyObjects: Int, init: (Int) -> T) : Iterable<T>, IReusable {

    private val randoms = List(numberOfBusyObjects - 1) { DiscreteUniformDistribution(until = it + 2) }
    private val busyObjects = List(numberOfBusyObjects, init)

    val size: Int get() = busyObjects.size

    fun anyAvailable(): Boolean = busyObjects.any { !it.isBusy() }

    fun getRandomAvailable(): T {
        val freeObjects = busyObjects.filter { !it.isBusy() }

        if (freeObjects.isEmpty())
            throw IllegalStateException("Non of the objects is free.")

        if (freeObjects.size == 1)
            return freeObjects[0]

        val sizeRandom = getRandomForSize(freeObjects.size)
        return freeObjects[sizeRandom.next()]
    }

    override fun restart() = busyObjects.forEach { it.restart() }

    override fun checkFinalState() = busyObjects.forEach { it.checkFinalState() }

    fun getBusyWorkersCount(): Int = busyObjects.count { it.isBusy() }

    private fun getRandomForSize(size: Int) = randoms[size - 2]

    override fun iterator(): Iterator<T> = busyObjects.iterator()

}