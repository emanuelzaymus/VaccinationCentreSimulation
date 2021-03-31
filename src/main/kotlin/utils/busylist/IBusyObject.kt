package utils.busylist

import utils.IReusable

interface IBusyObject : IReusable {
    fun isBusy(): Boolean
    fun setBusy(busy: Boolean, eventTime: Double)
}