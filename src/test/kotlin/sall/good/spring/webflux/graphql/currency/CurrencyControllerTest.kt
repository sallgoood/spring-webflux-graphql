package sall.good.spring.webflux.graphql.currency

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientAutoConfiguration
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest
import org.springframework.context.annotation.Import
import org.springframework.graphql.test.tester.GraphQlTester
import sall.good.spring.webflux.graphql.currency.rate.CurrencyRateService
import sall.good.spring.webflux.graphql.currency.rate.CurrencyRateServiceConfig

@GraphQlTest(CurrencyController::class)
@Import(CurrencyRateServiceConfig::class, CurrencyRateService::class)
@ImportAutoConfiguration(WebClientAutoConfiguration::class)
class CurrencyControllerTest {

    @Autowired
    lateinit var tester: GraphQlTester

    @Test
    fun shouldGetAllCurrencies() {
        tester.documentName("currencies")
            .execute()
            .path("currencies")
            .hasValue()
    }
}
