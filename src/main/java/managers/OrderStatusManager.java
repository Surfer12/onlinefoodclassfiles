package managers;

import java.util.ArrayList;
import java.util.List;

import model.Order;
import model.OrderStatus;
import notification.NotificationService;
import observer.OrderObserver;

public class OrderStatusManager {
    private final NotificationService notificationService;
    private final List<OrderObserver> observers = new ArrayList<>();

    public OrderStatusManager(final NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public void updateOrderStatus(final Order order, final OrderStatus newStatus) {
        final OrderStatus oldStatus = order.getStatus();
        if (this.isValidStatusTransition(oldStatus, newStatus)) {
            order.setStatus(newStatus);
            this.notificationService.sendOrderStatusUpdateToCustomer(order, newStatus);
            this.notifyObservers(order, newStatus);
        } else {
            throw new IllegalStateException(
                    String.format("Invalid status transition from %s to %s", oldStatus, newStatus));
        }
    }

    private boolean isValidStatusTransition(final OrderStatus current, final OrderStatus next) {
        return switch (current) {
            case SUBMITTED -> next == OrderStatus.PENDING || next == OrderStatus.CANCELLED;
            case PENDING -> next == OrderStatus.IN_PROGRESS || next == OrderStatus.CANCELLED;
            case IN_PROGRESS -> next == OrderStatus.PREPARING || next == OrderStatus.CANCELLED;
            case PREPARING -> next == OrderStatus.OUT_FOR_DELIVERY || next == OrderStatus.CANCELLED;
            case OUT_FOR_DELIVERY -> next == OrderStatus.DELIVERED || next == OrderStatus.CANCELLED;
            case DELIVERED, CANCELLED -> false;
            default -> false;
        };
    }

    private void notifyObservers(final Order order, final OrderStatus newStatus) {
        for (final OrderObserver observer : this.observers) {
            observer.onOrderUpdated(order, newStatus);
        }
    }

    public void addObserver(final OrderObserver observer) {
        this.observers.add(observer);
    }

    public void removeObserver(final OrderObserver observer) {
        this.observers.remove(observer);
    }
}
