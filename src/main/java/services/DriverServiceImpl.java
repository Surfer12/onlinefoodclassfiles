package services;

import java.util.ArrayList;
import java.util.List;

import model.Driver;
import model.Order;
import model.OrderStatus;
import rating.Rating;

public class DriverServiceImpl implements DriverService {
    private List<Driver> drivers = new ArrayList<>();

    @Override
    public List<Driver> getAvailableDrivers() {
        return this.drivers.stream()
                .filter(Driver::isAvailable)
                .toList();
    }

    @Override
    public Driver getDriverForOrder(Order order) {
        return this.drivers.stream()
                .filter(Driver::isAvailable)
                .findFirst()
                .orElse(null);
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
            ensureMaxRatings(driver);
        } else {
            System.out.println("Driver not found.");
        }
    }

    public void addDriverRating(Driver driver, Rating rating) {
        if (driver != null && rating != null) {
            driver.addRating(rating);
            ensureMaxRatings(driver);
        } else {
            throw new IllegalArgumentException("Invalid driver or rating");
        }
    }

    public void ensureMaxRatings(Driver driver) {
        if (driver != null) {
            while (driver.getRatings().size() > 10) {
                driver.getRatings().remove(0);
            }
        } else {
            throw new IllegalArgumentException("Invalid driver");
        }
    }
}
