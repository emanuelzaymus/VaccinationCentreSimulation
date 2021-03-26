package utils.busylist

interface IBusyObject {
    //    val busy: Boolean
    fun isBusy(): Boolean
    fun setBusy(busy: Boolean, eventTime: Double)
}