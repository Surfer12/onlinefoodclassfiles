package services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Driver;
import model.Location;
import model.MenuItem;
import model.Order;
import model.OrderStatus;

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
    public Order createOrder(final List<MenuItem> items, final Location deliveryLocation) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one item");
        }

        final Order newOrder = new Order(0L, "New Order", items, deliveryLocation);
        newOrder.setStatus(OrderStatus.SUBMITTED);
        this.orders.add(newOrder);
        return newOrder;
    }

    @Override
    public void displayOrderDetails(final Order order) {
        System.out.println("Order Details:");
        System.out.println("Order ID: " + order.getOrderId());
        System.out.println("Items:");

        final Map<MenuItem, Integer> itemCounts = new HashMap<>();
        double totalPrice = 0;

        for (final MenuItem item : order.getItems()) {
            itemCounts.put(item, itemCounts.getOrDefault(item, 0) + 1);
            totalPrice += item.getPrice();
        }

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

    public void processOrder(final Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
        this.orders.add(order);
        System.out.println("Order processed: " + order.getOrderId());
    }

    public void rateDriver(final Driver driver, final int rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        driver.addRating(rating);
        System.out.println("Driver " + driver.getName() + " rated: " + rating + " stars");
    }

    @Override
    public List<Order> getAllOrders() {
        return this.orders;
    }
}
