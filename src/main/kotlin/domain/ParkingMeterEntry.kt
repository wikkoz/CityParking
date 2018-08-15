package domain

import domain.money.Money
import java.time.LocalDateTime

data class ParkingMeterEntry(val businessID: Long, val vehicle: Vehicle, val driver: Driver,
                             val startTime: LocalDateTime, val endDate: LocalDateTime? = null, val fee: Money? = null) {

    fun stopParkingMeter(endDate: LocalDateTime, money: Money): ParkingMeterEntry {
        return ParkingMeterEntry(businessID, vehicle, driver, startTime, endDate, money)
    }

    fun isFinished() = endDate != null
}