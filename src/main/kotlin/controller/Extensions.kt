package controller

fun Double.roundToString(decimals: Int = 4): String = String.format("%.${decimals}f", this)

fun Double.minutesToTime(): String {
    val hours: Int = this.toInt() / 60
    val minutes: Int = this.toInt() % 60
    val seconds: Int = (this * 60).toInt() % 60
    val fraction: String = (this * 60).toString().substringAfter('.')

    return "%d:%02d:%02d,%s".format(hours, minutes, seconds, fraction)
}

fun Double.secondsToTime(): String {
    val hours: Int = toInt() / (60 * 60)
    val minutes: Int = toInt() / 60
    val seconds: Int = toInt() % 60
    val fraction: String = toString().substringAfter('.')

    return "%d:%02d:%02d,%s".format(hours, minutes, seconds, fraction)
}