package app;

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
import validation.ConsoleInputHandler;
import validation.InputValidatorImpl;
import validation.PositiveIntegerValidator;

public class DeliverySystemCLI {
    private static final Logger logger = Logger.getLogger(DeliverySystemCLI.class.getName());

    private final Scanner scanner;
    private final MenuManager menuManager;
    private final OrderManager orderManager;
    private final DriverManager driverManager;

    private final ConsoleInputHandler<Integer> positiveIntegerHandler;
    private final ConsoleInputHandler<String> emailHandler;
    private final ConsoleInputHandler<String> locationHandler;

    private boolean running = true;

    public DeliverySystemCLI(final Scanner scanner, final MenuManager menuManager, final OrderManager orderManager,
            final DriverManager driverManager, final ConsoleInputHandler<Integer> positiveIntegerHandler,
            final ConsoleInputHandler<String> emailHandler, final ConsoleInputHandler<String> locationHandler) {
        this.scanner = scanner;
        this.menuManager = menuManager;
        this.orderManager = orderManager;
        this.driverManager = driverManager;
        this.positiveIntegerHandler = positiveIntegerHandler;
        this.emailHandler = emailHandler;
        this.locationHandler = locationHandler;
    }

    public DeliverySystemCLI() {
        this(new Scanner(System.in),
                new MenuManager(),
                new OrderManager(),
                new DriverManager(),
                new ConsoleInputHandler<>(
                        new InputValidatorImpl<>(
                                new PositiveIntegerValidator(),
                                "Positive Integer",
                                "Invalid positive integer")),
                new ConsoleInputHandler<>(
                        InputValidatorImpl.emailValidator()),
                new ConsoleInputHandler<>(
                        InputValidatorImpl.deliveryLocationValidator()));
    }

    public void start() {
        while (this.running) {
            this.displayMainMenu();

            final Integer choice = this.menuManager.getMenuChoiceHandler().handleInput(
                    this.scanner,
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
        this.scanner.close();
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
            case 8 -> this.handleProcessOrdersInCorrectOrder();
            case 9 -> this.handleExit();
            default -> System.out.println("Invalid choice. Please enter a number between 1 and 9.");
        }
    }

    private void handlePlaceNewOrder() {
        Integer menuItem;
        do {
            menuItem = this.positiveIntegerHandler.handleInput(this.scanner, "Enter menu item number to add (0 to finish): ");
            if (menuItem == null || menuItem < 0) {
                System.out.println("Invalid positive integer. Please try again.");
            }
        } while (menuItem == null || menuItem < 0);

        this.orderManager.processOrderPlacement(
                this.scanner,
                this.menuManager,
                this.positiveIntegerHandler);
    }

    private void handleCheckOrderStatus() {
        this.orderManager.checkOrderStatus(this.scanner);
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
                this.menuManager.getMenuChoiceHandler());
    }

    private void handleCalculateOrderTotal() {
        System.out.print("Enter Order ID to calculate total: ");
        final Long orderId = this.orderManager.getOrderIdHandler().handleInput(this.scanner, "Order ID: ");
        if (orderId != null) {
            final Order order = this.orderManager.getOrderService().getOrderById(orderId);
            if (order != null) {
                final double total = new DeliverySystem().calculateOrderTotal(order);
                System.out.printf("Total amount for order %d: $%.2f\n", orderId, total);
            } else {
                System.out.println("Order not found.");
            }
        }
    }

    private void handleManageDriverRatings() {
        System.out.print("Enter Driver ID to manage ratings: ");
        final Long driverId = this.orderManager.getOrderIdHandler().handleInput(this.scanner, "Driver ID: ");
        if (driverId != null) {
            final Driver driver = this.driverManager.getDriverService().getDriverById(driverId);
            if (driver != null) {
                System.out.print("Enter rating (1-5): ");
                final Integer rating = this.positiveIntegerHandler.handleInput(this.scanner, "Rating: ");
                if (rating != null) {
                    new DeliverySystem().manageDriverRatings(driver, rating);
                } else {
                    System.out.println("Invalid rating.");
                }
            } else {
                System.out.println("Driver not found.");
            }
        }
    }

    private void handleProcessOrdersInCorrectOrder() {
        new DeliverySystem().processOrdersInCorrectOrder(this.orderManager.getOrderQueue());
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
