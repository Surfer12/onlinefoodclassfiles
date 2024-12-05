package app;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import CustomException.OrderProcessingException;
import model.Driver;
import model.Order;
import notification.NotificationService;
import queue.OrderQueue;
import services.DriverService;
import services.DriverServiceImpl;

public class DeliverySystem {
   private final Map<Long, String> orderStatuses = new HashMap<>();
   private final NotificationService notificationService;

   public DeliverySystem(final NotificationService notificationService) {
      this.notificationService = notificationService;
   }

   public void submitOrder(final Order order) {
      try {
         System.out.println("Order submitted: " + order.getOrderId());
         this.orderStatuses.put(order.getOrderId(), "Pending");
         this.notificationService.sendOrderConfirmationToCustomer(order);
      } catch (final Exception e) {
         throw new OrderProcessingException("Failed to submit order: " + e.getMessage(), e);
      }
   }

   public void assignOrderToDriver(final Order order, final Optional<Driver> driver) {
      try {
         if (driver.isPresent()) {
            System.out.println("Order " + order.getOrderId() + " assigned to driver " + driver.get().getName());
            this.orderStatuses.put(order.getOrderId(), "In Progress");
            this.notificationService.sendDriverAssignmentNotification(order, driver.get());
         } else {
            System.out.println("No available driver for order " + order.getOrderId());
         }
      } catch (final Exception e) {
         throw new OrderProcessingException("Failed to assign order to driver: " + e.getMessage(), e);
      }
   }

   public void completeDelivery(final Long orderId, final Long driverId) {
      try {
         System.out.println("Delivery completed for order " + orderId + " by driver " + driverId);
         this.orderStatuses.put(orderId, "Delivered");
         final Order order = new Order(orderId, null, null, null);
         this.notificationService.sendDeliveryCompletionNotification(order);
      } catch (final Exception e) {
         throw new OrderProcessingException("Failed to complete delivery: " + e.getMessage(), e);
      }
   }

   public String getOrderStatus(final Long orderId) {
      return this.orderStatuses.getOrDefault(orderId, "Order Not Found");
   }

   public void manageDriverRatings(final Driver driver, final int rating) {
      driver.addRating(rating);
      System.out.println("Driver " + driver.getName() + " rated with " + rating + " stars.");
   }

   public void processOrdersInCorrectOrder(final OrderQueue orderQueue) {
      while (!orderQueue.isEmpty()) {
         final Order order = orderQueue.dequeue().orElse(null);
         if (order != null) {
            System.out.println("Processing order: " + order.getOrderId());
            // Process the order
         }
      }
   }

   public void processNextOrder() {
      final OrderQueue orderQueue = new OrderQueue(10); // Create an instance of OrderQueue
      final Order order = orderQueue.dequeue().orElse(null);
      if (order != null) {
         final Optional<Driver> driver = this.selectDriverForOrder(order);
         this.assignOrderToDriver(order, driver);
         System.out.println("Order " + order.getOrderId() + " is now being processed.");
      } else {
         System.out.println("No orders to process.");
      }
   }

   public Optional<Driver> selectDriverForOrder(final Order order) {
      final Optional<Driver> availableDriver = this.findAvailableDriverForOrderType(order);
      if (availableDriver.isEmpty()) {
         throw new OrderProcessingException("No available drivers to assign to the order.");
      }
      return availableDriver;
   }

   private Optional<Driver> findAvailableDriverForOrderType(final Order order) {
      final DriverService driverService = new DriverServiceImpl();
      final List<Driver> availableDrivers = driverService.getAvailableDrivers();
      return availableDrivers.isEmpty() ? Optional.empty() : Optional.of(availableDrivers.get(0));
   }

   public void demonstrateDeliverySystem() {
      try {
         // Create a sample order
         final Order sampleOrder = new Order(1L, "123 Main St", "Sample Customer", List.of("Pizza", "Soda"));
         System.out.println("\n=== Starting Delivery System Demonstration ===");

         // Step 1: Submit the order
         System.out.println("\nStep 1: Submitting Order");
         this.submitOrder(sampleOrder);
         System.out.println("Current Status: " + this.getOrderStatus(sampleOrder.getOrderId()));

         // Step 2: Find and assign a driver
         System.out.println("\nStep 2: Finding and Assigning Driver");
         final Optional<Driver> selectedDriver = this.selectDriverForOrder(sampleOrder);
         this.assignOrderToDriver(sampleOrder, selectedDriver);
         System.out.println("Current Status: " + this.getOrderStatus(sampleOrder.getOrderId()));

         // Step 3: Complete the delivery
         System.out.println("\nStep 3: Completing Delivery");
         if (selectedDriver.isPresent()) {
            this.completeDelivery(sampleOrder.getOrderId(), selectedDriver.get().getId());

            // Step 4: Rate the driver
            System.out.println("\nStep 4: Rating Driver");
            this.manageDriverRatings(selectedDriver.get(), 5);
         }

         System.out.println("\nFinal Order Status: " + this.getOrderStatus(sampleOrder.getOrderId()));
         System.out.println("\n=== Demonstration Complete ===\n");

      } catch (final Exception e) {
         System.err.println("Demonstration failed: " + e.getMessage());
         throw new OrderProcessingException("Demonstration failed", e);
      }
   }
}
