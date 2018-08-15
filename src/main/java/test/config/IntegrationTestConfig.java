package test.config;

import database.Repository;
import driver.DriverHandler;
import driver.DriverService;
import driver.calculator.FeeCalculatorFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import owner.OwnerHandler;
import owner.OwnerService;
import util.TimerService;
import vehicle.VehicleHandler;
import vehicle.VehicleService;

@Configuration
public class IntegrationTestConfig {


    @Bean
    public TimerService timerService() {
        return new TimerService();
    }

    @Bean
    public VehicleService vehicleService(Repository repository) {
        return new VehicleService(repository);
    }

    @Bean
    public Repository repository() {
        return new Repository();
    }

    @Bean
    public FeeCalculatorFactory feeCalculatorFactory(TimerService timerService) {
        return new FeeCalculatorFactory(timerService);
    }

    @Bean
    public DriverService driverService(TimerService timerService, VehicleService vehicleService,
                                       Repository repository, FeeCalculatorFactory feeCalculatorFactory) {
        return new DriverService(timerService, vehicleService, repository, feeCalculatorFactory);
    }

    @Bean
    public DriverHandler driverHandler(DriverService driverService) {
        return new DriverHandler(driverService);
    }

    @Bean
    public VehicleHandler vehicleHandler(VehicleService vehicleService) {
        return new VehicleHandler(vehicleService);
    }

    @Bean
    public OwnerService ownerService(Repository repository) {
        return new OwnerService(repository);
    }

    @Bean
    public OwnerHandler ownerHandler(OwnerService ownerService) {
        return new OwnerHandler(ownerService);
    }
}
