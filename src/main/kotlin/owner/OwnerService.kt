package owner

import database.Repository
import domain.money.Money
import reactor.core.publisher.Mono
import java.time.LocalDate

class OwnerService(private val repository: Repository) {
    fun earnedMoney(day: LocalDate): Mono<Money> {
        val calculator = createCalculator()

        return repository.findParkingMeterEntries(day).collectList()
                .map { calculator.calculateEarnedMoney(it) }
    }

    private fun createCalculator(): EarnedMoneyCalculator {
        return EarnedMoneyCalculator()
    }
}