/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import model.Driver;
import model.Order;
import model.OrderStatus;
import services.DriverService;



public class DriverServiceImpl implements DriverService {
    private final List<Driver> drivers = new ArrayList<>();

    @Override
    public List<Driver> getAvailableDrivers() {
        return this.drivers.stream()
                .filter(Driver::isAvailable)
                .toList();
    }

    @Override
    public Optional<Driver> getDriverForOrder(final Order order) {
        return this.drivers.stream()
                .filter(Driver::isAvailable)
                .findFirst();
    }

    @Override
    public void assignDriverToOrder(final Driver driver, final Order order) {
        if (driver != null && order != null) {
            driver.setAvailable(false);
            order.setDriver(Optional.of(driver));
            order.setStatus(OrderStatus.IN_PROGRESS);
        }
    }

    @Override
    public void rateDriver(final Driver driver, final Integer rating) {
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

    @Override
    public Optional<Driver> getDriverById(final Long driverId) {
        return this.drivers.stream()
                .filter(driver -> driver.getId().equals(driverId))
                .findFirst();
    }

    public void processOrder(final Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
        System.out.println("Order processed: " + order.getOrderId());
    }

    // ...additional methods...
}
