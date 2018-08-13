package driver.calculator

import domain.money.Money
import java.time.LocalDateTime

interface FeeCalculator {
    fun calculateFee(startDate: LocalDateTime, endDate: LocalDateTime) : Money
}