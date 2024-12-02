package notification;

import model.Driver;
import model.Order;
import model.OrderStatus;

public interface NotificationService {
   void sendNotification(String message);

   void sendOrderConfirmationToCustomer(Order order);

   void sendDriverAssignmentNotification(Order order, Driver driver);

   void sendOrderStatusUpdateToCustomer(Order order, OrderStatus newStatus);

   void sendDeliveryCompletionNotification(Order order);
}
