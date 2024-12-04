package app;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.Driver;
import model.MenuItem;
import model.Order;
import queue.OrderQueue;
import services.DriverService;
import services.DriverServiceImpl;

/**
 * The DeliverySystem class handles the core functionality of the online food delivery system.
 */
public class DeliverySystem {
   private static final Logger logger = Logger.getLogger(DeliverySystem.class.getName());
   private final Map<Long, String> orderStatuses = new HashMap<>();
   private final OrderQueue orderQueue = new OrderQueue(10); // Ensure max queue size is 10 and is FIFO

   /**
    * Submits an order to the system.
    *
    * @param order the order to be submitted
    */
   public void submitOrder(final Order order) {
      try {
         this.orderQueue.enqueue(order);
         System.out.println("Order submitted: " + order.getOrderId());
         this.orderStatuses.put(order.getOrderId(), "Pending");
      } catch (final CustomException.QueueFullException e) {
         DeliverySystem.logger.log(Level.SEVERE, "Order queue is full", e);
         System.err.println("Order queue is full: " + e.getMessage());
      } catch (final Exception e) {
         DeliverySystem.logger.log(Level.SEVERE, "An error occurred while submitting the order", e);
         System.err.println("An error occurred while submitting the order: " + e.getMessage());
      }
   }

   /**
    * Assigns an order to a driver.
    *
    * @param order  the order to be assigned
    * @param driver the driver to whom the order is assigned
    */
   public void assignOrderToDriver(final Order order, final Driver driver) {
      try {
         System.out.println("Order " + order.getOrderId() + " assigned to driver " + driver.getName());
         this.orderStatuses.put(order.getOrderId(), "In Progress");
      } catch (final Exception e) {
         DeliverySystem.logger.log(Level.SEVERE, "An error occurred while assigning the order to the driver", e);
         System.err.println("An error occurred while assigning the order to the driver: " + e.getMessage());
      }
   }

   /**
    * Completes the delivery of an order.
    *
    * @param orderId  the ID of the order
    * @param driverId the ID of the driver
    */
   public void completeDelivery(final Long orderId, final Long driverId) {
      try {
         System.out.println("Delivery completed for order " + orderId + " by driver " + driverId);
         this.orderStatuses.put(orderId, "Delivered");
      } catch (final Exception e) {
         DeliverySystem.logger.log(Level.SEVERE, "An error occurred while completing the delivery", e);
         System.err.println("An error occurred while completing the delivery: " + e.getMessage());
      }
   }

   /**
    * Gets the status of an order.
    *
    * @param orderId the ID of the order
    * @return the status of the order
    */
   public String getOrderStatus(final Long orderId) {
      return this.orderStatuses.getOrDefault(orderId, "Order Not Found");
   }

   /**
    * Calculates the total amount for an order.
    *
    * @param order the order
    * @return the total amount
    */
   public double calculateOrderTotal(final Order order) {
      return order.getItems().stream()
            .mapToDouble(MenuItem::getPrice)
            .sum();
   }

   /**
    * Manages the ratings for a driver.
    *
    * @param driver the driver
    * @param rating the rating to be added
    */
   public void manageDriverRatings(final Driver driver, final int rating) {
      try {
         driver.addRating(rating);
         System.out.println("Driver " + driver.getName() + " rated with " + rating + " stars.");
      } catch (final IllegalArgumentException e) {
         DeliverySystem.logger.log(Level.SEVERE, "Invalid rating provided", e);
         System.err.println("Invalid rating: " + e.getMessage());
      } catch (final Exception e) {
         DeliverySystem.logger.log(Level.SEVERE, "An error occurred while managing driver ratings", e);
         System.err.println("An error occurred while managing driver ratings: " + e.getMessage());
      }
   }

   /**
    * Processes orders in the correct order from the queue.
    *
    * @param orderQueue the order queue
    */
   public void processOrdersInCorrectOrder(final OrderQueue orderQueue) {
      while (!orderQueue.isEmpty()) {
         final Order order = orderQueue.dequeue().orElse(null);
         if (order != null) {
            System.out.println("Processing order: " + order.getOrderId());
            // Process the order
         }
      }
   }

   /**
    * Processes the next order in the queue.
    */
   public void processNextOrder() {
      final Order order = this.orderQueue.dequeue().orElse(null);
      if (order != null) {
         // Assign the order to a driver (you may need to implement driver selection logic)
         final Driver driver = this.selectDriverForOrder(order);
         this.assignOrderToDriver(order, driver);
         System.out.println("Order " + order.getOrderId() + " is now being processed.");
      } else {
         System.out.println("No orders to process.");
      }
   }

   /**
    * Selects a driver for an order.
    *
    * @param order the order
    * @return the selected driver
    */
   private Driver selectDriverForOrder(final Order order) {
      // TODO implement actual logic to find an available driver
      // {{ edit_1 }}: Implement logic to select a driver based on order requirements
      final Driver availableDriver = this.findAvailableDriver();
      if (availableDriver == null) {
         throw new IllegalStateException("No available drivers to assign to the order.");
      }
      return availableDriver;
   }

   /**
    * Finds an available driver.
    *
    * @return the available driver, or null if no drivers are available
    */
   private Driver findAvailableDriver() {
      final DriverService driverService = new DriverServiceImpl(); // Assuming you have a way to get the DriverService
      final List<Driver> availableDrivers = driverService.getAvailableDrivers();
      if (availableDrivers.isEmpty()) {
         return null; // No available drivers
      }
      return availableDrivers.get(0); // Return the first available driver (you can implement more complex logic if needed)
   }
}
