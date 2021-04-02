package vaccinationcentresimulation.constants

// All time values are in seconds.

internal const val WORKING_TIME: Double = 9 * 60 * 60.0 // 32_400

internal const val NOT_ARRIVING_PATIENTS_MIN: Double = 5.0
internal const val NOT_ARRIVING_PATIENTS_MAX: Double = 25.0

internal const val REGISTRATION_EVENT_DURATION_MIN: Double = 140.0
internal const val REGISTRATION_EVENT_DURATION_MAX: Double = 220.0

internal const val EXAMINATION_EVENT_DURATION_LAMBDA: Double = 1 / 260.0

internal const val VACCINATION_EVENT_DURATION_MIN: Double = 20.0
internal const val VACCINATION_EVENT_DURATION_MODE: Double = 75.0
internal const val VACCINATION_EVENT_DURATION_MAX: Double = 100.0

internal const val WAITING_EVENT_LESS_PROBABILITY: Double = 0.95
internal const val WAITING_EVENT_DURATION_LESS: Double = 15 * 60.0 // 900
internal const val WAITING_EVENT_DURATION_MORE: Double = 30 * 60.0 // 1_800
