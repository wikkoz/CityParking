package database

import database.entities.DriverEntity
import database.entities.ParkingMeterEntryEntity
import database.entities.VehicleEntity
import domain.Driver
import domain.ParkingMeterEntry
import domain.Vehicle
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.lang.IllegalArgumentException
import java.time.LocalDate
import java.time.LocalDateTime

class Repository {
    companion object {
        private val drivers: MutableMap<Long, DriverEntity> = mutableMapOf(
                100L to DriverEntity(1, true, 100),
                200L to DriverEntity(2, false, 200)
        )
        private val vehicles: MutableMap<Long, VehicleEntity> = mutableMapOf(
                100L to VehicleEntity(1, 100),
                200L to VehicleEntity(2 ,200),
                300L to VehicleEntity(3 ,300),
                400L to VehicleEntity(3 ,400)
        )
        private val parkingMeterEntries: MutableMap<Long, ParkingMeterEntryEntity> = mutableMapOf()
    }

    fun findParkingMeterEntriesWithVehicle(vehicleBusinessId: Long): List<ParkingMeterEntry> {
        val vehicleId = vehicles[vehicleBusinessId]?.id ?: throw IllegalArgumentException("Cannot find vehicle with business id $vehicleBusinessId")
        return parkingMeterEntries.values.filter { it.vehicleId == vehicleId }
                .map {findParkingMeterEntry(it.businessID) }
    }

    fun findParkingMeterEntry(businessID: Long): ParkingMeterEntry {
        val parkingMeterEntryEntity = parkingMeterEntries[businessID]
                ?: throw IllegalArgumentException("Cannot find parking meter entry with business id $businessID")

        return mapParkingMeterEntity(parkingMeterEntryEntity)
    }

    private fun mapParkingMeterEntity(parkingMeterEntryEntity: ParkingMeterEntryEntity) =
            ParkingMeterMapper.map(parkingMeterEntryEntity, findVehicleById(parkingMeterEntryEntity.vehicleId), findDriverById(parkingMeterEntryEntity.driverId))

    fun findVehicle(businessID: Long): Vehicle {
        val entity = vehicles[businessID] ?: throw IllegalArgumentException("Cannot find vehicle with business id $businessID")
        return Vehicle(entity.businessID)
    }

    fun findDriver(businessID: Long): Driver {
        val entity = drivers[businessID] ?: throw IllegalArgumentException("Cannot find driver with business id $businessID")
        return Driver(entity.disabled, entity.businessID)
    }

    fun saveParkingMeterEntry(parkingMeterEntry: ParkingMeterEntry) {
        val vehicleId = vehicles.values.firstOrNull { it.businessID == parkingMeterEntry.vehicle.businessID }?.id ?:
            throw IllegalArgumentException("Cannot find vehicle with id ${parkingMeterEntry.vehicle.businessID}")
        val driverId = drivers.values.firstOrNull { it.businessID == parkingMeterEntry.driver.businessID }?.id ?:
            throw IllegalArgumentException("Cannot find driver with id ${parkingMeterEntry.driver.businessID}")

        val parkingMeterEntity = ParkingMeterMapper.map(parkingMeterEntry, parkingMeterEntries.size.toLong(), vehicleId, driverId)
        parkingMeterEntries[parkingMeterEntity.businessID] = parkingMeterEntity
    }

    fun findParkingMeterEntries(day: LocalDate): List<ParkingMeterEntry> {
        return parkingMeterEntries.values
                .filter { entry -> entry.endDate?.toLocalDate() == day }
                .map { mapParkingMeterEntity(it) }
    }

    private fun findVehicleById(id: Long): Vehicle {
        val vehicle =  vehicles.values.firstOrNull { it.id == id} ?: throw IllegalArgumentException("Cannot find vehicle with id $id")
        return Vehicle(vehicle.businessID)
    }

    private fun findDriverById(id: Long): Driver {
        val entity = drivers.values.firstOrNull { it.id == id} ?: throw IllegalArgumentException("Cannot find driver with id $id")
        return Driver(entity.disabled, entity.businessID)
    }
}