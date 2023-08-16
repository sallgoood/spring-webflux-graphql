package sall.good.spring.webflux.graphql.currency

import com.generated.graphql.Currency
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class CurrencyController {

    @QueryMapping
    suspend fun currencies(): List<Currency> {
        return emptyList()
    }
}
