package notification;

import model.Driver;
import model.Order;
import model.OrderStatus;

public interface NotificationService {
   /**
    * Sends a generic notification message.
    *
    * @param message the message to be sent
    */
   void sendNotification(String message);

   /**
    * Sends an order confirmation notification to the customer.
    *
    * @param order the order for which the confirmation is to be sent
    */
   void sendOrderConfirmationToCustomer(Order order);

   /**
    * Sends a notification to the customer about the driver assignment.
    *
    * @param order  the order for which the driver is assigned
    * @param driver the driver assigned to the order
    */
   void sendDriverAssignmentNotification(Order order, Driver driver);

   /**
    * Sends an order status update notification to the customer.
    *
    * @param order     the order for which the status update is to be sent
    * @param newStatus the new status of the order
    */
   void sendOrderStatusUpdateToCustomer(Order order, OrderStatus newStatus);

   /**
    * Sends a delivery completion notification to the customer.
    *
    * @param order the order for which the delivery is completed
    */
   void sendDeliveryCompletionNotification(Order order);
}
