package driver

import database.Repository
import domain.Driver
import domain.ParkingMeterEntry
import domain.Vehicle
import domain.money.Money
import driver.calculator.FeeCalculatorFactory
import reactor.core.publisher.Mono
import reactor.util.function.Tuple3
import util.BusinessIdGenerator
import util.TimerService
import vehicle.VehicleService

class DriverService(private val timerService: TimerService, private val vehicleService: VehicleService,
                    private val repository: Repository, private val feeCalculatorFactory: FeeCalculatorFactory) {

    fun startParkingMeter(vehicleId: Long, driverId: Long): Mono<Long> {
        val vehicle = repository.findVehicle(vehicleId)
        val driver = repository.findDriver(driverId)

        return Mono.zip(vehicleService.onParking(vehicleId), vehicle, driver)
                .flatMap{startParkingEntry(it)}
                .map { it.businessID }


    }

    fun stopParkingMeter(businessId: Long): Mono<Money> {
        val endTime = timerService.now()
        return repository.findParkingMeterEntry(businessId)
                .map {
                    val calculator = feeCalculatorFactory.create(it.driver)
                    val money = calculator.calculateFee(it.startTime, endTime)
                    it.stopParkingMeter(endTime, money)
                }
                .flatMap { repository.saveParkingMeterEntry(it) }
                .map { it.fee }
    }

    private fun startParkingEntry(tuple: Tuple3<Boolean, Vehicle, Driver>): Mono<ParkingMeterEntry> {
        val vehicle = tuple.t2
        val driver = tuple.t3
        val yetOnParking = tuple.t1
        if (yetOnParking) {
            return Mono.error(IllegalStateException("Vehicle with business id:" +
                    " ${vehicle.businessID}} is yet on parking meter"))
        }

        val businessId = BusinessIdGenerator.generate()
        val parkingMeterEntry = ParkingMeterEntry(businessId, vehicle, driver, timerService.now())
        return repository.saveParkingMeterEntry(parkingMeterEntry)
    }
}