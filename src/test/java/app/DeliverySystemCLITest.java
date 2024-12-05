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
import org.junit.jupiter.api.Nested;
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

@DisplayName("DeliverySystemCLI Tests")
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

    @Nested
    @DisplayName("Menu Navigation Tests")
    class MenuNavigationTests {
        @Test
        @DisplayName("Should initialize with single enter press")
        void testInitialization() {
            DeliverySystemCLITest.this.cli = DeliverySystemCLITest.this.createCLIWithInput("");
            DeliverySystemCLITest.this.cli.start();
            final String output = DeliverySystemCLITest.this.getOutput();
            Assertions.assertTrue(output.contains("Welcome to the Online Food Delivery System"));
            Assertions.assertTrue(output.contains("Press Enter to start"));
        }

        @Test
        @DisplayName("Should handle invalid menu choices")
        void testInvalidMenuChoice() {
            DeliverySystemCLITest.this.cli = DeliverySystemCLITest.this.createCLIWithInput("10", "abc", "-1");
            DeliverySystemCLITest.this.cli.start();
            final String output = DeliverySystemCLITest.this.getOutput();
            Assertions.assertTrue(output.contains("Invalid menu choice"));
            Assertions.assertTrue(output.contains("Please enter a number between 1 and 9"));
        }

        @Test
        @DisplayName("Should exit gracefully")
        void testGracefulExit() {
            DeliverySystemCLITest.this.cli = DeliverySystemCLITest.this.createCLIWithInput("9");
            DeliverySystemCLITest.this.cli.start();
            final String output = DeliverySystemCLITest.this.getOutput();
            Assertions.assertTrue(output.contains("Thank you for using"));
            Assertions.assertTrue(output.contains("Have a great day"));
        }
    }

    @Nested
    @DisplayName("Order Management Tests")
    class OrderManagementTests {
        @Test
        @DisplayName("Should validate customer ID format")
        void testCustomerIdValidation() {
            DeliverySystemCLITest.this.cli = DeliverySystemCLITest.this.createCLIWithInput("1", "12345", "123456",
                    "test@email.com", "123 Main St", "12345");
            DeliverySystemCLITest.this.cli.start();
            final String output = DeliverySystemCLITest.this.getOutput();
            Assertions.assertTrue(output.contains("Customer ID must be exactly 6 digits"));
        }

        @Test
        @DisplayName("Should handle duplicate items in order")
        void testDuplicateItems() {
            DeliverySystemCLITest.this.cli = DeliverySystemCLITest.this.createCLIWithInput(
                    "1", "123456", "test@email.com", "123 Main St", "12345",
                    "1", "2", // First item with quantity 2
                    "1", "3", // Same item with quantity 3
                    "0", "y" // Complete order
            );
            DeliverySystemCLITest.this.cli.start();
            final String output = DeliverySystemCLITest.this.getOutput();
            Assertions.assertTrue(output.contains("Updated quantity"));
        }

        @Test
        @DisplayName("Should validate item quantities")
        void testItemQuantityValidation() {
            DeliverySystemCLITest.this.cli = DeliverySystemCLITest.this.createCLIWithInput(
                    "1", "123456", "test@email.com", "123 Main St", "12345",
                    "1", "0", "-1", "abc", "2",
                    "0", "y");
            DeliverySystemCLITest.this.cli.start();
            final String output = DeliverySystemCLITest.this.getOutput();
            Assertions.assertTrue(output.contains("Quantity must be greater than 0"));
            Assertions.assertTrue(output.contains("Please enter a valid number"));
        }
    }

    @Nested
    @DisplayName("Driver Management Tests")
    class DriverManagementTests {
        @Test
        @DisplayName("Should generate valid license plates")
        void testLicensePlateGeneration() {
            DeliverySystemCLITest.this.cli = DeliverySystemCLITest.this.createCLIWithInput("4", "1", "John Doe",
                    "1234567890", "Sedan", "4");
            DeliverySystemCLITest.this.cli.start();
            final String output = DeliverySystemCLITest.this.getOutput();
            Assertions.assertTrue(output.contains("Generated license plate"));
            Assertions.assertTrue(output.matches(".*[A-Z]{3}-\\d{4}.*"));
        }

        @Test
        @DisplayName("Should validate phone numbers")
        void testPhoneNumberValidation() {
            DeliverySystemCLITest.this.cli = DeliverySystemCLITest.this.createCLIWithInput("4", "1", "John Doe", "123",
                    "4");
            DeliverySystemCLITest.this.cli.start();
            final String output = DeliverySystemCLITest.this.getOutput();
            Assertions.assertTrue(output.contains("Please enter a valid 10-digit phone number"));
        }

        @Test
        @DisplayName("Should prevent availability changes for active drivers")
        void testActiveDriverAvailability() {
            // First add a driver and assign an order
            DeliverySystemCLITest.this.cli = DeliverySystemCLITest.this.createCLIWithInput(
                    "4", "1", "John Doe", "1234567890", "Sedan", "4", // Add driver
                    "1", "123456", "test@email.com", "123 Main St", "12345", "1", "1", "0", "y", // Place order
                    "8", // Process orders
                    "4", "4", "2", "y" // Try to change availability
            );
            DeliverySystemCLITest.this.cli.start();
            final String output = DeliverySystemCLITest.this.getOutput();
            Assertions.assertTrue(output.contains("Cannot change availability while driver has active orders"));
        }
    }

    @Nested
    @DisplayName("Rating System Tests")
    class RatingSystemTests {
        @Test
        @DisplayName("Should validate rating values")
        void testRatingValidation() {
            DeliverySystemCLITest.this.cli = DeliverySystemCLITest.this.createCLIWithInput("5", "1", "0", "6", "abc",
                    "3");
            DeliverySystemCLITest.this.cli.start();
            final String output = DeliverySystemCLITest.this.getOutput();
            Assertions.assertTrue(output.contains("Invalid rating"));
            Assertions.assertTrue(output.contains("Please enter a number between 1 and 5"));
        }

        @Test
        @DisplayName("Should show rating statistics")
        void testRatingStatistics() {
            // Add driver and submit multiple ratings
            DeliverySystemCLITest.this.cli = DeliverySystemCLITest.this.createCLIWithInput(
                    "4", "1", "John Doe", "1234567890", "Sedan", "4", // Add driver
                    "5", "1", "4", // First rating
                    "5", "1", "5", // Second rating
                    "4", "5" // View ratings
            );
            DeliverySystemCLITest.this.cli.start();
            final String output = DeliverySystemCLITest.this.getOutput();
            Assertions.assertTrue(output.contains("Average rating"));
            Assertions.assertTrue(output.contains("Total ratings"));
        }
    }

    @Nested
    @DisplayName("Order Processing Tests")
    class OrderProcessingTests {
        @Test
        @DisplayName("Should assign orders to least busy driver")
        void testDriverAssignment() {
            // Add two drivers and create multiple orders
            DeliverySystemCLITest.this.cli = DeliverySystemCLITest.this.createCLIWithInput(
                    "4", "1", "Driver1", "1234567890", "Sedan", "4", // First driver
                    "4", "1", "Driver2", "0987654321", "Sedan", "4", // Second driver
                    "1", "123456", "test@email.com", "123 Main St", "12345", "1", "1", "0", "y", // Order 1
                    "1", "123457", "test2@email.com", "456 Main St", "12345", "1", "1", "0", "y", // Order 2
                    "8" // Process orders
            );
            DeliverySystemCLITest.this.cli.start();
            final String output = DeliverySystemCLITest.this.getOutput();
            Assertions.assertTrue(output.contains("Assigned driver"));
            Assertions.assertTrue(output.contains("Successfully processed"));
        }

        @Test
        @DisplayName("Should handle failed order processing")
        void testFailedOrderProcessing() {
            // Try to process orders without any available drivers
            DeliverySystemCLITest.this.cli = DeliverySystemCLITest.this.createCLIWithInput("8");
            DeliverySystemCLITest.this.cli.start();
            final String output = DeliverySystemCLITest.this.getOutput();
            Assertions.assertTrue(output.contains("No orders to process") || output.contains("No available drivers"));
        }
    }

    @Test
    @DisplayName("Should handle empty/null inputs")
    void testEmptyInputs() {
        this.cli = this.createCLIWithInput(
                "4", "1", "", " ", "John Doe", // Empty name
                "1234567890", "Sedan", "4");
        this.cli.start();
        final String output = this.getOutput();
        Assertions.assertTrue(output.contains("cannot be empty"));
    }

    @Test
    @DisplayName("Should maintain consistent state after errors")
    void testStateConsistency() {
        this.cli = this.createCLIWithInput(
                "4", "1", "John Doe", "1234567890", "Sedan", "4", // Add driver
                "1", "invalid", "123456", "test@email.com", "123 Main St", "12345", // Invalid then valid order
                "1", "1", "0", "n" // Cancel order
        );
        this.cli.start();
        final String output = this.getOutput();
        Assertions.assertTrue(output.contains("Order cancelled"));
    }
}
