package com.github.anddd7.adapter.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class WebClientConfig {
  @Bean
  fun restTemplate(downstream: Downstream) =
      RestTemplateBuilder().rootUri(downstream.stock).build()
}

@Configuration
@ConfigurationProperties("downstream")
class Downstream {
  var stock: String = ""
}
