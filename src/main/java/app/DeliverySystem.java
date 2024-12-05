package app;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import CustomException.OrderProcessingException;
import managers.OrderStatusManager;
import model.Driver;
import model.Order;
import model.OrderStatus;
import notification.NotificationService;
import queue.OrderQueue;
import services.DriverService;
import services.DriverServiceImpl;

public class DeliverySystem {
   private final Map<Long, String> orderStatuses = new HashMap<>();
   private final NotificationService notificationService;
   private final OrderStatusManager statusManager;

   public DeliverySystem(final NotificationService notificationService, final OrderStatusManager statusManager) {
      this.notificationService = notificationService;
      this.statusManager = statusManager;
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

   public void updateOrderStatus(final Order order, final OrderStatus newStatus) {
      try {
         this.statusManager.updateOrderStatus(order, newStatus);
         this.orderStatuses.put(order.getOrderId(), newStatus.toString());
      } catch (final IllegalStateException e) {
         throw new OrderProcessingException("Failed to update order status: " + e.getMessage(), e);
      }
   }

   private boolean isValidStatusTransition(final OrderStatus current, final OrderStatus next) {
      // Define valid transitions
      return switch (current) {
         case SUBMITTED -> next == OrderStatus.IN_PROGRESS;
         case IN_PROGRESS -> next == OrderStatus.OUT_FOR_DELIVERY;
         case OUT_FOR_DELIVERY -> next == OrderStatus.DELIVERED;
         default -> false;
      };
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
         this.notificationService.sendDeliveryCompletionNotification(orderId);
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
      orderQueue.getPendingOrders().stream()
         .forEach(order -> {
            System.out.println("Processing order: " + order.getOrderId());
            final Optional<Driver> driver = this.selectDriverForOrder(order);
            this.assignOrderToDriver(order, driver);
         });
   }

   public Optional<Driver> selectDriverForOrder(final Order order) {
      return this.findAvailableDriverForOrderType(order);
   }

   private Optional<Driver> findAvailableDriverForOrderType(final Order order) {
      final DriverService driverService = new DriverServiceImpl();
      return driverService.getAvailableDrivers().stream().findFirst();
   }
}
