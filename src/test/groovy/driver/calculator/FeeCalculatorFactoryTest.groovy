package driver.calculator

import domain.Driver
import spock.lang.Specification
import util.TimerService

class FeeCalculatorFactoryTest extends Specification {

    FeeCalculatorFactory factory = new FeeCalculatorFactory(Mock(TimerService))

    def "should choose correct calculator"() {
        when:
        def calculator = factory.create(driver)

        then:
        calculator.class == classType

        where:
        driver                  | classType
        new Driver(false, 100)  | RegularFeeCalculator.class
        new Driver(true, 100)   | DisabledFeeCalculator.class
    }
}
