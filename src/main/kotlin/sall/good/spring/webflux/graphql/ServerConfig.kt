package sall.good.spring.webflux.graphql

import graphql.ExecutionInput
import org.slf4j.LoggerFactory
import org.springframework.graphql.server.WebGraphQlInterceptor
import org.springframework.graphql.server.WebGraphQlRequest
import org.springframework.graphql.server.WebGraphQlResponse
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.util.*


@Component
class RequestHeaderInterceptor : WebGraphQlInterceptor {

    val logger = LoggerFactory.getLogger(this::class.java)

    override fun intercept(request: WebGraphQlRequest, chain: WebGraphQlInterceptor.Chain): Mono<WebGraphQlResponse> {
        request.configureExecutionInput { executionInput: ExecutionInput?, builder: ExecutionInput.Builder ->
            logger.info("${executionInput?.operationName}, ${executionInput?.variables}")
            builder.graphQLContext(
                mapOf(
                    "locale" to executionInput?.locale,
                )
            ).build()
        }
        return chain.next(request)
    }
}
