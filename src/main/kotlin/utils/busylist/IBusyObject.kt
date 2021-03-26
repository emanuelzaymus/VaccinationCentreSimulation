package utils.busylist

interface IBusyObject {
    fun isBusy(): Boolean
    fun setBusy(busy: Boolean, eventTime: Double)
    fun restart()
    fun checkFinalState()
}