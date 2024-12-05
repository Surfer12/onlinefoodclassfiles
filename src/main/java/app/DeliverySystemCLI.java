package app;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import managers.DriverManager;
import managers.MenuManager;
import managers.OrderManager;
import model.Driver;
import model.Order;
import model.OrderStatus;
import notification.NotificationService;
import validation.ConsoleInputHandler;

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
        try {
            while (this.running && this.scanner.hasNextLine()) {
                this.displayMainMenu();

                final String input = this.scanner.nextLine().trim();
                if (input.isEmpty()) {
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
        } finally {
            this.cleanup();
        }
    }

    private void cleanup() {
        System.out.println("\nCleaning up resources...");
        this.running = false;
        System.out.println("Thank you for using the Online Food Delivery System. Goodbye!");
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

        this.menuManager.displayMenu();
        final Order order = this.orderManager.processOrderPlacement(this.scanner, this.menuManager,
                this.positiveIntegerHandler);
        if (order != null) {
            this.deliverySystem.submitOrder(order);
            System.out.println("Order placed successfully!");
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
            System.out.println("4. Return to main menu");
            System.out.println();

            final Integer choice = this.positiveIntegerHandler.handleInput(this.scanner, "Enter your choice (1-4): ");
            if (choice == null || choice < 1 || choice > 4) {
                System.out.println("Invalid choice. Please enter a number between 1 and 4.");
                continue;
            }

            if (choice == 4) {
                break;
            }

            try {
                switch (choice) {
                    case 1 -> this.driverManager.addDriver(this.scanner);
                    case 2 -> this.driverManager.removeDriver(this.scanner);
                    case 3 -> this.driverManager.displayAllDrivers();
                }
            } catch (final Exception e) {
                System.out.println("Error managing drivers: " + e.getMessage());
            }
        }
    }

    private void handleRateDriver() {
        System.out.println("\n=== Rate Driver ===");
        final Long orderId = this.orderManager.getOrderIdHandler().handleInput(this.scanner,
                "Enter Order ID to rate driver: ");
        if (orderId == null) {
            System.out.println("Invalid order ID.");
            return;
        }

        final Order order = this.orderManager.getOrderService().getOrderById(orderId);
        if (order == null || order.getDriver().isEmpty()) {
            System.out.println("Order not found or no driver assigned.");
            return;
        }

        final Driver driver = order.getDriver().get();
        final Integer rating = this.positiveIntegerHandler.handleInput(this.scanner, "Rate driver (1-5): ");
        if (rating != null && rating >= 1 && rating <= 5) {
            this.driverManager.getDriverService().rateDriver(driver, rating);
            System.out.println("Rating submitted successfully!");
        } else {
            System.out.println("Invalid rating. Please enter a number between 1 and 5.");
        }
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
            System.out.println("No orders to process.");
            return;
        }

        while (!this.orderManager.getOrderQueue().isEmpty()) {
            final Order order = this.orderManager.getOrderQueue().dequeue().orElse(null);
            if (order == null)
                continue;

            System.out.println("Processing order: " + order.getOrderId());
            try {
                if (order.getDriver().isEmpty()) {
                    final Driver driver = this.driverManager.getDriverService().getAvailableDrivers().get(0);
                    this.driverManager.getDriverService().assignDriverToOrder(driver, order);
                    System.out.println("Assigned driver: " + driver.getName());
                }

                order.setStatus(OrderStatus.DELIVERED);
                this.notificationService.sendDeliveryCompletionNotification(order);
                System.out.println("Order " + order.getOrderId() + " delivered successfully!");
            } catch (final Exception e) {
                System.out.println("Error processing order: " + e.getMessage());
                order.setStatus(OrderStatus.CANCELLED);
            }
        }
    }
}
