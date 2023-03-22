package com.jj.blog.blogsearchclient.client

import com.jj.blog.blogsearchcommon.constant.Constants
import com.jj.blog.blogsearchcommon.exception.RestfulErrorCode
import com.jj.blog.blogsearchcommon.exception.ServiceException
import io.netty.channel.ChannelOption
import io.netty.handler.timeout.ReadTimeoutException
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.http.codec.ClientCodecConfigurer
import org.springframework.util.unit.DataSize
import org.springframework.web.reactive.function.client.*
import reactor.core.publisher.Mono
import reactor.netty.http.client.HttpClient
import reactor.netty.resources.ConnectionProvider
import java.net.ConnectException
import java.net.UnknownHostException
import java.time.Duration
import java.util.function.Consumer

@Configuration
class BlogWebClientConfig {
    @Value("\${bs.client.web.connection-timeout:3000}")
    private val connectionTimeout = 3000

    @Value("\${bs.client.web.read-timeout:10000}")
    private val readTimeout = 10000

    @Value("\${bs.client.web.max-in-memory-size:2MB}")
    private val maxInMemorySize: DataSize = DataSize.ofMegabytes(2L)

    @Value("\${bs.client.web.max-idle-time:60}")
    private val maxIdleTime = 60

    @Value("\${bs.client.web.max-life-time:-1}")
    private val maxLifeTime = -1

    @Bean
    fun webClient(): WebClient {
        val provider = ConnectionProvider.builder("fixed")
            .maxIdleTime(Duration.ofSeconds(maxIdleTime.toLong()))
            .maxLifeTime(Duration.ofSeconds(maxLifeTime.toLong()))
            .build()
        val httpClient = HttpClient.create(provider)
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectionTimeout)
            .responseTimeout(Duration.ofMillis(readTimeout.toLong()))
        val exchangeStrategies = ExchangeStrategies.builder()
            .codecs { configurer: ClientCodecConfigurer ->
                configurer
                    .defaultCodecs()
                    .maxInMemorySize(java.lang.Long.valueOf(maxInMemorySize.toBytes()).toInt())
            }
            .build()

        return WebClient.builder()
            .baseUrl("http://localhost:8080")
            .clientConnector(ReactorClientHttpConnector(httpClient))
            .filter { req: ClientRequest, next: ExchangeFunction ->
                req.headers()
                    .forEach { name: String?, values: List<String?> ->
                        values.forEach(
                            Consumer { value: String? ->
                                log.debug(
                                    "request header [{} : {}]",
                                    name,
                                    value
                                )
                            })
                    }
                val traceId = MDC.get(Constants.MDC_KEY_TRACE_ID)
                val startTime = System.currentTimeMillis()
                traceErq(req.method().toString(), req.url().toString())
                next.exchange(req)
                    .onErrorMap { throwable: Throwable ->
                        if (throwable is ServiceException) {
                            return@onErrorMap throwable
                        } else if (throwable.cause is ConnectException) {
                            return@onErrorMap ServiceException(RestfulErrorCode.REMOTE_SERVER_CONNECTION_ERROR)
                        } else if (throwable.cause is ReadTimeoutException) {
                            return@onErrorMap ServiceException(RestfulErrorCode.REMOTE_SERVER_READ_TIMEOUT_ERROR)
                        } else if (throwable.cause is UnknownHostException) {
                            return@onErrorMap ServiceException(RestfulErrorCode.REMOTE_SERVER_UNKNOWN_HOST_ERROR)
                        } else {
                            return@onErrorMap ServiceException(RestfulErrorCode.INTERNAL_SERVER_ERROR, throwable)
                        }
                    }
                    // handle request exception
                    .doOnError { throwable: Throwable ->
                        var traceStatusCode: Int = RestfulErrorCode.INTERNAL_SERVER_ERROR.status
                        var traceRestfulErrorCode: Int = RestfulErrorCode.INTERNAL_SERVER_ERROR.code
                        val serviceException: ServiceException
                        if (throwable is ServiceException) {
                            serviceException = throwable
                            traceStatusCode = serviceException.restfulErrorCode.status
                            traceRestfulErrorCode = serviceException.restfulErrorCode.code
                        }
                        val endTime = System.currentTimeMillis()
                        traceErs(
                            req.method().toString(),
                            req.url().toString(),
                            traceStatusCode,
                            traceRestfulErrorCode,
                            throwable.message,
                            endTime - startTime
                        )
                    }
                    .doOnNext { response: ClientResponse ->
                        response
                            .headers()
                            .asHttpHeaders()
                            .forEach { name: String?, values: List<String?> ->
                                values.forEach(
                                    Consumer { value: String? ->
                                        log.debug(
                                            "response header [{} : {}]",
                                            name,
                                            value
                                        )
                                    })
                            }
                        val endTime = System.currentTimeMillis()
                        MDC.put(Constants.MDC_KEY_TRACE_ID, traceId)
                        traceErs(
                            req.method().toString(),
                            req.url().toString(),
                            response.statusCode().value(),
                            0,
                            "Success",
                            endTime - startTime
                        )
                    }
            }
            .filter(
                ExchangeFilterFunction.ofResponseProcessor { clientResponse: ClientResponse ->
                    if (clientResponse.statusCode().is4xxClientError
                        || clientResponse.statusCode().is5xxServerError
                    ) {
                        return@ofResponseProcessor clientResponse
                            .bodyToMono(String::class.java)
                            .flatMap<ClientResponse> { body: String? ->
                                Mono.error(
                                    ServiceException(
                                        RestfulErrorCode.REMOTE_SERVER_RESULT_ERROR, body
                                    )
                                )
                            }
                    } else {
                        return@ofResponseProcessor Mono.just(clientResponse)
                    }
                })
            .exchangeStrategies(exchangeStrategies)
            .build()
    }

    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)
        private val traceLog = LoggerFactory.getLogger(Constants.LOGGER_NAME_TRACE)

        private fun traceErq(method: String, url: String) {
            traceLog.info("ERQ {} {}", method, url)
        }

        private fun traceErs(
            method: String,
            url: String,
            statusCode: Int,
            restfulErrorCode: Int,
            message: String?,
            elapsed: Long
        ) {
            traceLog.info(
                "ERS {} {}; {}, {}, {}, {}ms", method, url, statusCode, restfulErrorCode, message, elapsed
            )
        }
    }
}