package sall.good.spring.webflux.graphql

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain

@Configuration
class SecurityConfig {
    @Bean
    fun filterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http
            .csrf { it.disable() }
            .authorizeExchange {
                it.anyExchange().permitAll()
            }
            .build()
    }
}
