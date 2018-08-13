package driver.calculator


import domain.money.Currency
import domain.money.Money
import spock.lang.Specification
import spock.lang.Unroll
import util.TimerService

import java.time.LocalDateTime

class RegularCalculatorFactoryTest extends Specification {

    TimerService timerService = new TimerService()
    RegularFeeCalculator calculator = new RegularFeeCalculator(timerService)

    @Unroll
    def "should calculate fee: #fee for start date: #startDate end date: #endDate"() {
        when:
        def calculatedFee = calculator.calculateFee(startDate, endDate)

        then:
        Money.@Companion.of(fee, Currency.PLN) == calculatedFee

        where:
        startDate                            | endDate                              | fee
        LocalDateTime.of(2018, 12, 13, 2, 3) | LocalDateTime.of(2018, 12, 13, 2, 4) | BigDecimal.ONE
        LocalDateTime.of(2018, 12, 13, 2, 3) | LocalDateTime.of(2018, 12, 13, 3, 4) | BigDecimal.valueOf(3)
        LocalDateTime.of(2018, 12, 13, 2, 3) | LocalDateTime.of(2018, 12, 13, 4, 4) | BigDecimal.valueOf(4.50)
        LocalDateTime.of(2018, 12, 13, 2, 3) | LocalDateTime.of(2018, 12, 13, 5, 4) | BigDecimal.valueOf(4.5 * 1.5)
    }
}
