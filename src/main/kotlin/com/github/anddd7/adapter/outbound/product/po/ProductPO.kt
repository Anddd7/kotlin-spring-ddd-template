package com.github.anddd7.adapter.outbound.product.po

import com.github.anddd7.adapter.PersistObject
import com.github.anddd7.domain.product.Product
import java.math.BigDecimal
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "products")
data class ProductPO(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    val name: String,
    val price: BigDecimal
) : PersistObject

fun ProductPO.toProduct() = Product(id, name, price)
