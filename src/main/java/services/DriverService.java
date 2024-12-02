package services;

import java.util.List;

import model.Driver;
import model.Order;

public interface DriverService {
    List<Driver> getAvailableDrivers();
    Driver getDriverForOrder(Order order);
    void assignDriverToOrder(Driver driver, Order order);
    void rateDriver(Driver driver, Integer rating);
}