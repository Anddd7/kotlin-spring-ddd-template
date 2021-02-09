package com.github.anddd7

import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@Suppress("FunctionName")
@EnableApiTest
@Sql("classpath:fixture/product_api.sql")
internal class ProductControllerApiTest {

    @Autowired
    private lateinit var mvc: MockMvc

    @Test
    internal fun `should find all products`() {
        mvc.get("/product").andExpect {
            status { isOk() }
        }
    }

    @Test
    internal fun `should get the product by id`() {
        stubFor(
            get("/stock/product/1/stock")
                .willReturn(WireMock.okJson("9999.99"))
        )

        mvc.get("/product/1").andExpect {
            status { isOk() }
        }
    }

    @Test
    internal fun `should get stock of product`() {
        mvc.get("/product/1/stock").andExpect {
            status { isOk() }
        }
    }
}
