package app;

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

    private boolean running = true;

    public DeliverySystemCLI(
                    final MenuManager menuManager,
            final OrderManager orderManager,
            final DriverManager driverManager,
            final NotificationService notificationService,
            final ConsoleInputHandler<Integer> positiveIntegerHandler) {
        this.scanner = new Scanner(System.in);
        this.menuManager = menuManager;
        this.orderManager = orderManager;
        this.driverManager = driverManager;
        this.notificationService = notificationService;
        this.positiveIntegerHandler = positiveIntegerHandler;
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

        System.out.println("\n=== " + getMenuOptionTitle(choice) + " ===");

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
            this.orderManager.processOrderPlacement(
                    this.scanner,
                    this.menuManager,
                    this.positiveIntegerHandler);
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
        System.out.println("Driver Management Options:");
        System.out.println("- Add a new driver");
        System.out.println("- Remove an existing driver");
        System.out.println("- View all drivers");
        System.out.println();

        this.driverManager.manageDriverMenu(this.scanner, this.orderManager);
    }

    private void handleRateDriver() {
        System.out.println("Rate your delivery driver:");
        System.out.println("1. Enter your order ID");
        System.out.println("2. Rate the driver from 1-5 stars");
        System.out.println();

        this.driverManager.rateDriver(
                this.scanner,
                this.orderManager.getOrderService().getOrderById(
                        this.orderManager.getOrderIdHandler().handleInput(
                                this.scanner,
                                "Enter Order ID to rate driver: ")),
                this.positiveIntegerHandler);
    }

    private void handleCalculateOrderTotal() {
        try {
            System.out.println("Calculate the total amount for your order:");
            System.out.println("Enter your order ID to see the itemized bill and total.");
            System.out.println();

            System.out.print("Enter Order ID to calculate total: ");
            final Long orderId = this.orderManager.getOrderIdHandler().handleInput(this.scanner, "Order ID: ");
            if (orderId != null) {
                final Order order = this.orderManager.getOrderService().getOrderById(orderId);
                if (order != null) {
                    final double total = this.orderManager.calculateOrderTotal(order);
                    System.out.printf("Total amount for order %d: $%.2f\n", orderId, total);
                } else {
                    System.out.println("Order not found.");
                }
            }
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

            System.out.print("Enter Driver ID to manage ratings: ");
            final Long driverId = this.orderManager.getOrderIdHandler().handleInput(this.scanner, "Driver ID: ");
            if (driverId != null) {
                final Optional<Driver> driver = this.driverManager.getDriverService().getDriverById(driverId);
                if (driver.isPresent()) {
                    System.out.print("Enter rating (1-5): ");
                    final Integer rating = this.positiveIntegerHandler.handleInput(this.scanner, "Rating: ");
                    if (rating != null) {
                        this.driverManager.getDriverService().rateDriver(driver.get(), rating);
                    } else {
                        System.out.println("Invalid rating.");
                    }
                } else {
                    System.out.println("Driver not found.");
                }
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

        this.orderManager.processOrdersInCorrectOrder(this.orderManager.getOrderQueue());
    }

    private void handleExit() {
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
