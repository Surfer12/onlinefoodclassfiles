package app;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
    private InputStream originalIn;
    private DeliverySystemCLI cli;
    private DriverManager driverManager;
    private MenuManager menuManager;
    private NotificationService notificationService;
    private ConsoleInputHandler<Integer> positiveIntegerHandler;
    private Scanner scanner;

    @BeforeEach
    void setup() {
        // Save original streams
        this.originalOut = System.out;
        this.originalIn = System.in;

        // Setup output capture
        this.outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(this.outputStream));

        // Initialize core services
        final OrderStatusService orderStatusService = new OrderStatusServiceImpl();
        this.notificationService = new BasicNotificationService(orderStatusService);
        final OrderStatusManager statusManager = new OrderStatusManager(this.notificationService);

        // Initialize managers
        this.menuManager = new MenuManager();
        final OrderManager orderManager = new OrderManager(statusManager);
        this.driverManager = new DriverManager();

        // Initialize input handler
        this.positiveIntegerHandler = new ConsoleInputHandler<>(
                new InputValidatorImpl<>(
                        new PositiveIntegerValidator(),
                        "Positive Integer",
                        "Invalid positive integer"));

        // Initialize delivery system
        final DeliverySystem deliverySystem = new DeliverySystem(
                this.notificationService,
                statusManager,
                this.driverManager.getDriverService());

        // Create initial scanner with empty input
        this.scanner = new Scanner("");

        // Create CLI instance with our scanner
        this.cli = new DeliverySystemCLI(
                this.menuManager,
                        orderManager,
                this.driverManager,
                this.notificationService,
                this.positiveIntegerHandler,
                deliverySystem,
                this.scanner); // Pass our scanner instance
    }

    @AfterEach
    void cleanup() {
        if (this.scanner != null) {
            this.scanner.close();
        }
        System.setOut(this.originalOut);
        System.setIn(this.originalIn);
    }

    private void simulateUserInput(final String input) {
        // Close existing scanner
        if (this.scanner != null) {
            this.scanner.close();
        }

        // Create new scanner with the test input
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        this.scanner = new Scanner(inputStream);

        // Update CLI with new scanner
        this.cli.setScanner(this.scanner);
    }

    private String getOutput() {
        return this.outputStream.toString();
    }

    @Test
    @DisplayName("Test viewing menu")
    void testViewMenu() {
        this.simulateUserInput("3\n9\n");
        this.cli.start();
        final String output = this.getOutput();
        Assertions.assertTrue(output.contains("Current Menu") || output.contains("=== Menu ==="));
    }

    @Test
    @DisplayName("Test placing order")
    void testPlaceOrder() {
        this.simulateUserInput("1\n1\n1\n0\n9\n");
        this.cli.start();
        final String output = this.getOutput();
        Assertions.assertTrue(output.contains("Enter menu item") || output.contains("Place Order"));
    }

    @Test
    @DisplayName("Test driver management")
    void testDriverManagement() {
        this.simulateUserInput("4\n1\nJohn Doe\nSedan\nABC123\n9\n");
        this.cli.start();
        final String output = this.getOutput();
        Assertions.assertTrue(output.contains("Driver Management") || output.contains("Manage Drivers"));
    }

    @Test
    @DisplayName("Test order processing")
    void testOrderProcessing() {
        this.simulateUserInput("8\n9\n");
        this.cli.start();
        final String output = this.getOutput();
        Assertions.assertTrue(output.contains("Process Orders") || output.contains("Processing orders"));
    }

    @Test
    @DisplayName("Test invalid inputs")
    void testInvalidInputs() {
        this.simulateUserInput("abc\n-1\n10\n9\n");
        this.cli.start();
        final String output = this.getOutput();
        Assertions.assertTrue(output.contains("Invalid") || output.contains("Please choose"));
    }

    @Test
    @DisplayName("Test driver rating")
    void testDriverRating() {
        this.simulateUserInput("4\n1\nJohn Doe\nSedan\nABC123\n" +
                "1\n1\n1\n0\n" + // Place order
                "8\n" + // Process orders
                "5\n1\n5\n" + // Rate driver
                "9\n"); // Exit
        this.cli.start();
        final String output = this.getOutput();
        Assertions.assertTrue(output.contains("Rate Driver") ||
                output.contains("Driver Management") ||
                output.contains("No completed orders"));
    }

    @Test
    @DisplayName("Test system shutdown")
    void testSystemShutdown() {
        this.simulateUserInput("9\n");
        this.cli.start();
        final String output = this.getOutput();
        Assertions.assertTrue(output.contains("=== Online Food Delivery System ==="));
    }
}
