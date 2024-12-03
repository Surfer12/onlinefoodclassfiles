package app;

import java.util.HashMap;
import java.util.Map;

import model.Driver;
import model.Order;

public class DeliverySystem {
   private final Map<Long, String> orderStatuses = new HashMap<>();

   public void submitOrder(final Order order) {
      System.out.println("Order submitted: " + order.getOrderId());
      this.orderStatuses.put(order.getOrderId(), "Pending");
   }

   public void assignOrderToDriver(final Order order, final Driver driver) {
      System.out.println("Order " + order.getOrderId() + " assigned to driver " + driver.getName());
      this.orderStatuses.put(order.getOrderId(), "In Progress");
   }

   public void completeDelivery(final Long orderId, final Long driverId) {
      System.out.println("Delivery completed for order " + orderId + " by driver " + driverId);
      this.orderStatuses.put(orderId, "Delivered");
   }

   public String getOrderStatus(final Long orderId) {
      return this.orderStatuses.getOrDefault(orderId, "Order Not Found");
   }

   public double calculateOrderTotal(Order order) {
      return order.getItems().stream()
            .mapToDouble(item -> item.getPrice())
            .sum();
   }

   public void manageDriverRatings(Driver driver, int rating) {
      driver.addRating(rating);
      System.out.println("Driver " + driver.getName() + " rated with " + rating + " stars.");
   }

   public void processOrdersInCorrectOrder(OrderQueue orderQueue) {
      while (!orderQueue.isEmpty()) {
         Order order = orderQueue.dequeue().orElse(null);
         if (order != null) {
            System.out.println("Processing order: " + order.getOrderId());
            // Process the order
         }
      }
   }
}
