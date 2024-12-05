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
import notification.BasicNotificationService;
import notification.NotificationService;
import validation.ConsoleInputHandler;
import validation.InputValidatorImpl;
import validation.PositiveIntegerValidator;

public class DeliverySystemCLI {
    private static final Logger logger = Logger.getLogger(DeliverySystemCLI.class.getName());

    private final Scanner scanner;
    private final MenuManager menuManager;
    private final OrderManager orderManager;
    private final DriverManager driverManager;
    private final NotificationService notificationService;

    private final ConsoleInputHandler<Integer> positiveIntegerHandler;

    private boolean running = true;

    public DeliverySystemCLI(final Scanner scanner, final MenuManager menuManager, final OrderManager orderManager,
            final DriverManager driverManager, final NotificationService notificationService,
            final ConsoleInputHandler<Integer> positiveIntegerHandler) {
        this.scanner = scanner;
        this.menuManager = menuManager;
        this.orderManager = orderManager;
        this.driverManager = driverManager;
        this.notificationService = notificationService;
        this.positiveIntegerHandler = positiveIntegerHandler;
    }

    public DeliverySystemCLI() {
        this(
            new Scanner(System.in),
            new MenuManager(),
            new OrderManager(),
            new DriverManager(),
            new BasicNotificationService(),
            new ConsoleInputHandler<>(
                new InputValidatorImpl<>(
                    new PositiveIntegerValidator(),
                    "Positive Integer",
                    "Invalid positive integer"))
        );
    }

    public void start() {
        try (Scanner scannerLocal = this.scanner) {
            while (this.running) {
                this.displayMainMenu();

                final Integer choice = this.menuManager.getMenuChoiceHandler().handleInput(
                        scannerLocal,
                        "Enter your choice below: ");

                if (choice == null) {
                    System.out.println("Invalid input. Please try again.");
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
        switch (choice) {
            case 1:
                this.handlePlaceNewOrder();
                break;
            case 2:
                this.handleCheckOrderStatus();
                break;
            case 3:
                this.handleViewMenu();
                break;
            case 4:
                this.handleManageDrivers();
                break;
            case 5:
                this.handleRateDriver();
                break;
            case 6:
                this.handleCalculateOrderTotal();
                break;
            case 7:
                this.handleManageDriverRatings();
                break;
            case 8:
                this.handleProcessOrdersInCorrectOrder();
                break;
            case 9:
                this.handleExit();
                break;
            default:
                System.out.println("Invalid choice. Please enter a number between 1 and 9.");
        }
    }

    private void handlePlaceNewOrder() {
        try {
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
            this.orderManager.checkOrderStatus(this.scanner);
        } catch (final OrderProcessingException e) {
            DeliverySystemCLI.logger.log(Level.SEVERE, "Order processing error occurred", e);
            System.err.println("Order processing error: " + e.getMessage());
        }
    }

    private void handleViewMenu() {
        this.menuManager.displayMenu();
    }

    private void handleManageDrivers() {
        this.driverManager.manageDriverMenu(this.scanner, this.orderManager);
    }

    private void handleRateDriver() {
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
        new DeliverySystem(this.notificationService).processOrdersInCorrectOrder(this.orderManager.getOrderQueue());
    }

    private void handleExit() {
        System.out.println("Exiting...");
        this.running = false;
    }

    private void displayMainMenu() {
        System.out.println("\n--- Online Food Delivery System ---");
        System.out.println("1. Place a New Order");
        System.out.println("2. Check Order Status");
        System.out.println("3. View Menu");
        System.out.println("4. Manage Drivers");
        System.out.println("5. Rate Driver");
        System.out.println("6. Calculate Order Total");
        System.out.println("7. Manage Driver Ratings");
        System.out.println("8. Process Orders in Correct Order");
        System.out.println("9. Exit");
        System.out.print("Please choose an option from the list above (1-9): ");
    }

    public static void main(final String[] args) {
        final DeliverySystemCLI cli = new DeliverySystemCLI();
        cli.start();
    }
}
