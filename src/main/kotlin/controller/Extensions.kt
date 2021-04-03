package controller

fun Double.roundToString(decimals: Int = 6): String = String.format("%.${decimals}f", this)

fun Double.secondsToTime(): String {
    val int = toInt()
    val hours: Int = int / 3600 // 60 * 60
    val minutes: Int = (int - hours * 3600) / 60
    val seconds: Int = int % 60
    val fraction: String = (toString().substringAfter('.') + "00000").substring(0, 6)

    return "%d:%02d:%02d,%s".format(hours, minutes, seconds, fraction)
}