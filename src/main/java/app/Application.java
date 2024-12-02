package app;

import java.util.logging.Level;
import java.util.logging.Logger;

import builder.OrderBuilder;
import factory.MenuItemFactory;
import model.Driver;
import model.MenuItem;
import model.Order;
import model.Size;

public class Application {
    private static final Logger logger = Logger.getLogger(Application.class.getName());

    interface NotificationService {
        void sendNotification(String message);
    }

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

        } catch (final IllegalArgumentException e) {
            Application.logger.log(Level.SEVERE, "Invalid input provided", e);
            System.err.println("Invalid input: " + e.getMessage());
        } catch (final Exception e) {
            Application.logger.log(Level.SEVERE, "An error occurred while processing the order", e);
            System.err.println("An error occurred while processing the order: " + e.getMessage());
        }
    }
}
