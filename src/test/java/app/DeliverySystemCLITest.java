package app;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

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

public class DeliverySystemCLITest {
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;
    private DeliverySystemCLI cli;
    private DriverManager driverManager;

    private void setup() {
        // Capture console output
        this.outputStream = new ByteArrayOutputStream();
        this.originalOut = System.out;
        System.setOut(new PrintStream(this.outputStream));

        // Initialize core services
        final OrderStatusService orderStatusService = new OrderStatusServiceImpl();
        final NotificationService notificationService = new BasicNotificationService(orderStatusService);
        final OrderStatusManager statusManager = new OrderStatusManager(notificationService);

        // Initialize managers
        final MenuManager menuManager = new MenuManager();
        final OrderManager orderManager = new OrderManager(statusManager);
        this.driverManager = new DriverManager();

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

        // Create CLI instance
        this.cli = new DeliverySystemCLI(
                menuManager,
                orderManager,
                driverManager,
                notificationService,
                positiveIntegerHandler,
                deliverySystem);
    }

    private void cleanup() {
        System.setOut(this.originalOut);
    }

    private void simulateUserInput(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes()));
    }

    private void runTest() {
        try {
            this.setup();

            // Test 1: View Menu
            System.out.println("\n=== Test 1: View Menu ===");
            this.simulateUserInput("3\n9\n");
            this.cli.start();
            System.out.println(this.outputStream.toString());

            // Test 2: Place Order
            System.out.println("\n=== Test 2: Place Order ===");
            this.simulateUserInput("1\n1\n2\n2\n1\n0\n2\n9\n");
            this.cli.start();
            System.out.println(this.outputStream.toString());

            // Test 3: Add Driver
            System.out.println("\n=== Test 3: Add Driver ===");
            this.simulateUserInput("4\n1\nJohn Doe\nSedan\nABC123\n4\n9\n");
            this.cli.start();
            System.out.println(this.outputStream.toString());

            // Test 4: Process Orders
            System.out.println("\n=== Test 4: Process Orders ===");
            this.simulateUserInput("8\n9\n");
            this.cli.start();
            System.out.println(this.outputStream.toString());

            // Test 5: Rate Driver
            System.out.println("\n=== Test 5: Rate Driver ===");
            this.simulateUserInput("5\n1\n5\n9\n");
            this.cli.start();
            System.out.println(this.outputStream.toString());

        } finally {
            this.cleanup();
        }
    }

    public static void main(String[] args) {
        DeliverySystemCLITest test = new DeliverySystemCLITest();
        test.runTest();
    }
}
