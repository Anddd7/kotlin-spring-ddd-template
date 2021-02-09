package com.github.anddd7.domain.product

class ProductService(
    private val productRepository: ProductRepository
) {
    fun findAll(): List<Product> = productRepository.findAll()
    fun getOne(id: Int): Product = productRepository.getOne(id)
}
