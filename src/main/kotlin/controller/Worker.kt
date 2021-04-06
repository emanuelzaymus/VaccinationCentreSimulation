package controller

/**
 * Worker data class with workers id, state-isWorking and workload.
 */
data class Worker(val id: Int, private val isWorking: Boolean, private val workload: Double) {
    val working: String get() = if (isWorking) "Yes" else "No"
    val avgWorkload get() = workload.roundToString()
}