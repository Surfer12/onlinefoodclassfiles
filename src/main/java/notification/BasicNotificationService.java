package notification;

import model.Driver;
import model.Order;
import model.OrderStatus;

/**
 * Basic implementation of the NotificationService interface.
 */
public class BasicNotificationService implements NotificationService {

    /**
     * Sends a generic notification message.
     *
     * @param message the message to be sent
     */
    @Override
    public void sendNotification(final String message) {
        System.out.println("Notification: " + message);
    }

    /**
     * Sends an order confirmation notification to the customer.
     *
     * @param order the order for which the confirmation is to be sent
     */
    @Override
    public void sendOrderConfirmationToCustomer(final Order order) {
        this.sendNotification(String.format("Order confirmation sent to customer for order ID: %d", order.getOrderId()));
    }

    /**
     * Sends a notification to the customer about the driver assignment.
     *
     * @param order  the order for which the driver is assigned
     * @param driver the driver assigned to the order
     */
    @Override
    public void sendDriverAssignmentNotification(final Order order, final Driver driver) {
        this.sendNotification(String.format("Driver %s assigned to order ID: %d", driver.getName(), order.getOrderId()));
    }

    /**
     * Sends an order status update notification to the customer.
     *
     * @param order     the order for which the status update is to be sent
     * @param newStatus the new status of the order
     */
    @Override
    public void sendOrderStatusUpdateToCustomer(final Order order, final OrderStatus newStatus) {
        this.sendNotification(String.format("Order %d status updated to: %s", order.getOrderId(), newStatus));
    }

    /**
     * Sends a delivery completion notification to the customer.
     *
     * @param order the order for which the delivery is completed
     */
    @Override
    public void sendDeliveryCompletionNotification(final Long orderId) {
        this.sendNotification(String.format("Delivery completed for order ID: %d", orderId));
    }

    @Override
    public void sendDeliveryCompletionNotification(Order order) {
        // TODO Auto-generated method stub

    }
}
