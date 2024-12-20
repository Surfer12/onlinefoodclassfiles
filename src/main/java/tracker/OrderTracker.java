package tracker;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import model.Driver;
import model.Order;
import model.OrderStatus;

public class OrderTracker implements OrderSubject {
   private final Map<Long, OrderStatus> orderStatuses;
   private final Map<Long, LocalDateTime> estimatedDeliveryTimes;
   private final List<OrderObserver> observers;
   private final Map<Long, Order> orders = new ConcurrentHashMap<>();

   public OrderTracker() {
      this.orderStatuses = new ConcurrentHashMap<>();
      this.estimatedDeliveryTimes = new ConcurrentHashMap<>();
      this.observers = new ArrayList<>();
   }

   @Override
   public void attach(final OrderObserver observer) {
      this.observers.add(observer);
   }

   @Override
   public void detach(final OrderObserver observer) {
      this.observers.remove(observer);
   }

   @Override
   public void notifyObservers(final Order order) {
      for (final OrderObserver observer : this.observers) {
         observer.update(order, this.orderStatuses.get(order.getOrderId()));
      }
   }

   public void updateOrderStatus(final Long orderId, final OrderStatus newStatus, final Driver assignedDriver) {
      if (this.isValidStatusTransition(orderId, newStatus)) {
         this.updateStatusInDatabase(orderId, newStatus);
         this.updateDeliveryEstimates(orderId, assignedDriver);
         this.notifyObserversAboutOrderUpdate(orderId);
      }
   }

   public void addOrder(final Order order) {
      this.orders.put(order.getOrderId(), order);
      this.orderStatuses.put(order.getOrderId(), order.getStatus());
   }

   public void removeOrder(final Long orderId) {
      this.orders.remove(orderId);
      this.orderStatuses.remove(orderId);
      this.estimatedDeliveryTimes.remove(orderId);
   }

   private boolean isValidStatusTransition(final Long orderId, final OrderStatus newStatus) {
      final OrderStatus currentStatus = this.orderStatuses.get(orderId);
      if (currentStatus == null) {
         return true; // Allow any status for new orders
      }

      return switch (currentStatus) {
         case SUBMITTED -> newStatus == OrderStatus.PENDING || newStatus == OrderStatus.CANCELLED;
         case PENDING -> newStatus == OrderStatus.IN_PROGRESS || newStatus == OrderStatus.CANCELLED;
         case IN_PROGRESS -> newStatus == OrderStatus.PREPARING || newStatus == OrderStatus.CANCELLED;
         case PREPARING -> newStatus == OrderStatus.OUT_FOR_DELIVERY || newStatus == OrderStatus.CANCELLED;
         case OUT_FOR_DELIVERY -> newStatus == OrderStatus.DELIVERED || newStatus == OrderStatus.CANCELLED;
         case DELIVERED, CANCELLED -> false; // Terminal states
         default -> false;
      };
   }

   private void updateStatusInDatabase(final Long orderId, final OrderStatus newStatus) {
      this.orderStatuses.put(orderId, newStatus);
      final Order order = this.orders.get(orderId);
      if (order != null) {
         order.setStatus(newStatus);
      }
   }

   private void updateDeliveryEstimates(final Long orderId, final Driver assignedDriver) {
      if (assignedDriver != null) {
         final LocalDateTime estimatedTime = this.calculateEstimatedDeliveryTime(assignedDriver);
         this.estimatedDeliveryTimes.put(orderId, estimatedTime);
         final Order order = this.orders.get(orderId);
         if (order != null) {
            order.setDriver(Optional.of(assignedDriver));
         }
      }
   }

   public Optional<OrderStatus> getOrderStatus(final Long orderId) {
      return Optional.ofNullable(this.orderStatuses.get(orderId));
   }

   public Optional<LocalDateTime> getEstimatedDeliveryTime(final Long orderId) {
      return Optional.ofNullable(this.estimatedDeliveryTimes.get(orderId));
   }

   private Optional<Order> findOrderById(final Long orderId) {
      return Optional.ofNullable(this.orders.get(orderId));
   }

   private void notifyObserversAboutOrderUpdate(final Long orderId) {
      this.findOrderById(orderId).ifPresent(this::notifyObservers);
   }

   private LocalDateTime calculateEstimatedDeliveryTime(final Driver driver) {
      // Placeholder implementation
      return LocalDateTime.now().plusMinutes(30);
   }
}
