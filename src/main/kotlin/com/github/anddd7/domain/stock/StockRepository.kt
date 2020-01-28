package com.github.anddd7.domain.stock

import com.github.anddd7.domain.Repository
import java.math.BigDecimal

interface StockRepository : Repository {
  fun getStock(productId: Int): BigDecimal
}
