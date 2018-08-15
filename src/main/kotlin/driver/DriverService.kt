package driver

import database.Repository
import domain.ParkingMeterEntry
import domain.money.Money
import driver.calculator.FeeCalculatorFactory
import util.BusinessIdGenerator
import util.TimerService
import vehicle.VehicleService

class DriverService(private val timerService: TimerService, private val vehicleService: VehicleService,
                    private val repository: Repository, private val feeCalculatorFactory: FeeCalculatorFactory) {

    fun startParkingMeter(vehicleId: Long, driverId: Long): Long {
        val vehicle = repository.findVehicle(vehicleId)
        val driver = repository.findDriver(driverId)

        if (vehicleService.onParkingMeter(vehicleId)) {
            throw IllegalStateException("Vehicle with business id: ${vehicle.businessID} is yet on parking meter")
        }

        val businessId = BusinessIdGenerator.generate()
        val parkingMeterEntry = ParkingMeterEntry(businessId, vehicle, driver, timerService.now())
        repository.saveParkingMeterEntry(parkingMeterEntry)

        return parkingMeterEntry.businessID
    }

    fun stopParkingMeter(businessId: Long): Money {
        val endTime = timerService.now()
        val parkingMeterEntry = repository.findParkingMeterEntry(businessId)

        val calculator = feeCalculatorFactory.create(parkingMeterEntry.driver)
        val money = calculator.calculateFee(parkingMeterEntry.startTime, endTime)

        val setParkingMeterEntry = parkingMeterEntry.stopParkingMeter(endTime, money)
        repository.saveParkingMeterEntry(setParkingMeterEntry)

        return money
    }
}