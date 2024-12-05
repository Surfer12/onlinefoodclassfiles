package observer;

import model.Driver;
import model.Order;
import model.OrderStatus;

/**
 * Interface for observing order events.
 */
public interface OrderObserver {

    /**
     * Updates the observer with the latest order information.
     *
     * @param order the order to update
     */
    void update(Order order);

    /**
     * Handles order events and performs actions based on the event type.
     *
     * @param order the order associated with the event
     * @param event the event that occurred
     */
    void onOrderEvent(Order order, OrderEvent event);

    /**
     * Handles order status updates.
     *
     * @param order     the order to update
     * @param newStatus the new status of the order
     */
    void onOrderUpdated(Order order, OrderStatus newStatus);

    /**
     * Handles driver assignments.
     *
     * @param order  the order to update
     * @param driver the assigned driver
     */
    void onDriverAssigned(Order order, Driver driver);

    /**
     * Handles delivery completion.
     *
     * @param order the order to update
     */
    void onDeliveryCompleted(Order order);
}
