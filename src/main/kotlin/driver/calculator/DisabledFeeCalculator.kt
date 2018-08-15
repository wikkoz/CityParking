package driver.calculator

import domain.money.Currency
import domain.money.Money
import util.TimerService
import java.math.BigDecimal
import java.time.LocalDateTime

class DisabledFeeCalculator(private val timerService: TimerService): FeeCalculator {
    companion object {
        val FIRST_HOUR_FEE: Money = Money.of(BigDecimal.ZERO, Currency.PLN)
        val SECOND_HOUR_FEE: Money = Money.of(BigDecimal(2), Currency.PLN)
        const val MULTIPLIER: Double = 1.2
    }

    override fun calculateFee(startDate: LocalDateTime, endDate: LocalDateTime): Money {
        val hoursDiff = timerService.countFullHoursDifference(startDate, endDate)

        return when (hoursDiff){
            1 -> FIRST_HOUR_FEE
            2 -> FIRST_HOUR_FEE + SECOND_HOUR_FEE
            else -> (FIRST_HOUR_FEE + SECOND_HOUR_FEE) * Math.pow(MULTIPLIER, (hoursDiff - 2).toDouble())
        }
    }
}