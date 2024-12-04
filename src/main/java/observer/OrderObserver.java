package observer;

import model.Order;

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
     * Sends a notification to the customer about the order event.
     *
     * @param order the order associated with the event
     * @param event the event that occurred
     */
    void customerNotificationOfOrder(Order order, OrderEvent event);

    /**
     * Sends a notification to the customer about the driver assignment.
     *
     * @param order the order associated with the event
     * @param event the event that occurred
     */
    void driverNotificationToCustomer(Order order, OrderEvent event);
}
