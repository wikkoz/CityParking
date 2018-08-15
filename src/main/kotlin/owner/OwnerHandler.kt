package owner

import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import reactor.core.publisher.onErrorMap

class OwnerHandler(private val ownerService: OwnerService) {
    fun earn(request: ServerRequest): Mono<ServerResponse> {
        return request.bodyToMono(OwnerEarnDto::class.java)
                .map { it.date }
                .flatMap { ownerService.earnedMoney(it) }
                .flatMap { data -> ServerResponse.ok().syncBody(data) }
                .onErrorResume {data -> ServerResponse.status(500).syncBody(data.message)}
    }
}