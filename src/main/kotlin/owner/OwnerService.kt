package owner

import database.Repository
import domain.money.Money
import java.time.LocalDate

class OwnerService(private val repository: Repository) {
    fun earnedMoney(day: LocalDate): Money {
        val calculator = createCalculator()

        val entries = repository.findParkingMeterEntries(day)
        return calculator.calculateEarnedMoney(entries)
    }

    private fun createCalculator(): EarnedMoneyCalculator {
        return EarnedMoneyCalculator()
    }
}