package app;

import java.util.HashMap;
import java.util.Map;

import model.Driver;
import model.Order;
import model.OrderStatus;

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

   public void updateOrderStatus(final Long orderId, final OrderStatus status) {
      switch (status) {
         case PLACED:
            this.orderStatuses.put(orderId, "Placed");
            break;
         case ACCEPTED:
            this.orderStatuses.put(orderId, "Accepted");
            break;
         case DELIVERED:
            this.orderStatuses.put(orderId, "Delivered");
            break;
         default:
            throw new IllegalArgumentException("Unknown order status: " + status);
      }
   }
}
