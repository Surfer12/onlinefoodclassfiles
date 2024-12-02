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
      this.validateOrderUpdateRequest(orderId, newStatus);
      this.updateStatusInDatabase(orderId, newStatus);
      this.updateDeliveryEstimates(orderId, assignedDriver);
   }

   private void validateOrderUpdateRequest(final Long orderId, final OrderStatus newStatus) {
      // Validation logic
   }

   private boolean isDeliveryInProgress(final OrderStatus status) {
      return status == OrderStatus.OUT_FOR_DELIVERY;
   }

   private void updateStatusInDatabase(final Long orderId, final OrderStatus newStatus) {
      this.orderStatuses.put(orderId, newStatus);
   }

   private void updateDeliveryEstimates(final Long orderId, final Driver driver) {
      final LocalDateTime estimatedTime = this.calculateEstimatedDeliveryTime(driver);
      this.estimatedDeliveryTimes.put(orderId, estimatedTime);
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