package sall.good.spring.webflux.graphql.currency.rate

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.PersistenceCreator
import org.springframework.data.relational.core.mapping.Table
import sall.good.spring.webflux.graphql.currency.rate.CurrencyRateTable.Companion.TABLE_NAME

@Table(TABLE_NAME)
data class CurrencyRateTable @PersistenceCreator constructor(
  @Id
  var id: String? = null,
) {
  companion object {
    const val TABLE_NAME = "currency_rates"
  }
}
