package utils.busylist

import random.DiscreteUniformDistribution

class BusyList<T : IBusyObject>(numberOfBusyObjects: Int, init: (Int) -> T) {

    private val randoms = List(numberOfBusyObjects - 1) { DiscreteUniformDistribution(until = it + 2) }
    private val busyObjects = List(numberOfBusyObjects, init)

    fun anyAvailable(): Boolean = busyObjects.any { !it.busy }

    fun getRandomAvailable(): T {
        val freeObjects = busyObjects.filter { !it.busy }

        if (freeObjects.isEmpty())
            throw IllegalStateException("Non of the objects is free.")

        if (freeObjects.size == 1)
            return freeObjects[0]

        val sizeRandom = getRandomForSize(freeObjects.size)
        return freeObjects[sizeRandom.next()]
    }

    private fun getRandomForSize(size: Int) = randoms[size - 2]

}