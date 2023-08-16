package sall.good.spring.webflux.graphql.currency

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest
import org.springframework.graphql.test.tester.GraphQlTester

@GraphQlTest(CurrencyController::class)
class CurrencyControllerTest {

    @Autowired
    lateinit var tester: GraphQlTester

    @Test
    fun shouldGetAllCurrencies() {
        tester.documentName("currencies")
            .execute()
            .path("currencies")
            .matchesJson("""
                []
            """)
    }
}
