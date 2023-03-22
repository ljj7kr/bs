package com.jj.blog.blogsearchclient.client

import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Component
class BlogWebClient(
    private val webClient: WebClient
) {

    fun <T> getBlock(returnClazz: Class<T>, baseUrl: String, uri: String): T? {
        return get(returnClazz, baseUrl, uri).block()
    }

    fun <T> getBlock(
        returnClazz: Class<T>,
        baseUrl: String,
        uri: String,
        headers: Map<String, String>
    ): T? {
        return get(returnClazz, baseUrl, uri, headers).block()
    }

    fun <T> get(returnClazz: Class<T>, baseUrl: String, uri: String): Mono<T> {
        return get(returnClazz, baseUrl, uri, null)
    }

    fun <T> get(
        returnClazz: Class<T>,
        baseUrl: String,
        uri: String,
        headers: Map<String, String>?
    ): Mono<T> {
        return webClient
            .mutate()
            .baseUrl(baseUrl)
            .build()
            .get()
            .uri(uri)
            .headers { httpHeaders: HttpHeaders ->
                headers?.forEach { (headerName: String, headerValue: String) ->
                    httpHeaders.add(
                        headerName,
                        headerValue
                    )
                }
            }
            .retrieve()
            .bodyToMono(returnClazz)
    }
}
