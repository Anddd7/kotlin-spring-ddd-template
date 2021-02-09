package com.github.anddd7.adapter.inbound.product

import com.github.anddd7.Application
import com.github.anddd7.application.product.ProductUserCase
import com.github.anddd7.application.product.dto.ProductStockDTO
import com.github.anddd7.domain.product.Product
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import java.math.BigDecimal

@ContextConfiguration(classes = [Application::class])
@WebMvcTest(
    value = [ProductController::class],
    excludeAutoConfiguration = [SecurityAutoConfiguration::class]
)
@AutoConfigureWebMvc
internal class ProductControllerTest {

    @Autowired
    private lateinit var mvc: MockMvc

    @MockkBean
    private lateinit var productUserCase: ProductUserCase

    @Test
    internal fun `should find all products`() {
        val expect = Product(name = "test", price = BigDecimal.ONE)
        every { productUserCase.findAll() } returns listOf(expect)

        mvc.get("/product").andExpect {
            status { isOk() }
            jsonPath("$.length()", equalTo(1))
            jsonPath("$[0].name", equalTo(expect.name))
        }
    }

    @Test
    internal fun `should get the product by id`() {
        val expect = ProductStockDTO(name = "test", price = BigDecimal.ONE, stock = BigDecimal.ONE)
        every { productUserCase.getProductStock(any()) } returns expect

        mvc.get("/product/1").andExpect {
            status { isOk() }
            jsonPath("$.name", equalTo(expect.name))
        }
    }

    @Test
    internal fun `should get stock of product`() {
        mvc.get("/product/1/stock").andExpect {
            status { isOk() }
        }
    }
}
