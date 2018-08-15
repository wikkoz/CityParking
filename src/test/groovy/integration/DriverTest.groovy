package integration

import database.Repository
import driver.DriverService
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import reactor.core.publisher.Mono
import spock.lang.Shared
import spock.lang.Specification
import test.config.IntegrationTestConfig
import util.TimerService

class DriverTest extends Specification {

    @Shared
    DriverService driverService

    @Shared
    Repository repository

    def setup() {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(IntegrationTestConfig.class)
        ctx.refresh()
        driverService = ctx.getBean(DriverService.class)
        repository = ctx.getBean(Repository.class)
    }

    def "should correctly add parking entry"() {
        given:
        def vehicleId = 100L
        def driverId = 200L

        when:
        def parkingId = driverService.startParkingMeter(vehicleId, driverId).block()

        then:
        def parkingEntry = repository.findParkingMeterEntry(parkingId).block()
        parkingEntry.vehicle.businessID == vehicleId
        parkingEntry.driver.businessID == driverId
        !parkingEntry.isFinished()
    }

    def "should correctly add parking and stop entry"() {
        given:
        def vehicleId = 200L
        def driverId = 200L

        when:
        def parkingId = driverService.startParkingMeter(vehicleId, driverId).block()
        def money = driverService.stopParkingMeter(parkingId).block()
        then:
        def parkingEntry = repository.findParkingMeterEntry(parkingId).block()
        parkingEntry.vehicle.businessID == vehicleId
        parkingEntry.driver.businessID == driverId
        parkingEntry.isFinished()
        money != null
    }

    def "should not allow start parking on the same car more than time"() {
        given:
        def vehicleId = 300L
        def driverId = 200L

        when:
        driverService.startParkingMeter(vehicleId, driverId).block()
        driverService.startParkingMeter(vehicleId, driverId).block()
        then:
        thrown(IllegalStateException)
    }


}
