package sall.good.spring.webflux.graphql.currency

import com.generated.graphql.Currency
import org.slf4j.LoggerFactory
import org.springframework.graphql.data.method.annotation.ContextValue
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import sall.good.spring.webflux.graphql.currency.rate.CurrencyRateService
import java.util.Locale

@Controller
class CurrencyController(
    private val rateService: CurrencyRateService,
) {

    val logger = LoggerFactory.getLogger(this::class.java)

    @QueryMapping
    suspend fun currencies(
        @ContextValue locale: Locale? = null,
    ): List<Currency> {
        logger.info("locale, $locale")
        val getCurrencies = rateService.getCurrencies()
        if (getCurrencies.hasError()) throw IllegalStateException("getCurrencies error")
        val currencies = getCurrencies.data?.let {
            it.map { (code, name) ->
                val rates = rateService.getCurrencyRate(code)
                Currency.builder()
                    .withCode(code)
                    .withName(name)
                    .withRates(rates.data)
                    .build()
            }
        } ?: emptyList()
        return currencies
    }
}
