package vehicle

import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

class VehicleHandler(private val vehicleService: VehicleService) {
    fun onParkingMeter(request: ServerRequest): Mono<ServerResponse> {
        return Mono.justOrEmpty(request.pathVariable("vehicleId").toLong())
                .flatMap { vehicleId -> vehicleService.onParking(vehicleId) }
                .flatMap { data -> ServerResponse.ok().syncBody(data) }
                .onErrorResume { data -> ServerResponse.status(500).syncBody(data.message) }
    }
}