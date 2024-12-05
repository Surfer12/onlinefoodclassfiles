package app;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import managers.DriverManager;
import managers.MenuManager;
import managers.OrderManager;
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
            case 1 -> handlePlaceNewOrder();
            case 2 -> handleCheckOrderStatus();
            case 3 -> handleViewMenu();
            case 4 -> handleManageDrivers();
            case 5 -> handleRateDriver();
            case 6 -> handleCalculateOrderTotal();
            case 7 -> handleManageDriverRatings();
            case 8 -> handleProcessOrders();
            case 9 -> this.running = false;
        }
    }

    // ... rest of the implementation remains the same ...
}
