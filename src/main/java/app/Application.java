package app;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import builder.OrderBuilder;
import managers.OrderStatusManager;
import model.Driver;
import model.Order;
import notification.BasicNotificationService;
import notification.NotificationService;

public class Application {
    private static final Logger logger = Logger.getLogger(Application.class.getName());

    public static void main(final String[] args) {
        try {
            final NotificationService notificationService = new BasicNotificationService();
            final OrderStatusManager statusManager = new OrderStatusManager(notificationService);
            final DeliverySystem deliverySystem = new DeliverySystem(notificationService, statusManager);

            // Create order using builder
            final Order order = new OrderBuilder()
                    .withValidatedCustomerId(1L)
                    .withCustomerEmail("jane.doe@example.com")
                    .addItem(createSampleMenuItem())
                    .withDeliveryLocation("456 Elm Street", "12345")
                    .build();

            // Process order with proper status management
            deliverySystem.submitOrder(order);

            final Optional<Driver> driver = Optional.of(new Driver(101L, "Bob Smith", "Car", "ABC123"));
            deliverySystem.assignOrderToDriver(order, driver);
            deliverySystem.completeDelivery(order.getOrderId(), driver.get().getId());

        } catch (final Exception e) {
            Application.logger.log(Level.SEVERE, "An error occurred while processing the order", e);
            System.err.println("Error: " + e.getMessage());
        }
    }
}
