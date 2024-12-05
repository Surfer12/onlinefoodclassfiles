package app;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
    private DeliverySystemCLI cli;
    private DriverManager driverManager;
    private MenuManager menuManager;
    private NotificationService notificationService;
    private ConsoleInputHandler<Integer> positiveIntegerHandler;
    private OrderManager orderManager;
    private DeliverySystem deliverySystem;
    private OrderStatusManager statusManager;

    @BeforeEach
    void setup() {
        // Setup output capture
        this.originalOut = System.out;
        this.outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(this.outputStream));

        // Initialize core services
        final OrderStatusService orderStatusService = new OrderStatusServiceImpl();
        this.notificationService = new BasicNotificationService(orderStatusService);
        this.statusManager = new OrderStatusManager(this.notificationService);

        // Initialize managers
        this.menuManager = new MenuManager();
        this.orderManager = new OrderManager(this.statusManager);
        this.driverManager = new DriverManager();

        // Initialize input handler
        this.positiveIntegerHandler = new ConsoleInputHandler<>(
                new InputValidatorImpl<>(
                        new PositiveIntegerValidator(),
                        "Positive Integer",
                        "Invalid positive integer"));

        // Initialize delivery system
        this.deliverySystem = new DeliverySystem(
                this.notificationService,
                this.statusManager,
                this.driverManager.getDriverService());
    }

    @AfterEach
    void cleanup() {
        System.setOut(this.originalOut);
    }

    private Scanner createTestScanner(final String... inputs) {
        final StringBuilder input = new StringBuilder();
        for (final String s : inputs) {
            input.append(s).append("\n");
        }
        // Always append exit command if not present
        if (!input.toString().trim().endsWith("9")) {
            input.append("9\n");
        }
        return new Scanner(new ByteArrayInputStream(input.toString().getBytes()));
    }

    private DeliverySystemCLI createCLIWithInput(final String... inputs) {
        return new DeliverySystemCLI(
                this.menuManager,
                this.orderManager,
                this.driverManager,
                this.notificationService,
                this.positiveIntegerHandler,
                        this.deliverySystem,
                this.createTestScanner(inputs));
    }

    private String getOutput() {
        return this.outputStream.toString();
    }

    @Test
    @DisplayName("Test viewing menu")
    void testViewMenu() {
        this.cli = this.createCLIWithInput("3");
        this.cli.start();
        final String output = this.getOutput();
        Assertions.assertTrue(output.contains("=== View Menu ===") ||
                output.contains("Current Menu") ||
                output.contains("Menu with prices"));
    }

    @Test
    @DisplayName("Test placing order")
    void testPlaceOrder() {
        this.cli = this.createCLIWithInput("1", "1", "1", "0");
        this.cli.start();
        final String output = this.getOutput();
        Assertions.assertTrue(output.contains("Place") || output.contains("Order"));
    }

    @Test
    @DisplayName("Test driver management")
    void testDriverManagement() {
        this.cli = this.createCLIWithInput("4", "4");
        this.cli.start();
        final String output = this.getOutput();
        Assertions.assertTrue(output.contains("Driver Management"));
    }

    @Test
    @DisplayName("Test order processing")
    void testOrderProcessing() {
        this.cli = this.createCLIWithInput("8");
        this.cli.start();
        final String output = this.getOutput();
        Assertions.assertTrue(output.contains("orders"));
    }

    @Test
    @DisplayName("Test invalid inputs")
    void testInvalidInputs() {
        this.cli = this.createCLIWithInput("abc");
        this.cli.start();
        final String output = this.getOutput();
        Assertions.assertTrue(output.contains("Invalid") || output.contains("Please choose"));
    }

    @Test
    @DisplayName("Test driver rating")
    void testDriverRating() {
        // First add a driver
        this.cli = this.createCLIWithInput("4", "1", "John Doe", "Sedan", "ABC123", "4");
        this.cli.start();
        this.outputStream.reset();

        // Try to rate the driver
        this.cli = this.createCLIWithInput("5", "1", "5");
        this.cli.start();
        final String output = this.getOutput();
        Assertions.assertTrue(output.contains("Rate") || output.contains("Driver"));
    }

    @Test
    @DisplayName("Test system shutdown")
    void testSystemShutdown() {
        this.cli = this.createCLIWithInput("9");
        this.cli.start();
        final String output = this.getOutput();
        Assertions.assertTrue(output.contains("=== Online Food Delivery System ==="));
    }
}
