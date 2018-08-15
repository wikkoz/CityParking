package driver.calculator

import domain.Driver
import util.TimerService

class FeeCalculatorFactory(timerService: TimerService) {
    private val regularFeeCalculator = RegularFeeCalculator(timerService)
    private val disabledFeeCalculator = DisabledFeeCalculator(timerService)

    fun create(driver: Driver): FeeCalculator {
        return if (driver.disabled) disabledFeeCalculator else regularFeeCalculator
    }
}