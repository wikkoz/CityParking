package owner

import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

class OwnerHandler(private val ownerService: OwnerService) {
    fun earn(request: ServerRequest): Mono<ServerResponse> {
        return request.bodyToMono(OwnerEarnDto::class.java)
                .map { it.date }
                .map { ownerService.earnedMoney(it) }
                .flatMap { data -> ServerResponse.ok().syncBody(data) }
    }
}