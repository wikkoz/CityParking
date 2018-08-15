package config

import driver.DriverHandler
import org.springframework.web.reactive.function.server.router
import owner.OwnerHandler
import vehicle.VehicleHandler

fun routers(driverHandler: DriverHandler, vehicleHandler: VehicleHandler, ownerHandler: OwnerHandler) = router {
    "/api".nest {
        "/driver".nest {
            POST("/start/{vehicleId}/{driverId}", driverHandler::start)
            POST("/stop/{parkingEntryId}", driverHandler::stop)
        }
        "/vehicle".nest {
            POST("/parking/{vehicleId}", vehicleHandler::onParkingMeter)
        }
        "/owner".nest {
            POST("/earned", ownerHandler::earn)

        }
    }
}