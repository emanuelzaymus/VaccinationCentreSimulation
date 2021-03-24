package newsstandsimulation

class Trader {
    var busy: Boolean = false
        set(value) {
            if (busy == value)
                throw IllegalArgumentException("You cannot reassigned this property with the same value: $value.")
            field = value
        }

    fun restart() {
        if (busy) busy = false
    }

}