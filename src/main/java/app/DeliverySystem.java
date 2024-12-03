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

   public void rateDriver(final Driver driver, final int rating) {
      if (rating < 1 || rating > 5) {
         throw new IllegalArgumentException("Rating must be between 1 and 5");
      }
      driver.addRating(rating);
      System.out.println("Driver " + driver.getName() + " rated: " + rating + " stars");
   }

   public void processOrder(final Order order) {
      if (order == null) {
         throw new IllegalArgumentException("Order cannot be null");
      }
      this.submitOrder(order);
      System.out.println("Order processed: " + order.getOrderId());
   }
}
