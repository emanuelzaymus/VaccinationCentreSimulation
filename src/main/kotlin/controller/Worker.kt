package controller

class Worker(val id: Int, private val isWorking: Boolean, val workload: Double) {
    val working: String get() = if (isWorking) "Yes" else "No"
    val avgWorkload get() = workload.roundToString()
}