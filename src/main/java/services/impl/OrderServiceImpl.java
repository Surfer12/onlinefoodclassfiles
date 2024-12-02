package services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.MenuItem;
import model.Order;
import services.OrderService;

public class OrderServiceImpl implements OrderService {
    private List<Order> orders = new ArrayList<>();

    @Override
    public Order getOrderById(Long orderId) {
        return this.orders.stream()
                     .filter(order -> order.getId().equals(orderId))
                     .findFirst()
                     .orElse(null);
    }

    @Override
    public Order createOrder(List<MenuItem> items) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one item");
        }

        Order newOrder = new Order(0L, "New Order", items, null);
        this.orders.add(newOrder);
        return newOrder;
    }

    @Override
    public void displayOrderDetails(Order order) {
        System.out.println("Order Details:");
        System.out.println("Order ID: " + order.getOrderId());
        System.out.println("Items:");

        Map<MenuItem, Integer> itemCounts = new HashMap<>();
        double totalPrice = 0;

        for (MenuItem item : order.getItems()) {
            itemCounts.put(item, itemCounts.getOrDefault(item, 0) + 1);
            totalPrice += item.getPrice();
        }

        itemCounts.forEach(
                (item, count) -> System.out.printf("%s x%d - $%.2f\n", item.getName(), count, item.getPrice() * count));

        System.out.printf("Total Price: $%.2f\n", totalPrice);
    }

    @Override
    public String getOrderStatus(Long orderId) {
        Order order = this.getOrderById(orderId);
        return order != null ? "Order in progress" : "Order not found";
    }
}