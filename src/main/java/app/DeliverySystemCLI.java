package app;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import CustomException.OrderProcessingException;
import CustomException.PaymentException;
import CustomException.QueueFullException;
import CustomException.ValidationException;
import managers.DriverManager;
import managers.MenuManager;
import managers.OrderManager;
import model.Driver;
import model.Order;
import model.OrderStatus;
import notification.NotificationService;
import queue.OrderQueue;
import validation.ConsoleInputHandler;

public class DeliverySystemCLI {
    private static final Logger logger = Logger.getLogger(DeliverySystemCLI.class.getName());

    private Scanner scanner;
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

    public void setScanner(final Scanner scanner) {
        if (this.scanner != null) {
            this.scanner.close();
        }
        this.scanner = scanner;
    }

    public void start() {
        try (Scanner scannerLocal = this.scanner) {
            while (this.running) {
                this.displayMainMenu();

                final Integer choice = this.menuManager.getMenuChoiceHandler().handleInput(
                        scannerLocal,
                        "Please choose an option (1-9): ");

                if (choice == null) {
                    System.out.println("Invalid input. Please enter a number between 1 and 9.");
                    continue;
                }

                try {
                    this.handleMenuChoice(choice);
                } catch (final ValidationException e) {
                    DeliverySystemCLI.logger.log(Level.SEVERE, "Validation error occurred", e);
                    System.err.println("Validation error: " + e.getMessage());
                } catch (final PaymentException e) {
                    DeliverySystemCLI.logger.log(Level.SEVERE, "Payment processing error occurred", e);
                    System.err.println("Payment processing error: " + e.getMessage());
                } catch (final QueueFullException e) {
                    DeliverySystemCLI.logger.log(Level.SEVERE, "Order queue is full", e);
                    System.err.println("Order queue is full: " + e.getMessage());
                } catch (final OrderProcessingException e) {
                    DeliverySystemCLI.logger.log(Level.SEVERE, "Order processing error occurred", e);
                    System.err.println("Order processing error: " + e.getMessage());
                } catch (final Exception e) {
                    DeliverySystemCLI.logger.log(Level.SEVERE, "An unexpected error occurred", e);
                    System.err.println("An unexpected error occurred: " + e.getMessage());
                }
            }
        }
    }

    private void handleMenuChoice(final int choice) {
        if (choice < 1 || choice > 9) {
            System.out.println("Invalid menu choice. Please enter a number between 1 and 9.");
            return;
        }

        System.out.println("\n=== " + this.getMenuOptionTitle(choice) + " ===");

        switch (choice) {
            case 1 -> this.handlePlaceNewOrder();
            case 2 -> this.handleCheckOrderStatus();
            case 3 -> this.handleViewMenu();
            case 4 -> this.handleManageDrivers();
            case 5 -> this.handleRateDriver();
            case 6 -> this.handleCalculateOrderTotal();
            case 7 -> this.handleManageDriverRatings();
            case 8 -> this.handleProcessOrdersInCorrectOrder();
            case 9 -> {
                this.handleExit();
                return;
            }
        }
    }

    private String getMenuOptionTitle(final int choice) {
        return switch (choice) {
            case 1 -> "Place New Order";
            case 2 -> "Check Order Status";
            case 3 -> "View Menu";
            case 4 -> "Manage Drivers";
            case 5 -> "Rate Driver";
            case 6 -> "Calculate Order Total";
            case 7 -> "Manage Driver Ratings";
            case 8 -> "Process Orders";
            case 9 -> "Exit";
            default -> "Unknown Option";
        };
    }

    private void handlePlaceNewOrder() {
        try {
            System.out.println("Follow these steps to place your order:");
            System.out.println("1. First, you'll see the menu");
            System.out.println("2. Enter the menu item number and quantity for each item you want");
            System.out.println("3. Enter 0 when you're done adding items");
            System.out.println("4. Choose how to provide your customer ID");
            System.out.println();

            // First, display the menu
            this.menuManager.displayMenu();

            // Then process order placement
            final Order order = this.orderManager.processOrderPlacement(
                    this.scanner,
                    this.menuManager,
                    this.positiveIntegerHandler);

            // Submit order through delivery system
            if (order != null) {
                this.deliverySystem.submitOrder(order);
            }
        } catch (final OrderProcessingException e) {
            DeliverySystemCLI.logger.log(Level.SEVERE, "Order processing error occurred", e);
            System.err.println("Order processing error: " + e.getMessage());
        }
    }

    private void handleCheckOrderStatus() {
        try {
            System.out.println("Enter your order ID to check its current status.");
            System.out.println("The status will show where your order is in the delivery process.");
            System.out.println();

            this.orderManager.checkOrderStatus(this.scanner);
        } catch (final OrderProcessingException e) {
            DeliverySystemCLI.logger.log(Level.SEVERE, "Order processing error occurred", e);
            System.err.println("Order processing error: " + e.getMessage());
        }
    }

    private void handleViewMenu() {
        System.out.println("Here's our current menu with prices:");
        System.out.println();
        this.menuManager.displayMenu();
    }

    private void handleManageDrivers() {
        while (true) {
            System.out.println("\nDriver Management Options:");
            System.out.println("1. Add a new driver");
            System.out.println("2. Remove a driver");
            System.out.println("3. View all drivers");
            System.out.println("4. Return to main menu");
            System.out.println();

            final Integer choice = this.positiveIntegerHandler.handleInput(this.scanner, "Enter your choice (1-4): ");
            if (choice == null || choice < 1 || choice > 4) {
                System.out.println("Invalid choice. Please enter a number between 1 and 4.");
                continue;
            }

            if (choice == 4) {
                return;
            }

            try {
                switch (choice) {
                    case 1 -> this.driverManager.addDriver(this.scanner);
                    case 2 -> this.driverManager.removeDriver(this.scanner);
                    case 3 -> this.driverManager.displayAllDrivers();
                }
            } catch (final Exception e) {
                System.err.println("Error managing drivers: " + e.getMessage());
                DeliverySystemCLI.logger.log(Level.SEVERE, "Error in driver management", e);
            }
        }
    }

    private void handleRateDriver() {
        System.out.println("Rate your delivery driver:");
        System.out.println("1. Enter your order ID");
        System.out.println("2. Rate the driver from 1-5 stars");
        System.out.println();

        final Long orderId = this.orderManager.getOrderIdHandler().handleInput(
                this.scanner,
                "Enter Order ID to rate driver: ");

        if (orderId == null) {
            System.out.println("Invalid order ID provided. Please try again.");
            return;
        }

        final Order order = this.orderManager.getOrderService().getOrderById(orderId);
        if (order == null) {
            System.out.println("Order not found. Please check your order ID and try again.");
            return;
        }

        if (order.getDriver().isEmpty()) {
            System.out.println("No driver assigned to this order yet.");
            return;
        }

        System.out.println("Rating driver: " + order.getDriver().get().getName());
        System.out.println("Please rate your driver (1-5 stars):");
        System.out.println("1 = Poor, 2 = Fair, 3 = Good, 4 = Very Good, 5 = Excellent");

        Integer rating;
        do {
            rating = this.positiveIntegerHandler.handleInput(this.scanner, "Enter rating (1-5): ");
            if (rating == null || rating < 1 || rating > 5) {
                System.out.println("Invalid rating. Please enter a number between 1 and 5.");
            }
        } while (rating == null || rating < 1 || rating > 5);

        this.driverManager.getDriverService().rateDriver(order.getDriver().get(), rating);
        System.out.println("Thank you for rating your driver!");
    }

    private void handleCalculateOrderTotal() {
        try {
            System.out.println("Calculate the total amount for your order:");
            System.out.println("Enter your order ID to see the itemized bill and total.");
            System.out.println();

            final Long orderId = this.orderManager.getOrderIdHandler().handleInput(this.scanner, "Enter Order ID: ");
            if (orderId == null) {
                System.out.println("Invalid order ID provided. Please try again.");
                return;
            }

            final Order order = this.orderManager.getOrderService().getOrderById(orderId);
            if (order == null) {
                System.out.println("Order not found. Please check your order ID and try again.");
                return;
            }

            // Show itemized bill
            System.out.println("\nOrder Details:");
            System.out.println("Order ID: " + orderId);
            order.getItems().forEach(item -> System.out.printf("- %s: $%.2f\n", item.getName(), item.getPrice()));
            System.out.println("-".repeat(30));

            final double total = this.orderManager.calculateOrderTotal(order);
            System.out.printf("Total amount: $%.2f\n", total);
        } catch (final OrderProcessingException e) {
            DeliverySystemCLI.logger.log(Level.SEVERE, "Order processing error occurred", e);
            System.err.println("Order processing error: " + e.getMessage());
        }
    }

    private void handleManageDriverRatings() {
        try {
            System.out.println("Manage Driver Ratings:");
            System.out.println("1. Enter the driver ID");
            System.out.println("2. View current ratings");
            System.out.println("3. Add a new rating if needed");
            System.out.println();

            final Long driverId = this.orderManager.getOrderIdHandler().handleInput(this.scanner, "Enter Driver ID: ");
            if (driverId == null) {
                System.out.println("Invalid driver ID provided. Please try again.");
                return;
            }

            final Optional<Driver> driver = this.driverManager.getDriverService().getDriverById(driverId);
            if (driver.isEmpty()) {
                System.out.println("Driver not found. Please check the driver ID and try again.");
                return;
            }

            // Show current ratings
            System.out.println("\nCurrent Ratings for Driver " + driver.get().getName() + ":");
            final List<Integer> ratings = driver.get().getRatings();
            if (ratings.isEmpty()) {
                System.out.println("No ratings yet.");
            } else {
                final double averageRating = ratings.stream()
                        .mapToDouble(Integer::doubleValue)
                        .average()
                        .orElse(0.0);
                System.out.println("Number of ratings: " + ratings.size());
                System.out.printf("Average rating: %.1f stars\n", averageRating);
                System.out.println("Rating distribution:");
                for (int i = 1; i <= 5; i++) {
                    final int stars = i;
                    final long count = ratings.stream().filter(r -> r == stars).count();
                    System.out.printf("%d stars: %d ratings\n", i, count);
                }
            }

            System.out.println("\nWould you like to add a new rating? (Y/N)");
            final String choice = this.scanner.nextLine().trim().toUpperCase();
            if ("Y".equals(choice)) {
                Integer rating;
                do {
                    System.out.println("Please rate the driver (1-5 stars):");
                    System.out.println("1 = Poor, 2 = Fair, 3 = Good, 4 = Very Good, 5 = Excellent");
                    rating = this.positiveIntegerHandler.handleInput(this.scanner, "Enter rating (1-5): ");
                    if (rating == null || rating < 1 || rating > 5) {
                        System.out.println("Invalid rating. Please enter a number between 1 and 5.");
                    }
                } while (rating == null || rating < 1 || rating > 5);

                this.driverManager.getDriverService().rateDriver(driver.get(), rating);
                System.out.println("Rating added successfully!");
            }
        } catch (final OrderProcessingException e) {
            DeliverySystemCLI.logger.log(Level.SEVERE, "Order processing error occurred", e);
            System.err.println("Order processing error: " + e.getMessage());
        }
    }

    private void handleProcessOrdersInCorrectOrder() {
        System.out.println("Processing all pending orders in the queue:");
        System.out.println("Each order will go through these stages:");
        System.out.println("1. IN_PROGRESS - Order is being processed");
        System.out.println("2. PREPARING - Food is being prepared");
        System.out.println("3. OUT_FOR_DELIVERY - Order is on its way");
        System.out.println("4. DELIVERED - Order has been delivered");
        System.out.println();

        final OrderQueue orderQueue = this.orderManager.getOrderQueue();
        if (orderQueue.isEmpty()) {
            System.out.println("No orders in the queue to process.");
            return;
        }

        // Process each order
        while (!orderQueue.isEmpty()) {
            final Optional<Order> orderOpt = orderQueue.dequeue();
            if (orderOpt.isEmpty()) {
                continue;
            }

            final Order order = orderOpt.get();
            System.out.println("\nProcessing order: " + order.getOrderId());

            try {
                // Assign a driver if not already assigned
                if (order.getDriver().isEmpty()) {
                    final List<Driver> availableDrivers = this.driverManager.getDriverService().getAvailableDrivers();
                    if (availableDrivers.isEmpty()) {
                        System.out.println("No drivers available. Order will remain in queue.");
                        continue;
                    }
                    final Driver driver = availableDrivers.get(0);
                    this.driverManager.getDriverService().assignDriverToOrder(driver, order);
                    System.out.println("Assigned driver: " + driver.getName());
                    this.notificationService.sendDriverAssignmentNotification(order, driver);
                }

                // Update order status
                order.setStatus(OrderStatus.IN_PROGRESS);
                System.out.println("Status: IN_PROGRESS - Order is being processed");
                this.notificationService.sendOrderStatusUpdateToCustomer(order, OrderStatus.IN_PROGRESS);

                order.setStatus(OrderStatus.PREPARING);
                System.out.println("Status: PREPARING - Food is being prepared");
                this.notificationService.sendOrderStatusUpdateToCustomer(order, OrderStatus.PREPARING);

                order.setStatus(OrderStatus.OUT_FOR_DELIVERY);
                System.out.println("Status: OUT_FOR_DELIVERY - Order is on its way");
                this.notificationService.sendOrderStatusUpdateToCustomer(order, OrderStatus.OUT_FOR_DELIVERY);

                order.setStatus(OrderStatus.DELIVERED);
                System.out.println("Status: DELIVERED - Order has been delivered");
                this.notificationService.sendDeliveryCompletionNotification(order);

                // Make driver available again
                order.getDriver().ifPresent(driver -> driver.setAvailable(true));

            } catch (final Exception e) {
                System.err.println("Error processing order " + order.getOrderId() + ": " + e.getMessage());
                order.setStatus(OrderStatus.CANCELLED);
                System.out.println("Order has been cancelled due to an error");
                this.notificationService.sendOrderStatusUpdateToCustomer(order, OrderStatus.CANCELLED);

                // Make sure to make driver available if there was an error
                order.getDriver().ifPresent(driver -> driver.setAvailable(true));
            }
        }
        System.out.println("\nAll orders have been processed.");
    }

    private void handleExit() {
        System.out.println("\nCleaning up resources...");

        // Close scanner
        if (this.scanner != null) {
            this.scanner.close();
        }

        // Make all drivers available
        this.driverManager.getDriverService().getAllDrivers()
                .forEach(driver -> driver.setAvailable(true));

        System.out.println("Thank you for using the Online Food Delivery System. Goodbye!");
        this.running = false;
        System.exit(0);
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
    }
}
