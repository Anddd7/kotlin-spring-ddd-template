package com.github.anddd7.domain.product

import com.github.anddd7.domain.Repository

interface ProductRepository : Repository {
  fun findAll(): List<Product>
  fun getOne(id: Int): Product
}
