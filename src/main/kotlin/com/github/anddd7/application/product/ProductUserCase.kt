package com.github.anddd7.application.product

import com.github.anddd7.application.UserCase
import com.github.anddd7.application.product.dto.ProductStockDTO
import com.github.anddd7.domain.product.Product
import com.github.anddd7.domain.product.ProductRepository
import com.github.anddd7.domain.product.ProductService
import com.github.anddd7.domain.stock.StockRepository
import org.springframework.stereotype.Service

@Service
class ProductUserCase(
    productRepository: ProductRepository,
    private val stockRepository: StockRepository
) : UserCase {
  private val productService: ProductService = ProductService(productRepository)

  fun findAll(): List<Product> = productService.findAll()
  fun getProductStock(id: Int): ProductStockDTO {
    val product = productService.getOne(id)
    val stock = stockRepository.getStock(id)

    return ProductStockDTO(product, stock)
  }
}
