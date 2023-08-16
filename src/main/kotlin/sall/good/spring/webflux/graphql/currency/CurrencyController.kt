package sall.good.spring.webflux.graphql.currency

import com.generated.graphql.Currency
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import sall.good.spring.webflux.graphql.currency.rate.CurrencyRateService

@Controller
class CurrencyController(
    private val rateService: CurrencyRateService,
) {

    @QueryMapping
    suspend fun currencies(): List<Currency> {
        val getCurrencies = rateService.getCurrencies()
        if (getCurrencies.hasError()) throw IllegalStateException("getCurrencies error")
        val currencies = getCurrencies.data?.let {
            it.map { (code, name) ->
                Currency.builder()
                    .withCode(code)
                    .withName(name)
                    .build()
            }
        } ?: emptyList()
        return currencies
    }
}
