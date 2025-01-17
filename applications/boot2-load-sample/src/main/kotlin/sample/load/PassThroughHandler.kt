package sample.load

import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.BodyInserters.fromValue
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyToMono
import reactor.core.publisher.Mono

class PassThroughHandler(private val webClient: WebClient) {

    fun handle(serverRequest: ServerRequest): Mono<ServerResponse> {
        val messageMono = serverRequest.bodyToMono<Message>()

        return messageMono.flatMap { message ->
            passThrough(message)
                .flatMap { messageAck ->
                    ServerResponse.ok().body(fromValue(messageAck))
                }
        }
    }

    private fun passThrough(message: Message): Mono<MessageAck> {
        return webClient.post()
            .uri("/messages")
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
            .body(fromValue(message))
            .exchangeToMono { response: ClientResponse ->
                response.bodyToMono()
            }
    }
}