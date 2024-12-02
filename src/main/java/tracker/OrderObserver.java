package tracker;

import model.Order;
import model.OrderStatus;

public interface OrderObserver {
    void update(Order order, OrderStatus newStatus);
}