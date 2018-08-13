package vehicle

import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

class VehicleHandler(private val vehicleService: VehicleService) {
    fun onParkingMeter(request: ServerRequest): Mono<ServerResponse> {
        val onParkingMeter = Mono.justOrEmpty(request.pathVariable("vehicleId").toLong())
                .map{vehicleId -> vehicleService.onParkingMeter(vehicleId)}

        return ServerResponse.ok().body(onParkingMeter, Boolean::class.java)
    }
}