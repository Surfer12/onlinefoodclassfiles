package app;

import java.util.logging.Level;
import java.util.logging.Logger;

import builder.OrderBuilder;
import factory.MenuItemFactory;
import model.Driver;
import model.MenuItem;
import model.Order;
import model.OrderStatus;
import model.Size;

/**
 * The Application class is the entry point for the online food delivery system.
 */
public class Application {
    private static final Logger logger = Logger.getLogger(Application.class.getName());

    /**
     * Interface for sending notifications.
     */
    interface NotificationService {
        void sendNotification(String message);
    }

    /**
     * The main method to start the application.
     *
     * @param args the command-line arguments
     */
    public static void main(final String[] args) {
        try {
            final DeliverySystem deliverySystem = new DeliverySystem();

            final NotificationService notificationService = message -> System.out.println("Notification: " + message);

            final Driver driver = new Driver(101L, "Bob Smith", "Car", "ABC123");

            final MenuItem pizza = MenuItemFactory.createMenuItem(
                    "hamburger",
                    "Pepperoni Pizza",
                    "Spicy pepperoni with cheese",
                    12.99,
                    Size.MEDIUM,
                    1);

            final Order order = new OrderBuilder()
                    .withValidatedCustomerId(1L)
                    .withCustomerEmail("jane.doe@example.com")
                    .addItem(pizza)
                    .withDeliveryLocation("456 Elm Street", "12345")
                    .build();

            deliverySystem.submitOrder(order);
            deliverySystem.assignOrderToDriver(order, driver);
            deliverySystem.completeDelivery(order.getOrderId(), driver.getId());

            // Process the next order in the queue
            deliverySystem.processNextOrder();
            // Update order status
            order.setStatus(OrderStatus.SUBMITTED);
            System.out.println("Order Status: " + order.getStatus());

            deliverySystem.assignOrderToDriver(order, driver);
            order.setStatus(OrderStatus.IN_PROGRESS);
            System.out.println("Order Status: " + order.getStatus());

            deliverySystem.completeDelivery(order.getOrderId(), driver.getId());
            order.setStatus(OrderStatus.DELIVERED);
            System.out.println("Order Status: " + order.getStatus());

        } catch (final IllegalArgumentException e) {
            Application.logger.log(Level.SEVERE, "Invalid input provided", e);
            System.err.println("Invalid input: " + e.getMessage());
        } catch (final Exception e) {
            Application.logger.log(Level.SEVERE, "An error occurred while processing the order", e);
            System.err.println("An error occurred while processing the order: " + e.getMessage());
        }
    }
}
