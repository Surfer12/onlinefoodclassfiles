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
import services.DriverService;

public class DriverServiceImpl implements DriverService {
    private final List<Driver> drivers = new ArrayList<>();

    public DriverServiceImpl() {
        // Initialize with some default drivers
        this.drivers.add(new Driver(1L, "John Smith", "Sedan", "ABC123"));
        this.drivers.add(new Driver(2L, "Sarah Johnson", "SUV", "XYZ789"));
        this.drivers.add(new Driver(3L, "Michael Brown", "Sedan", "DEF456"));
        this.drivers.add(new Driver(4L, "Emily Davis", "Hybrid", "GHI789"));
    }

    @Override
    public List<Driver> getAvailableDrivers() {
        return this.drivers.stream()
                .filter(Driver::isAvailable)
                .sorted((d1, d2) -> {
                    // First, compare by number of active orders (ascending)
                    int orderComparison = Integer.compare(d1.getActiveOrderCount(), d2.getActiveOrderCount());
                    if (orderComparison != 0) {
                        return orderComparison;
                    }
                    // If same number of orders, compare by average rating (descending)
                    return Double.compare(d2.getAverageRating(), d1.getAverageRating());
                })
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public Optional<Driver> getDriverForOrder(final Order order) {
        return order.getDriver();
    }

    @Override
    public void assignDriverToOrder(final Driver driver, final Order order) {
        if (driver == null || order == null) {
            throw new IllegalArgumentException("Driver and order cannot be null");
        }
        driver.setAvailable(false);
        driver.incrementActiveOrderCount();
        order.setDriver(Optional.of(driver));
    }

    @Override
    public void rateDriver(final Driver driver, final Integer rating) {
        if (driver == null || rating == null) {
            throw new IllegalArgumentException("Driver and rating cannot be null");
        }
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        driver.addRating(rating);
    }

    @Override
    public Optional<Driver> getDriverById(final Long driverId) {
        if (driverId == null) {
            throw new IllegalArgumentException("Driver ID cannot be null");
        }
        return this.drivers.stream()
                .filter(d -> d.getId().equals(driverId))
                .findFirst();
    }

    @Override
    public List<Driver> getAllDrivers() {
        return new ArrayList<>(this.drivers);
    }

    @Override
    public void addDriver(final Driver driver) {
        if (driver == null) {
            throw new IllegalArgumentException("Driver cannot be null");
        }
        this.drivers.add(driver);
    }

    @Override
    public boolean removeDriver(final Long driverId) {
        if (driverId == null) {
            throw new IllegalArgumentException("Driver ID cannot be null");
        }
        return this.drivers.removeIf(d -> d.getId().equals(driverId));
    }

    @Override
    public void updateDriverInfo(Long driverId, String newVehicle, boolean available) {
        Optional<Driver> driverOpt = getDriverById(driverId);
        if (driverOpt.isPresent()) {
            Driver driver = driverOpt.get();
            if (newVehicle != null && !newVehicle.trim().isEmpty()) {
                driver.setVehicle(newVehicle);
            }
            driver.setAvailable(available);
        } else {
            throw new IllegalArgumentException("Driver not found");
        }
    }

    @Override
    public void deleteDriver(Long driverId) {
        if (driverId == null) {
            throw new IllegalArgumentException("Driver ID cannot be null");
        }
        this.drivers.removeIf(d -> d.getId().equals(driverId));
    }

    @Override
    public void viewDriverRatings(Long driverId) {
        Optional<Driver> driverOpt = getDriverById(driverId);
        if (driverOpt.isPresent()) {
            Driver driver = driverOpt.get();
            System.out.println("Driver: " + driver.getName());
            System.out.println("All ratings: " + driver.getRatings());
            System.out.println("Average rating: " + String.format("%.1f", driver.getAverageRating()));
            System.out.println("Total ratings: " + driver.getRatings().size());
        } else {
            throw new IllegalArgumentException("Driver not found");
        }
    }
}
