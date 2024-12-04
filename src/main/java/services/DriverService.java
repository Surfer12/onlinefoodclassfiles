package services;

import java.util.List;
import java.util.Optional;

import model.Driver;
import model.Order;

public interface DriverService {
    List<Driver> getAvailableDrivers();
    Optional<Driver> getDriverForOrder(Order order);
    void assignDriverToOrder(Driver driver, Order order);
    void rateDriver(Driver driver, Integer rating);
    Optional<Driver> getDriverById(Long driverId);
}
