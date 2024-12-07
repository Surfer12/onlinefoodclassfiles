package app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import managers.DriverManager;
import managers.MenuManager;
import managers.OrderManager;
import model.Driver;
import model.Location;
import model.MenuItem;
import model.Order;
import model.OrderStatus;
import notification.NotificationService;
import validation.ConsoleInputHandler;
import validation.InputValidationUtils;

public class DeliverySystemCLI {
    private static final Logger logger = Logger.getLogger(DeliverySystemCLI.class.getName());

    private final Scanner scanner;
    private final MenuManager menuManager;
    private final OrderManager orderManager;
    private final DriverManager driverManager;
    private final NotificationService notificationService;
    private final ConsoleInputHandler<Integer> positiveIntegerHandler;
    private final DeliverySystem deliverySystem;
    private boolean running;

    public DeliverySystemCLI(
            final MenuManager menuManager,
            final OrderManager orderManager,
            final DriverManager driverManager,
            final NotificationService notificationService,
            final ConsoleInputHandler<Integer> positiveIntegerHandler,
            final DeliverySystem deliverySystem) {
        this(menuManager, orderManager, driverManager, notificationService,
                positiveIntegerHandler, deliverySystem, new Scanner(System.in));
    }

    public DeliverySystemCLI(
            final MenuManager menuManager,
            final OrderManager orderManager,
            final DriverManager driverManager,
            final NotificationService notificationService,
            final ConsoleInputHandler<Integer> positiveIntegerHandler,
            final DeliverySystem deliverySystem,
            final Scanner scanner) {
        this.menuManager = menuManager;
        this.orderManager = orderManager;
        this.driverManager = driverManager;
        this.notificationService = notificationService;
        this.positiveIntegerHandler = positiveIntegerHandler;
        this.deliverySystem = deliverySystem;
        this.scanner = scanner;
        this.running = true;
    }

    public void start() {
        System.out.println("Welcome to the Online Food Delivery System!");
        System.out.println("Press Enter to start...");
        this.scanner.nextLine(); // Single enter to initialize

        try {
            while (this.running && this.scanner.hasNextLine()) {
                this.displayMainMenu();

                final String input = this.scanner.nextLine().trim();

                // Handle exit command directly
                if ("exit".equalsIgnoreCase(input) || "9".equals(input)) {
                    this.cleanup();
                    break;
                }

                if (input.isEmpty()) {
                    System.out.println("Please enter a valid option.");
                    continue;
                }

                try {
                    final int choice = Integer.parseInt(input);
                    if (choice < 1 || choice > 9) {
                        System.out.println("Invalid menu choice. Please enter a number between 1 and 9.");
                        continue;
                    }
                    this.handleMenuChoice(choice);
                } catch (final NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number between 1 and 9.");
                } catch (final Exception e) {
                    DeliverySystemCLI.logger.log(Level.SEVERE, "An unexpected error occurred", e);
                    System.out.println("An unexpected error occurred: " + e.getMessage());
                }
            }
        } catch (final Exception e) {
            DeliverySystemCLI.logger.log(Level.SEVERE, "An unexpected error occurred", e);
        }
    }

    private void cleanup() {
        System.out.println("\nThank you for using the Online Food Delivery System!");
        System.out.println("Saving all data and cleaning up resources...");
        this.running = false;
        System.out.println("Have a great day! Goodbye!");
    }

    private void displayMainMenu() {
        System.out.println("\n=== Online Food Delivery System ===");
        System.out.println("1. Place a New Order (Add items to cart and checkout)");
        System.out.println("2. Check Order Status (View status of an existing order)");
        System.out.println("3. View Menu (See available items and prices)");
        System.out.println("4. Manage Drivers (Add/Remove/List drivers)");
        System.out.println("5. Rate Driver (Rate a driver for a completed order)");
        System.out.println("6. Calculate Order Total (View total for an existing order)");
        System.out.println("7. Manage Driver Ratings (View/Update driver ratings)");
        System.out.println("8. Process Orders (Process all pending orders for delivery)");
        System.out.println("9. Exit");
        System.out.println("=====================================");
        System.out.print("Please choose an option (1-9): ");
    }

    private void handleMenuChoice(final int choice) {
        switch (choice) {
            case 1 -> this.handlePlaceNewOrder();
            case 2 -> this.handleCheckOrderStatus();
            case 3 -> this.handleViewMenu();
            case 4 -> this.handleManageDrivers();
            case 5 -> this.handleRateDriver();
            case 6 -> this.handleCalculateOrderTotal();
            case 7 -> this.handleManageDriverRatings();
            case 8 -> this.handleProcessOrders();
            case 9 -> this.running = false;
        }
    }

    private void handlePlaceNewOrder() {
        System.out.println("\n=== Place New Order ===");
        System.out.println("Follow these steps to place your order:");
        System.out.println("1. First, you'll see the menu");
        System.out.println("2. Enter the menu item number and quantity for each item you want");
        System.out.println("3. Enter 0 when you're done adding items");
        System.out.println();

        // Get customer ID with proper validation
        Long customerId = null;
        while (customerId == null) {
            System.out.print("Please enter your customer ID (6 digits): ");
            final String input = this.scanner.nextLine().trim();
            if (input.matches("\\d{6}")) {
                customerId = Long.parseLong(input);
            } else {
                System.out.println("Error: Customer ID must be exactly 6 digits");
            }
        }

        String email = null;
        while (email == null || email.trim().isEmpty()) {
            System.out.print("Enter your email: ");
            email = this.scanner.nextLine().trim();
            if (email.isEmpty()) {
                System.out.println("Email cannot be empty");
            }
        }

        String address = null;
        while (address == null || address.trim().isEmpty()) {
            System.out.print("Enter delivery address: ");
            address = this.scanner.nextLine().trim();
            if (address.isEmpty()) {
                System.out.println("Address cannot be empty");
            }
        }

        String postalCode = null;
        while (postalCode == null || postalCode.trim().isEmpty()) {
            System.out.print("Enter postal code: ");
            postalCode = this.scanner.nextLine().trim();
            if (postalCode.isEmpty()) {
                System.out.println("Postal code cannot be empty");
            }
        }

        this.menuManager.displayMenu();
        final Order order = new Order(customerId, email, new ArrayList<>(), new Location(address, postalCode));

        final Map<MenuItem, Integer> orderItems = new HashMap<>();

        while (true) {
            System.out.print("\nEnter menu item number (0 to finish): ");
            final String itemInput = this.scanner.nextLine().trim();

            if (itemInput.isEmpty() || itemInput.isBlank()) {
                System.out.println("Error: Please enter a valid menu item number");
                continue;
            }

            try {
                final int itemNumber = Integer.parseInt(itemInput);
                if (itemNumber == 0) {
                    break;
                }

                final MenuItem menuItem = this.menuManager.getMenuItemById(itemNumber);
                if (menuItem == null) {
                    System.out.println("Error: Invalid menu item number");
                    continue;
                }

                System.out.print("Enter quantity: ");
                final String quantityInput = this.scanner.nextLine().trim();

                if (quantityInput.isEmpty() || quantityInput.isBlank()) {
                    System.out.println("Error: Please enter a valid quantity");
                    continue;
                }

                try {
                    final int quantity = Integer.parseInt(quantityInput);
                    if (quantity <= 0) {
                        System.out.println("Error: Quantity must be greater than 0");
                        continue;
                    }

                    // Add or update item quantity
                    orderItems.merge(menuItem, quantity, Integer::sum);
                    System.out.println("Updated quantity for " + menuItem.getName());

                } catch (final NumberFormatException e) {
                    System.out.println("Error: Please enter a valid number for quantity");
                }

            } catch (final NumberFormatException e) {
                System.out.println("Error: Please enter a valid number for menu item");
            }
        }

        // Add all items to the order
        orderItems.forEach((item, quantity) -> {
            for (int i = 0; i < quantity; i++) {
                order.addItem(item);
            }
        });

        if (order.getItems().isEmpty()) {
            System.out.println("Order cancelled - no items added");
            return;
        }

        // Show order summary
        System.out.println("\n=== Order Summary ===");
        orderItems.forEach((item, quantity) -> System.out.printf("%dx %s ($%.2f each)\n", quantity, item.getName(),
                item.getPrice()));
        System.out.printf("Total: $%.2f\n", order.getTotalAmount());

        // Confirm order
        System.out.print("\nConfirm order? (y/n): ");
        final String confirm = this.scanner.nextLine().trim().toLowerCase();
        if ("y".equals(confirm)) {
            this.deliverySystem.submitOrder(order);
            System.out.println("Order placed successfully! Your order ID is: " + order.getOrderId());
        } else {
            System.out.println("Order cancelled");
        }
    }

    private void handleCheckOrderStatus() {
        System.out.println("\n=== Check Order Status ===");
        System.out.println("Enter your order ID to check its current status.");
        this.orderManager.checkOrderStatus(this.scanner);
    }

    private void handleViewMenu() {
        System.out.println("\n=== View Menu ===");
        System.out.println("Here's our current menu with prices:");
        System.out.println();
        this.menuManager.displayMenu();
    }

    private void handleManageDrivers() {
        System.out.println("\n=== Manage Drivers ===");
        while (true) {
            System.out.println("\nDriver Management Options:");
            System.out.println("1. Add a new driver");
            System.out.println("2. Remove a driver");
            System.out.println("3. View all drivers");
            System.out.println("4. Update driver information");
            System.out.println("5. View driver ratings");
            System.out.println("6. Return to main menu");
            System.out.println("7. Exit");
            System.out.println();

            final Integer choice = this.positiveIntegerHandler.handleInput(this.scanner, "Enter your choice (1-7): ");
            if (choice == null || choice < 1 || choice > 7) {
                System.out.println("Invalid choice. Please enter a number between 1 and 7.");
                continue;
            }

            if (choice == 6) {
                break;
            }

            if (choice == 7) {
                this.cleanup();
                this.running = false;
                break;
            }

            try {
                switch (choice) {
                    case 1 -> this.handleAddDriver();
                    case 2 -> this.driverManager.removeDriver(this.scanner);
                    case 3 -> this.driverManager.displayAllDrivers();
                    case 4 -> this.handleUpdateDriverInfo();
                    case 5 -> this.handleViewDriverRatings();
                }
            } catch (final Exception e) {
                System.out.println("Error managing drivers: " + e.getMessage());
            }
        }
    }

    private void handleAddDriver() {
        System.out.println("\n=== Add New Driver ===");

        // Get driver name with validation
        String name = null;
        while (name == null || name.trim().isEmpty()) {
            System.out.print("Enter driver name: ");
            name = this.scanner.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("Driver name cannot be empty");
            }
        }

        // Get phone number with validation
        String phoneNumber = null;
        while (phoneNumber == null || !phoneNumber.matches("\\d{10}")) {
            System.out.print("Enter phone number (10 digits): ");
            phoneNumber = this.scanner.nextLine().trim();
            if (!phoneNumber.matches("\\d{10}")) {
                System.out.println("Please enter a valid 10-digit phone number");
            }
        }

        // Get vehicle type with validation
        String vehicleType = null;
        while (vehicleType == null || vehicleType.trim().isEmpty()) {
            System.out.print("Enter vehicle type (e.g., Sedan, SUV): ");
            vehicleType = this.scanner.nextLine().trim();
            if (vehicleType.isEmpty()) {
                System.out.println("Vehicle type cannot be empty");
            }
        }

        // Generate license plate
        final String licensePlate = this.generateLicensePlate();
        System.out.println("Generated license plate: " + licensePlate);

        try {
            // Create and add the driver
            final Driver driver = new Driver(
                    (long) (Math.random() * 10000), // Generate random ID
                    name,
                    vehicleType,
                    licensePlate);
            this.driverManager.getDriverService().addDriver(driver);
            System.out.println("Driver added successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println("Error adding driver: " + e.getMessage());
        }
    }

    private String generateLicensePlate() {
        final StringBuilder plate = new StringBuilder();
        // Generate 3 random uppercase letters
        for (int i = 0; i < 3; i++) {
            plate.append((char) ('A' + (int) (Math.random() * 26)));
        }
        plate.append('-');
        // Generate 4 random digits
        for (int i = 0; i < 4; i++) {
            plate.append((int) (Math.random() * 10));
        }
        return plate.toString();
    }

    private void handleUpdateDriverInfo() {
        System.out.println("\n=== Update Driver Information ===");
        final Long driverId = this.orderManager.getOrderIdHandler().handleInput(this.scanner, "Enter Driver ID: ");
        if (driverId == null) {
            System.out.println("Invalid driver ID.");
            return;
        }

        final Driver driver = this.driverManager.getDriverService().getDriverById(driverId).orElse(null);
        if (driver == null) {
            System.out.println("Driver not found.");
            return;
        }

        System.out.println("Current Information:");
        System.out.println("Name: " + driver.getName());
        System.out.println("Vehicle Type: " + driver.getVehicleType() + " (cannot be changed)");
        System.out.println("Current Vehicle: " + (driver.getVehicle() != null ? driver.getVehicle() : "Not set"));
        System.out.println("License Plate: " + driver.getLicensePlate() + " (cannot be changed)");
        System.out.println("Available: " + (driver.isAvailable() ? "Yes" : "No"));
        System.out.println("Average Rating: " + driver.getAverageRating());

        System.out.println("\nWhat would you like to update?");
        System.out.println("1. Current Vehicle");
        System.out.println("2. Availability");
        System.out.println("3. Cancel");

        final Integer choice = this.positiveIntegerHandler.handleInput(this.scanner, "Enter your choice (1-3): ");
        if (choice == null || choice < 1 || choice > 3) {
            System.out.println("Invalid choice.");
            return;
        }

        switch (choice) {
            case 1 -> {
                System.out.print("Enter new vehicle (e.g. Toyota Camry 2020): ");
                final String newVehicle = this.scanner.nextLine().trim();
                if (!newVehicle.isEmpty()) {
                    driver.setVehicle(newVehicle);
                    System.out.println("Vehicle updated successfully!");
                }
            }
            case 2 -> {
                if (driver.getActiveOrderCount() > 0) {
                    System.out.println("Cannot change availability while driver has active orders.");
                    return;
                }
                System.out.print("Set driver as available? (y/n): ");
                final String available = this.scanner.nextLine().trim().toLowerCase();
                driver.setAvailable("y".equals(available));
                System.out.println("Availability updated successfully!");
            }
        }
    }

    private void handleRateDriver() {
        System.out.println("\n=== Rate Driver ===");
        final Long driverId = this.orderManager.getOrderIdHandler().handleInput(this.scanner,
                "Enter Driver ID to rate: ");
        if (driverId == null) {
            System.out.println("Invalid driver ID.");
            return;
        }

        final Driver driver = this.driverManager.getDriverService().getDriverById(driverId).orElse(null);
        if (driver == null) {
            System.out.println("Driver not found.");
            return;
        }

        System.out.println("Rating Driver: " + driver.getName());
        System.out.println("Current average rating: " + String.format("%.1f", driver.getAverageRating()));

        Integer rating = null;
        while (rating == null) {
            try {
                rating = this.positiveIntegerHandler.handleInput(this.scanner, "Enter rating (1-5): ");
                if (rating == null || rating < 1 || rating > 5) {
                    System.out.println("Invalid rating. Please enter a number between 1 and 5");
                    rating = null;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid rating. Please enter a number between 1 and 5");
                rating = null;
            }
        }

        try {
            this.driverManager.getDriverService().rateDriver(driver, rating);
            System.out.println("Rating submitted successfully!");
            System.out.println("New average rating: " + String.format("%.1f", driver.getAverageRating()));
            System.out.println("Total ratings: " + driver.getRatings().size());
        } catch (final IllegalArgumentException e) {
            System.out.println("Error adding rating: " + e.getMessage());
        }
    }

    private void handleViewDriverRatings() {
        System.out.println("\n=== View Driver Ratings ===");
        final Long driverId = this.orderManager.getOrderIdHandler().handleInput(this.scanner, "Enter Driver ID: ");
        if (driverId == null) {
            System.out.println("Invalid driver ID.");
            return;
        }

        final Driver driver = this.driverManager.getDriverService().getDriverById(driverId).orElse(null);
        if (driver == null) {
            System.out.println("Driver not found.");
            return;
        }

        System.out.println("Driver: " + driver.getName());
        System.out.println("All ratings: " + driver.getRatings());
        System.out.println("Average rating: " + String.format("%.1f", driver.getAverageRating()));
        System.out.println("Total ratings: " + driver.getRatings().size());
    }

    private void handleCalculateOrderTotal() {
        System.out.println("\n=== Calculate Order Total ===");
        final Long orderId = this.orderManager.getOrderIdHandler().handleInput(this.scanner, "Enter Order ID: ");
        if (orderId == null) {
            System.out.println("Invalid order ID.");
            return;
        }

        final Order order = this.orderManager.getOrderService().getOrderById(orderId);
        if (order == null) {
            System.out.println("Order not found.");
            return;
        }

        final double total = this.orderManager.calculateOrderTotal(order);
        System.out.printf("Total for order %d: $%.2f%n", orderId, total);
    }

    private void handleManageDriverRatings() {
        System.out.println("\n=== Manage Driver Ratings ===");
        final Long driverId = this.orderManager.getOrderIdHandler().handleInput(this.scanner, "Enter Driver ID: ");
        if (driverId == null) {
            System.out.println("Invalid driver ID.");
            return;
        }

        final Driver driver = this.driverManager.getDriverService().getDriverById(driverId).orElse(null);
        if (driver == null) {
            System.out.println("Driver not found.");
            return;
        }

        System.out.println("Driver: " + driver.getName());
        System.out.println("Current ratings: " + driver.getRatings());
        final double avgRating = driver.getRatings().stream()
                .mapToDouble(Integer::doubleValue)
                .average()
                .orElse(0.0);
        System.out.printf("Average rating: %.1f%n", avgRating);
    }

    private void handleProcessOrders() {
        System.out.println("\n=== Process Orders ===");
        if (this.orderManager.getOrderQueue().isEmpty()) {
            System.out.println("No orders to process");
            return;
        }

        List<Driver> availableDrivers = this.driverManager.getDriverService().getAvailableDrivers();
        if (availableDrivers.isEmpty()) {
            System.out.println("No available drivers");
            return;
        }

        while (!this.orderManager.getOrderQueue().isEmpty()) {
            final Order order = this.orderManager.getOrderQueue().dequeue().orElse(null);
            if (order == null)
                continue;

            System.out.println("\nProcessing order: " + order.getOrderId());
            try {
                // Get the least busy available driver
                availableDrivers = this.driverManager.getDriverService().getAvailableDrivers();
                if (availableDrivers.isEmpty()) {
                    throw new RuntimeException("No available drivers");
                }

                // The first driver in the list will be the least busy due to sorting in
                // getAvailableDrivers
                Driver selectedDriver = availableDrivers.get(0);
                this.driverManager.getDriverService().assignDriverToOrder(selectedDriver, order);
                System.out.println("Assigned driver " + selectedDriver.getName() + " to order " + order.getOrderId());

                // Update order status
                order.setStatus(OrderStatus.IN_PROGRESS);
                System.out.println("Successfully processed order: " + order.getOrderId());
            } catch (Exception e) {
                System.out.println("Failed to process order " + order.getOrderId() + ": " + e.getMessage());
                // Re-queue the order if processing failed
                this.orderManager.getOrderQueue().enqueue(order);
            }
        }
    }
}
