package app;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import CustomException.OrderProcessingException;
import model.Driver;
import model.Order;
import queue.OrderQueue;
import services.DriverService;
import services.DriverServiceImpl;

public class DeliverySystem {
   private final Map<Long, String> orderStatuses = new HashMap<>();

   public void submitOrder(final Order order) {
      try {
         System.out.println("Order submitted: " + order.getOrderId());
         this.orderStatuses.put(order.getOrderId(), "Pending");
      } catch (Exception e) {
         throw new OrderProcessingException("Failed to submit order: " + e.getMessage(), e);
      }
   }

   public void assignOrderToDriver(final Order order, final Driver driver) {
      try {
         System.out.println("Order " + order.getOrderId() + " assigned to driver " + driver.getName());
         this.orderStatuses.put(order.getOrderId(), "In Progress");
      } catch (Exception e) {
         throw new OrderProcessingException("Failed to assign order to driver: " + e.getMessage(), e);
      }
   }

   public void completeDelivery(final Long orderId, final Long driverId) {
      try {
         System.out.println("Delivery completed for order " + orderId + " by driver " + driverId);
         this.orderStatuses.put(orderId, "Delivered");
      } catch (Exception e) {
         throw new OrderProcessingException("Failed to complete delivery: " + e.getMessage(), e);
      }
   }

   public String getOrderStatus(final Long orderId) {
      return this.orderStatuses.getOrDefault(orderId, "Order Not Found");
   }
  public double calculateOrderTotal(final Order order) {
      return order.getItems().stream()
            .mapToDouble(MenuItem::getPrice)
            .sum();
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
         final Driver driver = this.selectDriverForOrder(order);
         this.assignOrderToDriver(order, driver);
         System.out.println("Order " + order.getOrderId() + " is now being processed.");
      } else {
         System.out.println("No orders to process.");
      }
   }

   private Driver selectDriverForOrder(final Order order) {
      final Driver availableDriver = this.findAvailableDriver();
      if (availableDriver == null) {
         throw new OrderProcessingException("No available drivers to assign to the order.");
      }
      return availableDriver;
   }

   private Driver findAvailableDriver() {
      final DriverService driverService = new DriverServiceImpl(); // Assuming you have a way to get the DriverService
      final List<Driver> availableDrivers = driverService.getAvailableDrivers();
      if (availableDrivers.isEmpty()) {
         return null; // No available drivers
      }
      return availableDrivers.get(0); // Return the first available driver (you can implement more complex logic if needed)
   }
}
