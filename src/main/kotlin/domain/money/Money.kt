package domain.money

import java.math.BigDecimal

data class Money private constructor (val amount: BigDecimal, val currency: Currency) {
    companion object {

        fun of(amount: BigDecimal, currency: Currency): Money {
            return Money(amount.setScale(2), currency)
        }

        fun zero(currency: Currency = Currency.PLN): Money {
            return of(BigDecimal.ZERO, currency)
        }
    }

    fun exchange(toCurrency: Currency): Money {
        return Money(amount.multiply(currency.rationToPLN).divide(toCurrency.rationToPLN), toCurrency)
    }

    operator fun plus(money: Money): Money {
        if (currency == money.currency) {
            return Money(amount.plus(money.amount).setScale(2), currency)
        }
        throw IllegalArgumentException("Cannot add money with different currencies $currency and ${money.currency}")
    }

    operator fun times(multiplier: Double): Money {
        return Money(amount.multiply(BigDecimal.valueOf(multiplier)).setScale(2), currency)
    }
}