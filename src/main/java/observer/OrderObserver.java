package observer;

import model.Order;

public interface OrderObserver {
    void update(Order order);

    void onOrderEvent(Order order, OrderEvent event);

    void customerNotificationOfOrder(Order order, OrderEvent event);

    void driverNotificationToCustomer(Order order, OrderEvent event);
}
