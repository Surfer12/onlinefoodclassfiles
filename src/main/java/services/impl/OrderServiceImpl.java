package services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import model.Location;
import model.MenuItem;
import model.Order;
import model.OrderStatus;
import services.OrderService;

public class OrderServiceImpl implements OrderService {
    private final List<Order> orders = new ArrayList<>();

    @Override
    public Order getOrderById(final Long orderId) {
        return this.orders.stream()
                .filter(order -> order.getId().equals(orderId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Order createOrder(final List<MenuItem> items, final Long customerId) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one item");
        }

        final Location defaultLocation = new Location("Unknown Address", "00000");

        final Order newOrder = new Order(
            customerId,
            "customer@example.com",
            items,
            defaultLocation
        );
        newOrder.setStatus(OrderStatus.SUBMITTED);
        this.orders.add(newOrder);
        return newOrder;
    }

    @Override
    public void displayOrderDetails(final Order order) {
        System.out.println("Order Details:");
        System.out.println("Order ID: " + order.getOrderId());
        System.out.println("Items:");

        final Map<MenuItem, Long> itemCounts = order.getItems().stream()
                .collect(Collectors.groupingBy(item -> item, Collectors.counting()));

        final double totalPrice = order.getItems().stream()
                .mapToDouble(MenuItem::getPrice)
                .sum();

        itemCounts.forEach((item, count) -> System.out.printf("%s x%d - $%.2f\n",
                item.getName(),
                count,
                item.getPrice() * count));

        System.out.printf("Total Price: $%.2f\n", totalPrice);
    }

    @Override
    public String getOrderStatus(final Long orderId) {
        final Order order = this.getOrderById(orderId);
        return order != null ? order.getStatus().toString() : "Order not found";
    }

    @Override
    public List<Order> getAllOrders() {
        return new ArrayList<>(this.orders);
    }
}
