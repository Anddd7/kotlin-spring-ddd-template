package com.github.anddd7.application.product

import com.github.anddd7.domain.product.Product
import com.github.anddd7.domain.product.ProductRepository
import com.github.anddd7.domain.stock.StockRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class ProductUserCaseTest {
  @MockK
  private lateinit var productRepository: ProductRepository
  @MockK
  private lateinit var stockRepository: StockRepository
  @InjectMockKs
  private lateinit var productUserCase: ProductUserCase

  @Test
  fun `should find all products from db`() {
    val expect = mockk<Product>()
    every { productRepository.findAll() } returns listOf(expect)

    val result = productUserCase.findAll()

    assertThat(result).hasSize(1)

    verify(exactly = 1) { productRepository.findAll() }
  }

  @Test
  fun `should get product and stock info together`() {
    every { productRepository.getOne(any()) } returns mockk(relaxed = true)
    every { stockRepository.getStock(any()) } returns mockk()

    productUserCase.getProductStock(1)

    verify(exactly = 1) {
      productRepository.getOne(any())
      stockRepository.getStock(any())
    }
  }
}
