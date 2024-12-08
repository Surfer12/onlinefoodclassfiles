package app;

import java.util.logging.Logger;

import managers.DriverManager;
import managers.MenuManager;
import managers.OrderManager;
import managers.OrderStatusManager;
import notification.BasicNotificationService;
import notification.NotificationService;
import services.OrderStatusService;
import services.impl.OrderStatusServiceImpl;
import validation.ConsoleInputHandler;
import validation.InputValidatorImpl;
import validation.PositiveIntegerValidator;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    private static DeliverySystemCLI createDeliverySystemCLI() {
        // Initialize core services
        final OrderStatusService orderStatusService = new OrderStatusServiceImpl();
        final NotificationService notificationService = new BasicNotificationService(orderStatusService);
        final OrderStatusManager statusManager = new OrderStatusManager(notificationService);

        // Initialize managers
        final MenuManager menuManager = new MenuManager();
        final OrderManager orderManager = new OrderManager(statusManager);
        final DriverManager driverManager = new DriverManager();

        // Initialize delivery system
        final DeliverySystem deliverySystem = new DeliverySystem(
                notificationService,
                statusManager,
                driverManager.getDriverService());

        // Initialize input handler
        final ConsoleInputHandler<Integer> positiveIntegerHandler = new ConsoleInputHandler<>(
                new InputValidatorImpl<>(
                        new PositiveIntegerValidator(),
                        "Positive Integer",
                        "Invalid positive integer"));

        // Create and return CLI instance
        return new DeliverySystemCLI(
                menuManager,
                orderManager,
                driverManager,
                notificationService,
                positiveIntegerHandler,
                deliverySystem);
    }

    public static void main(final String[] args) {
        try {
            final DeliverySystemCLI cli = Main.createDeliverySystemCLI();
            cli.start();
        } catch (final Exception e) {
            Main.logger.severe(() -> "An error occurred while running the application: " + e.getMessage());
            System.err.println("Error: " + e.getMessage());
            System.exit(1); // Exit with error code
        }
    }
}
