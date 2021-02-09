package com.github.anddd7.adapter.outbound.stock

import com.github.anddd7.adapter.RepositoryImpl
import com.github.anddd7.domain.stock.StockRepository
import org.springframework.stereotype.Repository
import org.springframework.web.client.RestTemplate
import java.math.BigDecimal

@Repository
class StockClient(private val restTemplate: RestTemplate) : StockRepository, RepositoryImpl {
    override fun getStock(productId: Int): BigDecimal =
        restTemplate.getForObject("/product/$productId/stock", BigDecimal::class.java)
            ?: throw IllegalArgumentException()
}
