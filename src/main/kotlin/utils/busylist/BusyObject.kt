package utils.busylist

abstract class BusyObject : IBusyObject {
    override var busy: Boolean = false
        set(value) {
            if (field == value)
                throw IllegalArgumentException("You cannot reassigned this property with the same value: $value.")
            field = value
        }
}