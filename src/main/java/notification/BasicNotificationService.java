package notification;

import model.Driver;
import model.Order;
import model.OrderStatus;

public class BasicNotificationService implements NotificationService {
   private static final String ORDER_CONFIRMATION_SUBJECT = "Order Confirmation";
   private static final String DRIVER_ASSIGNMENT_SUBJECT = "Driver Assigned";
   private static final String ORDER_STATUS_UPDATE_SUBJECT = "Order Status Update";
   private static final String DELIVERY_COMPLETION_SUBJECT = "Delivery Complete";

   @Override
   public void sendNotification(final String message) {
      System.out.println("Notification: " + message);
   }

   @Override
   public void sendOrderConfirmationToCustomer(final Order order) {
      final String message = this.formatOrderConfirmationMessage(order);
      this.sendEmail(order.getCustomerEmail(), BasicNotificationService.ORDER_CONFIRMATION_SUBJECT, message);
   }

   @Override
   public void sendDriverAssignmentNotification(final Order order, final Driver driver) {
      final String message = this.formatDriverAssignmentMessage(order, driver);
      this.sendEmail(order.getCustomerEmail(), BasicNotificationService.DRIVER_ASSIGNMENT_SUBJECT, message);
   }

   @Override
   public void sendOrderStatusUpdateToCustomer(final Order order, final OrderStatus newStatus) {
      final String message = this.formatStatusUpdateMessage(order, newStatus);
      this.sendEmail(order.getCustomerEmail(), BasicNotificationService.ORDER_STATUS_UPDATE_SUBJECT, message);
   }

   @Override
   public void sendDeliveryCompletionNotification(final Order order) {
      final String message = this.formatDeliveryCompletionMessage(order);
      this.sendEmail(order.getCustomerEmail(), BasicNotificationService.DELIVERY_COMPLETION_SUBJECT, message);
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