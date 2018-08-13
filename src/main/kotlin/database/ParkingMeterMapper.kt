package database

import database.entities.ParkingMeterEntryEntity
import domain.Driver
import domain.ParkingMeterEntry
import domain.Vehicle
import domain.money.Currency
import domain.money.Money
import java.math.BigDecimal

class ParkingMeterMapper {
    companion object {
        fun map(parkingMeterEntry: ParkingMeterEntry, id: Long, vehicleId: Long, driverId: Long): ParkingMeterEntryEntity {
            val amount = if (parkingMeterEntry.fee == null) null else parkingMeterEntry.fee.amount.toDouble()
            val startDate = parkingMeterEntry.startTime
            val businessID = parkingMeterEntry.businessID
            val currency = parkingMeterEntry.fee?.currency.toString()
            val endDate = parkingMeterEntry.endDate

            return ParkingMeterEntryEntity(id, businessID, vehicleId, driverId,
                    startDate, endDate, currency, amount)
        }

        fun map(parkingMeterEntryEntity: ParkingMeterEntryEntity, vehicle: Vehicle, driver: Driver): ParkingMeterEntry {
            val money = if (parkingMeterEntryEntity.amount == null) {
                null
            } else {
                Money.of(BigDecimal.valueOf(parkingMeterEntryEntity.amount!!), Currency.valueOf(parkingMeterEntryEntity.currency!!))
            }

            return ParkingMeterEntry(parkingMeterEntryEntity.businessID, vehicle,
                    driver, parkingMeterEntryEntity.startTime, parkingMeterEntryEntity.endDate, money)
        }
    }
}