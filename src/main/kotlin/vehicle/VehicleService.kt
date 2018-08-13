package vehicle

import database.Repository

class VehicleService(private val repository: Repository) {
    fun onParkingMeter(vehicleBusinessId: Long) = repository.findParkingMeterEntriesWithVehicle(vehicleBusinessId)
            .any { !it.isFinished() }
}