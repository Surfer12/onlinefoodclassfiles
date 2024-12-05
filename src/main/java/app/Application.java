package app;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Optional;

import builder.OrderBuilder;
import factory.MenuItemFactory;
import model.Driver;
import model.MenuItem;
import model.Order;
import model.OrderStatus;
import model.Size;
import notification.BasicNotificationService;
import notification.NotificationService;

public class Application {
    private static final Logger logger = Logger.getLogger(Application.class.getName());

    public static void main(final String[] args) {
        try {
            final NotificationService notificationService = new BasicNotificationService();
            final DeliverySystem deliverySystem = new DeliverySystem(notificationService);

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
            deliverySystem.assignOrderToDriver(order, Optional.of(driver));
            deliverySystem.completeDelivery(order.getOrderId(), driver.getId());

            // Process the next order in the queue
            deliverySystem.processNextOrder();
            // Update order status
            order.setStatus(OrderStatus.SUBMITTED);
            System.out.println("Order Status: " + order.getStatus());

            deliverySystem.assignOrderToDriver(order, Optional.of(driver));
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
