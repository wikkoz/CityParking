package domain.money

import java.math.BigDecimal

enum class Currency(val rationToPLN: BigDecimal) {
    PLN(BigDecimal.ONE);
}