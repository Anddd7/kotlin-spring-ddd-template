package com.github.anddd7.adapter.outbound.product

import com.github.anddd7.adapter.RepositoryImpl
import com.github.anddd7.adapter.outbound.product.po.ProductPO
import com.github.anddd7.adapter.outbound.product.po.toProduct
import com.github.anddd7.domain.product.Product
import com.github.anddd7.domain.product.ProductRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository

@Repository
class ProductRepositoryImpl(private val productDAO: ProductDAO) : ProductRepository, RepositoryImpl {
  override fun findAll(): List<Product> = productDAO.findAll().map(ProductPO::toProduct)
  override fun getOne(id: Int): Product = productDAO.getOne(id).toProduct()
}

@Component
interface ProductDAO : JpaRepository<ProductPO, Int>
