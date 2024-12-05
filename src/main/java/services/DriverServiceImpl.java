package services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import model.Driver;
import model.Order;
import model.OrderStatus;

public class DriverServiceImpl implements DriverService {
    private List<Driver> drivers = new ArrayList<>();

    @Override
    public List<Driver> getAvailableDrivers() {
        return this.drivers.stream()
                .filter(Driver::isAvailable)
                .toList();
    }

    @Override
    public Optional<Driver> getDriverForOrder(Order order) {
        return this.drivers.stream()
                .filter(Driver::isAvailable)
                .findFirst();
    }

    @Override
    public void assignDriverToOrder(Driver driver, Order order) {
        if (driver != null && order != null) {
            driver.setAvailable(false);
            order.setDriver(driver);
            // Optionally update order status
            order.setStatus(OrderStatus.IN_PROGRESS); // Ensure OrderStatus.IN_PROGRESS exists
        }
    }

    @Override
    public void rateDriver(Driver driver, Integer rating) {
        if (driver != null) {
            if (rating < 1 || rating > 5) {
                System.out.println("Rating must be between 1 and 5.");
                return;
            }
            driver.addRating(rating);
        } else {
            System.out.println("Driver not found.");
        }
    }

    public void processOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
        System.out.println("Order processed: " + order.getOrderId());
    }

    @Override
    public List<Driver> getAllDrivers() {
        return new ArrayList<>(this.drivers);
    }

    @Override
    public Optional<Driver> getDriverById(Long driverId) {
        return this.drivers.stream()
                .filter(driver -> driver.getId().equals(driverId))
                .findFirst();
    }
}
