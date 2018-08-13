package owner

import domain.ParkingMeterEntry
import domain.money.Money

class EarnedMoneyCalculator {
    fun calculateEarnedMoney(parkingMeterEntries: List<ParkingMeterEntry>): Money {
        return parkingMeterEntries
                .filter { it.isFinished() }
                .map { it.fee }
                .requireNoNulls()
                .fold(Money.zero()) { acc, money -> acc.plus(money)}
    }
}