package com.github.anddd7

import com.github.anddd7.adapter.config.Downstream
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.options
import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension

@Profile("test")
@SpringBootApplication
internal class TestApplication {
    @Bean
    fun wireMock(): WireMockServer {
        val mockServer = WireMockServer(options().dynamicPort())
        mockServer.start()
        WireMock.configureFor("localhost", mockServer.port())
        return mockServer
    }

    @Bean
    @Primary
    fun downstream(mockServer: WireMockServer) =
        Downstream().apply {
            stock = mockServer.url("stock")
        }
}

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@ExtendWith(SpringExtension::class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = [TestApplication::class]
)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@AutoConfigureEmbeddedDatabase
@ActiveProfiles("test")
annotation class EnableApiTest
