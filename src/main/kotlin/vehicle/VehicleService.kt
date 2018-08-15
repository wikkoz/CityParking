package vehicle

import database.Repository
import reactor.core.publisher.Mono

class VehicleService(private val repository: Repository) {
    fun onParking(vehicleBusinessId: Long): Mono<Boolean> = repository.findParkingMeterEntriesWithVehicle(vehicleBusinessId)
            .any { !it.isFinished() }
}