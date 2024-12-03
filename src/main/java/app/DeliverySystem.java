package app;

import java.util.HashMap;
import java.util.Map;

import model.Driver;
import model.Order;
import queue.OrderQueue;

public class DeliverySystem {
   private final Map<Long, String> orderStatuses = new HashMap<>();
   private final OrderQueue orderQueue = new OrderQueue(10);

   public void submitOrder(final Order order) {
      System.out.println("Order submitted: " + order.getOrderId());
      this.orderQueue.enqueue(order);
      this.orderStatuses.put(order.getOrderId(), "Pending");
   }

   public void assignOrderToDriver(final Order order, final Driver driver) {
      Order nextOrder = this.orderQueue.dequeue().orElse(null);
      if (nextOrder != null) {
         System.out.println("Order " + nextOrder.getOrderId() + " assigned to driver " + driver.getName());
         this.orderStatuses.put(nextOrder.getOrderId(), "In Progress");
      } else {
         System.out.println("No orders in the queue to assign.");
      }
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
