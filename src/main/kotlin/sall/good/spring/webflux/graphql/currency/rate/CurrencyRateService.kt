package sall.good.spring.webflux.graphql.currency.rate

import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class CurrencyRateService(
    private val currencyRateClient: WebClient,
) {

    val currencyTypeRefer = object : ParameterizedTypeReference<Map<String, String>>() {}
    val currencyErrorTypeRefer = object : ParameterizedTypeReference<Map<String, String>>() {}

    suspend fun getCurrencies(): ExternalServiceResponse<Map<String, String>, Map<String, String>> {
        return currencyRateClient.get()
            .uri("/gh/fawazahmed0/currency-api@1/latest/currencies.json")
            .exchangeToMono { res ->
                val success = res.statusCode().is2xxSuccessful
                val typeRefer = if (success) currencyTypeRefer else currencyErrorTypeRefer
                res.bodyToMono(typeRefer)
                    .map {
                        ExternalServiceResponse(
                            data = if (success) it else null,
                            error = if (!success) it else null,
                        )
                    }
            }.awaitSingle()
    }
}

@Configuration
class CurrencyRateServiceConfig {

    @Bean
    fun currencyRateClient(
        builder: WebClient.Builder,
    ): WebClient {
        return builder
            .baseUrl("https://cdn.jsdelivr.net")
            .defaultHeaders {
                it.contentType = MediaType.APPLICATION_JSON
            }
            .build()
    }
}


data class ExternalServiceResponse<T, E>(
    val data: T? = null,
    val error: E? = null,
) {

    fun hasError() = error != null
}
