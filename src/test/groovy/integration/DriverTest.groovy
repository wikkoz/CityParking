package integration

import database.Repository
import driver.DriverService
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import spock.lang.Shared
import spock.lang.Specification
import test.config.IntegrationTestConfig
import util.TimerService

class DriverTest extends Specification {

    @Shared
    DriverService driverService

    @Shared
    Repository repository

    def setupSpec() {
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
        def parkingId = driverService.startParkingMeter(vehicleId, driverId)

        then:
        def parkingEntry = repository.findParkingMeterEntry(parkingId)
        parkingEntry.vehicle.businessID == vehicleId
        parkingEntry.driver.businessID == driverId
        !parkingEntry.isFinished()
    }

    def "should correctly add parking and stop entry"() {
        given:
        def vehicleId = 200L
        def driverId = 200L

        when:
        def parkingId = driverService.startParkingMeter(vehicleId, driverId)
        def money = driverService.stopParkingMeter(parkingId)
        then:
        def parkingEntry = repository.findParkingMeterEntry(parkingId)
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
        driverService.startParkingMeter(vehicleId, driverId)
        driverService.startParkingMeter(vehicleId, driverId)
        then:
        thrown(IllegalStateException)
    }


}
