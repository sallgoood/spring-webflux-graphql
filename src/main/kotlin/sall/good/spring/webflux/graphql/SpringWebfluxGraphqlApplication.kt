package sall.good.spring.webflux.graphql

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain
import reactor.core.publisher.Hooks


@SpringBootApplication
class SpringWebfluxGraphqlApplication

fun main(args: Array<String>) {
    runApplication<SpringWebfluxGraphqlApplication>(*args).also {
        Hooks.enableAutomaticContextPropagation()
    }
}
