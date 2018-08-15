package database

import database.entities.DriverEntity
import database.entities.ParkingMeterEntryEntity
import database.entities.VehicleEntity
import domain.Driver
import domain.ParkingMeterEntry
import domain.Vehicle
import groovy.lang.Tuple2
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
                200L to VehicleEntity(2, 200),
                300L to VehicleEntity(3, 300),
                400L to VehicleEntity(4, 400),
                500L to VehicleEntity(5, 500)
        )
        private val parkingMeterEntries: MutableMap<Long, ParkingMeterEntryEntity> = mutableMapOf()
    }

    fun findParkingMeterEntriesWithVehicle(vehicleBusinessId: Long): Flux<ParkingMeterEntry> {
        val vehicleId = vehicles[vehicleBusinessId]?.id
                ?: throw IllegalArgumentException("Cannot find vehicle with business id $vehicleBusinessId")
        return Flux.fromIterable(parkingMeterEntries.values.filter { it.vehicleId == vehicleId })
                .flatMap { findParkingMeterEntry(it.businessID) }
    }

    fun findParkingMeterEntry(businessID: Long): Mono<ParkingMeterEntry> {
        val parkingMeterEntryEntity = parkingMeterEntries[businessID]
                ?: return Mono.error(IllegalArgumentException("Cannot find parking meter entry with business id $businessID"))

        return mapParkingMeterEntity(parkingMeterEntryEntity)
    }

    private fun mapParkingMeterEntity(parkingMeterEntryEntity: ParkingMeterEntryEntity) =
            Mono.zip(findVehicleById(parkingMeterEntryEntity.vehicleId), findDriverById(parkingMeterEntryEntity.driverId))
                    .map { ParkingMeterMapper.map(parkingMeterEntryEntity, it.t1, it.t2) }

    fun findVehicle(businessID: Long): Mono<Vehicle> {
        val entity = vehicles[businessID]
                ?: return Mono.error(IllegalArgumentException("Cannot find vehicle with business id $businessID"))
        return Mono.just(Vehicle(entity.businessID))
    }

    fun findDriver(businessID: Long): Mono<Driver> {
        val entity = drivers[businessID]
                ?: return Mono.error(IllegalArgumentException("Cannot find driver with business id $businessID"))
        return Mono.just(Driver(entity.disabled, entity.businessID))
    }

    fun saveParkingMeterEntry(parkingMeterEntry: ParkingMeterEntry): Mono<ParkingMeterEntry> {
        val vehicleId = vehicles.values.firstOrNull { it.businessID == parkingMeterEntry.vehicle.businessID }?.id
                ?: return Mono.error(IllegalArgumentException("Cannot find vehicle with id ${parkingMeterEntry.vehicle.businessID}"))
        val driverId = drivers.values.firstOrNull { it.businessID == parkingMeterEntry.driver.businessID }?.id
                ?: return Mono.error(IllegalArgumentException("Cannot find driver with id ${parkingMeterEntry.driver.businessID}"))

        val parkingMeterEntity = ParkingMeterMapper.map(parkingMeterEntry, parkingMeterEntries.size.toLong(), vehicleId, driverId)
        parkingMeterEntries[parkingMeterEntity.businessID] = parkingMeterEntity
        return Mono.just(parkingMeterEntry)
    }

    fun findParkingMeterEntries(day: LocalDate): Flux<ParkingMeterEntry> {
        return Flux.fromIterable(parkingMeterEntries.values
                .filter { entry -> entry.endDate?.toLocalDate() == day })
                .flatMap { mapParkingMeterEntity(it) }
    }

    private fun findVehicleById(id: Long): Mono<Vehicle> {
        val vehicle = vehicles.values.firstOrNull { it.id == id }
                ?: return Mono.error(IllegalArgumentException("Cannot find vehicle with id $id"))
        return Mono.just(Vehicle(vehicle.businessID))
    }

    private fun findDriverById(id: Long): Mono<Driver> {
        val entity = drivers.values.firstOrNull { it.id == id }
                ?: return Mono.error(IllegalArgumentException("Cannot find driver with id $id"))
        return Mono.just(Driver(entity.disabled, entity.businessID))
    }
}