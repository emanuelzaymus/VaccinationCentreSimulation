package controller

//fun Double.round(decimals: Int = 2): Double = "%.${decimals}f".format(this).toDouble()

fun Double.roundToString(decimals: Int = 4): String = String.format("%.${decimals}f", this)