package utils.pool

import java.util.*

class Pool<T : IPooledObject>(private val newInstanceOfT: () -> T) {

    private val pool: Queue<T> = LinkedList()

    fun acquire(): T {
        return if (pool.isEmpty())
            newInstanceOfT()
        else
            pool.remove().apply { restart() }
    }

    fun release(element: T) = pool.add(element)

}