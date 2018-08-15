package driver

import domain.Vehicle
import domain.money.Money
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

open class DriverHandler(private val driverService: DriverService) {

    fun start(request: ServerRequest): Mono<ServerResponse> {

        return Mono.just(Pair(request.pathVariable("vehicleId").toLong(),
                request.pathVariable("driverId").toLong()))
                .map { (vehicleId, driverId) -> driverService.startParkingMeter(vehicleId, driverId) }
                .flatMap { data ->  ok().syncBody(data) }
    }

    fun stop(request: ServerRequest): Mono<ServerResponse> {
        return Mono.just(request.pathVariable("parkingEntryId").toLong())
                .map { driverService.stopParkingMeter(it) }
                .flatMap { data ->  ok().syncBody(data) }
    }
}