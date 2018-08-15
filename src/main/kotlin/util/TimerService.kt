package util

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

open class TimerService {
    fun now(): LocalDateTime {
        return LocalDateTime.now()
    }

    fun countFullHoursDifference(beginDate: LocalDateTime, endDate: LocalDateTime): Int =
            beginDate.until(endDate, ChronoUnit.HOURS).toInt() + 1
}