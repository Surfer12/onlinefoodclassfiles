
package managers;

import java.util.ArrayList;
import java.util.List;

import model.Order;
import model.OrderStatus;
import notification.NotificationService;

public class OrderStatusManager {
    private final NotificationService notificationService;
    private final List<OrderObserver> observers = new ArrayList<>();

    public OrderStatusManager(final NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public void updateStatus(final Order order, final OrderStatus newStatus) {
        final OrderStatus oldStatus = order.getStatus();
        validateStatusTransition(oldStatus, newStatus);

        order.setStatus(newStatus);
        this.notificationService.sendOrderStatusUpdateToCustomer(order, newStatus);

        // Notify observers
        this.observers.forEach(observer -> observer.onOrderUpdated(order, newStatus));
    }

    public void addObserver(final OrderObserver observer) {
        this.observers.add(observer);
    }

    public void removeObserver(final OrderObserver observer) {
        this.observers.remove(observer);
    }
}
