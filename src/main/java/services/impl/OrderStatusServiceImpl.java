package services.impl;

import java.util.HashMap;
import java.util.Map;

import model.OrderStatus;
import services.OrderStatusService;

public class OrderStatusServiceImpl implements OrderStatusService {
    private final Map<Long, OrderStatus> orderStatuses = new HashMap<>();

    @Override
    public void updateOrderStatus(final Long orderId, final OrderStatus newStatus) {
        if (orderId == null || newStatus == null) {
            throw new IllegalArgumentException("Order ID and status cannot be null");
        }
        this.orderStatuses.put(orderId, newStatus);
    }

    @Override
    public OrderStatus getOrderStatus(final Long orderId) {
        return this.orderStatuses.get(orderId);
    }
}
