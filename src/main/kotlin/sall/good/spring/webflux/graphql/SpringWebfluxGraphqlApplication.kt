package sall.good.spring.webflux.graphql

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import reactor.core.publisher.Hooks

@SpringBootApplication
class SpringWebfluxGraphqlApplication

fun main(args: Array<String>) {
  runApplication<SpringWebfluxGraphqlApplication>(*args).also {
    Hooks.enableAutomaticContextPropagation()
  }
}
