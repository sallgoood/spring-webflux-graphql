package sall.good.spring.webflux.graphql.currency.rate

import com.infobip.spring.data.r2dbc.QuerydslR2dbcRepository

interface CurrencyRateRepository: QuerydslR2dbcRepository<CurrencyRateTable, String> {
}
