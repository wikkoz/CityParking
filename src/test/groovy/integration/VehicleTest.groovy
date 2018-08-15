package integration

import database.Repository
import driver.DriverService
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import spock.lang.Shared
import spock.lang.Specification
import test.config.IntegrationTestConfig
import vehicle.VehicleService

class VehicleTest extends Specification {

    @Shared
    VehicleService vehicleService

    @Shared
    DriverService driverService

    @Shared
    Repository repository

    def setupSpec() {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(IntegrationTestConfig.class)
        ctx.refresh()
        vehicleService = ctx.getBean(VehicleService.class)
        driverService = ctx.getBean(DriverService.class)
        repository = ctx.getBean(Repository.class)
    }

    def "should correctly check if car is on parking"() {
        given:
        def vehicleId = 100L
        def driverId = 200L

        when:
        def onParking = vehicleService.onParking(vehicleId).block()

        then:
        !onParking

        when:
        driverService.startParkingMeter(vehicleId, driverId).block()
        def onParkingAfterStop = vehicleService.onParking(vehicleId).block()

        then:
        onParkingAfterStop
    }
}
