package services;

import model.OrderStatus;

public interface OrderStatusService {
    void updateOrderStatus(Long orderId, OrderStatus newStatus);

    OrderStatus getOrderStatus(Long orderId);
}
