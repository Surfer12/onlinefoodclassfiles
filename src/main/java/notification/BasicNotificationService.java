package notification;

import model.Driver;
import model.Order;
import model.OrderStatus;

/**
 * Basic implementation of the NotificationService interface.
 */
public class BasicNotificationService implements NotificationService {
   private static final String ORDER_CONFIRMATION_SUBJECT = "Order Confirmation";
   private static final String DRIVER_ASSIGNMENT_SUBJECT = "Driver Assigned";
   private static final String ORDER_STATUS_UPDATE_SUBJECT = "Order Status Update";
   private static final String DELIVERY_COMPLETION_SUBJECT = "Delivery Complete";

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
      final String message = this.formatOrderConfirmationMessage(order);
      this.sendEmail(order.getCustomerEmail(), BasicNotificationService.ORDER_CONFIRMATION_SUBJECT, message);
   }

   /**
    * Sends a notification to the customer about the driver assignment.
    *
    * @param order  the order for which the driver is assigned
    * @param driver the driver assigned to the order
    */
   @Override
   public void sendDriverAssignmentNotification(final Order order, final Driver driver) {
      final String message = this.formatDriverAssignmentMessage(order, driver);
      this.sendEmail(order.getCustomerEmail(), BasicNotificationService.DRIVER_ASSIGNMENT_SUBJECT, message);
   }

   /**
    * Sends an order status update notification to the customer.
    *
    * @param order     the order for which the status update is to be sent
    * @param newStatus the new status of the order
    */
   @Override
   public void sendOrderStatusUpdateToCustomer(final Order order, final OrderStatus newStatus) {
      final String message = this.formatStatusUpdateMessage(order, newStatus);
      this.sendEmail(order.getCustomerEmail(), BasicNotificationService.ORDER_STATUS_UPDATE_SUBJECT, message);
   }

   /**
    * Sends a delivery completion notification to the customer.
    *
    * @param order the order for which the delivery is completed
    */
   @Override
   public void sendDeliveryCompletionNotification(final Long orderId) {
      final String message = String.format("Delivery completed for order #%d", orderId);
      this.sendEmail("customer@example.com", BasicNotificationService.DELIVERY_COMPLETION_SUBJECT, message);
   }

   private String formatOrderConfirmationMessage(final Order order) {
      return String.format(
            "Order #%d confirmed. Total: $%.2f",
            order.getOrderId(),
            order.getTotalAmount());
   }

   private String formatDriverAssignmentMessage(final Order order, final Driver driver) {
      return String.format(
            "Driver %s has been assigned to your order. %s",
            driver.getName(),
            this.formatEstimatedDeliveryTime(order));
   }

   private String formatEstimatedDeliveryTime(final Order order) {
      return order.getEstimatedDeliveryTime() != null
            ? "Estimated delivery time: " + order.getEstimatedDeliveryTime()
            : "Delivery time to be determined";
   }

   private String formatStatusUpdateMessage(final Order order, final OrderStatus newStatus) {
      return String.format("Order #%d status updated: %s",
            order.getOrderId(),
            newStatus);
   }

   private String formatDeliveryCompletionMessage(final Order order) {
      return String.format("Order #%d has been delivered successfully.",
            order.getOrderId());
   }

   private void sendEmail(final String recipientEmail, final String subject, final String message) {
      try {
         System.out.printf("Sending email to %s%nSubject: %s%nMessage: %s%n",
               recipientEmail, subject, message);
      } catch (final Exception e) {
         System.err.printf("Failed to send email to %s%nSubject: %s%nMessage: %s%nError: %s%n",
               recipientEmail, subject, message, e.getMessage());
         this.sendSMS(recipientEmail, message);
      }
   }

   private void sendSMS(final String recipientPhone, final String message) {
      try {
         System.out.printf("Sending SMS to %s%nMessage: %s%n",
               recipientPhone, message);
      } catch (final Exception e) {
         System.err.printf("Failed to send SMS to %s%nMessage: %s%nError: %s%n",
               recipientPhone, message, e.getMessage());
      }
   }
}
