package newsstandsimulation.events

import newsstandsimulation.Customer

interface IOnStartServiceActionSource {
    fun setOnStartServiceActionListener(listener: IOnStartServiceActionListener)
    fun customer(): Customer
}