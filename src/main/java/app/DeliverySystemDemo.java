package app;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import CustomException.OrderProcessingException;
import model.Drink;
import model.Driver;
import model.Location;
import model.MenuItem;
import model.Order;
import model.Pizza;
import model.Size;
import notification.BasicNotificationService;
import notification.NotificationService;

public class DeliverySystemDemo {
    private final DeliverySystem deliverySystem;

    public DeliverySystemDemo(final NotificationService notificationService) {
        this.deliverySystem = new DeliverySystem(notificationService, new OrderStatusManager(notificationService));
    }

    public void runDemonstration() {
        try {
            // Create sample menu items
            final MenuItem pizza = new Pizza(1L, "Margherita Pizza", "Classic Italian pizza", 12.99, Size.MEDIUM, 1);
            final MenuItem soda = new Drink(2L, "Cola", "Refreshing drink", 2.99, Size.MEDIUM, 1);
            final List<MenuItem> items = Arrays.asList(pizza, soda);

            // Create a sample order with proper location
            final Location location = new Location("123 Main St", "12345");
            final Order sampleOrder = new Order(1L, "customer@example.com", items, location);

            System.out.println("\n=== Starting Delivery System Demonstration ===");

            // Step 1: Submit the order
            System.out.println("\nStep 1: Submitting Order");
            this.deliverySystem.submitOrder(sampleOrder);
            System.out.println("Current Status: " + this.deliverySystem.getOrderStatus(sampleOrder.getOrderId()));

            // Step 2: Find and assign a driver
            System.out.println("\nStep 2: Finding and Assigning Driver");
            final Optional<Driver> selectedDriver = this.deliverySystem.selectDriverForOrder(sampleOrder);
            this.deliverySystem.assignOrderToDriver(sampleOrder, selectedDriver);
            System.out.println("Current Status: " + this.deliverySystem.getOrderStatus(sampleOrder.getOrderId()));

            // Step 3: Complete the delivery
            System.out.println("\nStep 3: Completing Delivery");
            if (selectedDriver.isPresent()) {
                this.deliverySystem.completeDelivery(sampleOrder.getOrderId(), selectedDriver.get().getId());

                // Step 4: Rate the driver
                System.out.println("\nStep 4: Rating Driver");
                this.deliverySystem.manageDriverRatings(selectedDriver.get(), 5);
            }

            System.out.println("\nFinal Order Status: " + this.deliverySystem.getOrderStatus(sampleOrder.getOrderId()));
            System.out.println("\n=== Demonstration Complete ===\n");

        } catch (final Exception e) {
            System.err.println("Demonstration failed: " + e.getMessage());
            throw new OrderProcessingException("Demonstration failed", e);
        }
    }

    public static void main(final String[] args) {
        // Create a demo instance with a basic notification service
        final DeliverySystemDemo demo = new DeliverySystemDemo(new BasicNotificationService());
        demo.runDemonstration();
    }
}
