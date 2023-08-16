package sall.good.spring.webflux.graphql.currency.rate

import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import java.time.LocalDate

@Service
class CurrencyRateService(
    private val currencyRateClient: WebClient,
) {

    suspend fun getCurrencies(): ExternalServiceResponse<Map<String, String>, Map<String, String>> {
        return currencyRateClient.get()
            .uri("/gh/fawazahmed0/currency-api@1/latest/currencies.json")
            .exchangeToMono { res ->
                val success = res.statusCode().is2xxSuccessful
                res.bodyToMono(Any::class.java)
                    .map {
                        ExternalServiceResponse(
                            data = if (success) it as Map<String, String> else null,
                            error = if (!success) it as Map<String, String> else null,
                        )
                    }
            }.awaitSingle()
    }

    suspend fun getCurrencyRate(
        sourceCurrencyCode: String,
        targetCurrencyCode: String? = null,
        date: LocalDate? = null,
    ): ExternalServiceResponse<Map<String, Any?>, Map<String, String>> {
        val dateRequest = date?.let { "$date" } ?: "latest"
        val request =
            targetCurrencyCode?.let { "$sourceCurrencyCode/$targetCurrencyCode.json" } ?: "$sourceCurrencyCode.json"
        return currencyRateClient.get()
            .uri("/gh/fawazahmed0/currency-api@1/$dateRequest/currencies/$request")
            .exchangeToMono { res ->
                val success = res.statusCode().is2xxSuccessful
                res.bodyToMono(Any::class.java)
                    .map {
                        ExternalServiceResponse(
                            data = if (success) it as Map<String, Any?> else null,
                            error = if (!success) it as Map<String, String> else null,
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
