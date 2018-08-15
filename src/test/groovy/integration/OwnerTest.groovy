package integration

import database.Repository
import domain.money.Currency
import domain.money.Money
import driver.DriverService
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import owner.OwnerService
import spock.lang.Shared
import spock.lang.Specification
import test.config.IntegrationTestConfig
import vehicle.VehicleService

import java.time.LocalDate

class OwnerTest extends Specification{


    @Shared
    OwnerService ownerService

    @Shared
    DriverService driverService

    @Shared
    Repository repository


    def setup() {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(IntegrationTestConfig.class)
        ctx.refresh()
        ownerService = ctx.getBean(OwnerService.class)
        driverService = ctx.getBean(DriverService.class)
        repository = ctx.getBean(Repository.class)
    }

    def "should return 0 if there is no parking on given day"() {
        given:
        clearDb()

        when:
        def money = ownerService.earnedMoney(LocalDate.of(2015, 12, 21))

        then:
        money.block() == Money.@Companion.zero(Currency.PLN)
    }

    def "should not count not finished parkings"() {
        given:
        clearDb()
        driverService.startParkingMeter(500, 200).block()

        when:
        def money = ownerService.earnedMoney(LocalDate.now())

        then:
        money.block() == Money.@Companion.zero(Currency.PLN)
    }

    def "should count finished parkings"() {
        given:
        clearDb()
        def businessId = driverService.startParkingMeter(400, 200).block()
        driverService.stopParkingMeter(businessId).block()

        when:
        def money = ownerService.earnedMoney(LocalDate.now())

        then:
        money.block() != Money.@Companion.zero(Currency.PLN)
    }


    def clearDb() {
        Repository.parkingMeterEntries.clear()
    }
}
