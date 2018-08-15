package config

import database.Repository
import driver.calculator.FeeCalculatorFactory
import driver.DriverHandler
import driver.DriverService
import org.springframework.context.support.beans
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.server.WebHandler
import owner.OwnerHandler
import owner.OwnerService
import util.TimerService
import vehicle.VehicleHandler
import vehicle.VehicleService

fun beans() = beans {
    bean<TimerService>()
    bean<VehicleService>()
    bean<Repository>()
    bean<FeeCalculatorFactory>()
    bean<DriverService>()
    bean<DriverHandler>()
    bean<VehicleHandler>()
    bean<VehicleService>()
    bean<OwnerService>()
    bean<OwnerHandler>()
    bean<WebHandler>("webHandler") {
        RouterFunctions.toWebHandler(
                routers(ref(), ref(), ref())
        )
    }
}