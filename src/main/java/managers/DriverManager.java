package managers;

import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.Driver;
import model.Order;
import rating.Rating;
import services.DriverService;
import services.impl.DriverServiceImpl;
import validation.ConsoleInputHandler;
import validation.InputValidatorImpl;
import validation.MenuItemValidator;

public class DriverManager {
    private static final Logger logger = Logger.getLogger(DriverManager.class.getName());

    private final DriverService driverService;
    private final ConsoleInputHandler<Integer> menuChoiceHandler;

    public DriverManager() {
        this.driverService = new DriverServiceImpl();
        this.menuChoiceHandler = new ConsoleInputHandler<>(
                new InputValidatorImpl<>(
                        new MenuItemValidator(3),
                        "Driver Management Choice",
                        "Invalid choice"));
    }

    public void listAvailableDrivers() {
        final List<Driver> availableDrivers = this.driverService.getAvailableDrivers();
        System.out.println("\n--- Available Drivers ---");
        for (final Driver driver : availableDrivers) {
            System.out.println(driver.getName() + " - " + driver.getVehicle());
        }
    }

    public void assignDriverToOrder(final Scanner scanner, final Order order, final ConsoleInputHandler<Long> orderIdHandler) {
        final List<Driver> availableDrivers = this.driverService.getAvailableDrivers();
        if (availableDrivers.isEmpty()) {
            System.out.println("No available drivers at the moment.");
            return;
        }

        System.out.println("\n--- Available Drivers ---");
        for (int i = 0; i < availableDrivers.size(); i++) {
            final Driver driver = availableDrivers.get(i);
            System.out.printf("%d. %s - %s\n", i + 1, driver.getName(), driver.getVehicle());
        }

        final Integer driverChoice = this.menuChoiceHandler.handleInput(
                scanner,
                "Select a driver (enter number): ");

        if (driverChoice == null || driverChoice < 1 || driverChoice > availableDrivers.size())
            return;

        final Driver selectedDriver = availableDrivers.get(driverChoice - 1);

        if (order != null) {
            this.driverService.assignDriverToOrder(selectedDriver, order);
            System.out.println("Driver assigned successfully.");
            DriverManager.logger.log(Level.INFO, "Driver {0} assigned to order {1}", new Object[]{selectedDriver.getName(), order.getOrderId()});
        } else {
            System.out.println("Order not found.");
        }
    }

    public void rateDriver(final Scanner scanner, final Order order, final ConsoleInputHandler<Integer> menuChoiceHandler) {
        final Driver driver = this.driverService.getDriverForOrder(order);
        if (driver == null) {
            System.out.println("No driver assigned to this order.");
            return;
        }

        final Integer rating = menuChoiceHandler.handleInput(
                scanner,
                "Rate the driver (1-5 stars): ",
                input -> input >= 1 && input <= 5);

        if (rating != null) {
            this.driverService.rateDriver(driver, rating);
            System.out.println("Thank you for your feedback!");
            DriverManager.logger.info("Driver " + driver.getName() + " rated: " + rating + " stars");
        }
    }

    public void addDriverRating(final Driver driver, final Rating rating) {
        if (driver != null && rating != null) {
            driver.addRating(rating);
            System.out.println("Rating added successfully.");
            DriverManager.logger.log(Level.INFO, "Rating added for driver {0}", driver.getName());
        } else {
            System.out.println("Invalid driver or rating.");
        }
    }

    public void ensureMaxRatings(final Driver driver) {
        if (driver != null) {
            while (driver.getRatings().size() > 10) {
                driver.getRatings().remove(0);
            }
            System.out.println("Ensured maximum of 10 ratings for driver " + driver.getName());
            DriverManager.logger.log(Level.INFO, "Ensured maximum of 10 ratings for driver {0}", driver.getName());
        } else {
            System.out.println("Invalid driver.");
        }
    }

    public ConsoleInputHandler<Integer> getMenuChoiceHandler() {
        return this.menuChoiceHandler;
    }

    public void manageDriverMenu(final Scanner scanner, final OrderManager orderManager) {
        while (true) {
            System.out.println("\n--- Driver Management ---");
            System.out.println("1. View Available Drivers");
            System.out.println("2. Assign Driver to Order");
            System.out.println("3. Return to Main Menu");

            final Integer choice = this.menuChoiceHandler.handleInput(
                    scanner,
                    "Enter your choice: ");

            if (choice == null)
                return;

            switch (choice) {
                case 1 -> this.listAvailableDrivers();
                case 2 -> this.assignDriverToOrderInteractive(scanner, orderManager);
                case 3 -> {
                    return; // Return to main menu
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void assignDriverToOrderInteractive(final Scanner scanner, final OrderManager orderManager) {
        final Long orderId = orderManager.getOrderIdHandler().handleInput(
                scanner,
                "Enter Order ID to assign driver: ");

        if (orderId == null)
            return;

        final Order order = orderManager.getOrderService().getOrderById(orderId);
        if (order != null) {
            this.assignDriverToOrder(scanner, order, orderManager.getOrderIdHandler());
        } else {
            System.out.println("Order not found.");
        }
    }

    public Driver getDriverById(final Long driverId) {
        return this.driverService.getDriverById(driverId);
    }

    public DriverService getDriverService() {
        return this.driverService;
    }

    public void addDriver(final Driver driver) {
        if (driver != null) {
            this.driverService.addDriver(driver);
            System.out.println("Driver added successfully.");
            DriverManager.logger.log(Level.INFO, "Driver {0} added", driver.getName());
        } else {
            System.out.println("Invalid driver.");
        }
    }
}
