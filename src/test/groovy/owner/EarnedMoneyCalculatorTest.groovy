package owner

import domain.money.Currency
import domain.Driver
import domain.ParkingMeterEntry
import domain.Vehicle
import domain.money.Money
import spock.lang.Specification

import java.time.LocalDateTime

class EarnedMoneyCalculatorTest extends Specification {

    EarnedMoneyCalculator calculator = new EarnedMoneyCalculator()
    def vehicle = new Vehicle(0)
    def driver = new Driver(false, 0)

    def "should filter out not finished parkings"() {
        given:
        def parkings = [new ParkingMeterEntry(0, vehicle, driver, LocalDateTime.of(2018, 12, 13, 2, 3), null, null)]

        when:
        def calculatedFee = calculator.calculateEarnedMoney(parkings)

        then:
        Money.@Companion.zero(Currency.PLN) == calculatedFee
    }

    def "should correctly add money for parkings"() {
        given:
        def parkings = [
                new ParkingMeterEntry(0, vehicle, driver, LocalDateTime.of(2018, 12, 13, 2, 3), LocalDateTime.of(2018, 12, 13, 2, 3), Money.@Companion.of(BigDecimal.ONE, Currency.PLN)),
                new ParkingMeterEntry(0, vehicle, driver, LocalDateTime.of(2018, 12, 13, 2, 3), LocalDateTime.of(2018, 12, 13, 2, 3), Money.@Companion.of(BigDecimal.ONE, Currency.PLN))]

        when:
        def calculatedFee = calculator.calculateEarnedMoney(parkings)

        then:
        Money.@Companion.of(BigDecimal.valueOf(2), Currency.PLN) == calculatedFee
    }
}
