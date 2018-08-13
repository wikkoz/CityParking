package database.entities

import domain.Driver
import domain.Vehicle
import domain.money.Money
import java.time.LocalDateTime

class ParkingMeterEntryEntity(val id:Long, var businessID: Long, var vehicleId: Long, var driverId: Long,
                              var startTime: LocalDateTime, var endDate: LocalDateTime?, var currency: String?, var amount: Double?) {
}