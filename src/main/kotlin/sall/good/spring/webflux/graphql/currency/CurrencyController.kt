package sall.good.spring.webflux.graphql.currency

import com.generated.graphql.Currency
import com.generated.graphql.CurrencyInput
import com.generated.graphql.CurrencyRatesInput
import org.slf4j.LoggerFactory
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.ContextValue
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller
import sall.good.spring.webflux.graphql.currency.rate.CurrencyRateService
import java.util.*

@Controller
class CurrencyController(
  private val rateService: CurrencyRateService,
) {

  val logger = LoggerFactory.getLogger(this::class.java)

  @QueryMapping
  suspend fun currencies(
    @ContextValue locale: Locale? = null,
    @Argument input: CurrencyInput? = null,
  ): List<Currency> {
    logger.info("locale, $locale")
    val getCurrencies = rateService.getCurrencies()
    if (getCurrencies.hasError()) throw IllegalStateException("getCurrencies error")
    val currencies = getCurrencies.data?.let {
      it.filter { currency -> if (input?.sourceCurrencyCode == null) true else currency.key == input.sourceCurrencyCode }
        .map { (code, name) ->
          Currency.builder()
            .setCode(code)
            .setName(name)
            .build()
        }
    } ?: emptyList()
    return currencies
  }

  @SchemaMapping(typeName = "Currency", field = "rates")
  suspend fun currencyRates(
    currency: Currency,
    @Argument input: CurrencyRatesInput?,
  ): Map<String, Any?>? {
    val rates = rateService.getCurrencyRate(
      sourceCurrencyCode = currency.code,
      targetCurrencyCode = input?.targetCurrencyCode,
      date = input?.date,
    )
    return rates.data
  }
}
