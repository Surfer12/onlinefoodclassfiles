package services;

import java.util.List;
import java.util.Scanner;

import managers.MenuManager;
import model.Location;
import model.MenuItem;
import model.Order;
import queue.OrderQueue;
import validation.ConsoleInputHandler;

public class OrderManagerImpl implements OrderManager {
    private final OrderQueue orderQueue;

    public OrderManagerImpl() {
        this.orderQueue = new OrderQueue(10); // Default queue size of 10
    }

    @Override
    public void processOrderPlacement(
            final Scanner scanner,
            final MenuManager menuManager,
            final ConsoleInputHandler<Integer> positiveIntegerHandler,
            final ConsoleInputHandler<String> emailHandler,
            final ConsoleInputHandler<String> locationHandler) {
        // Use emailHandler to get and validate email
        final String email = emailHandler.getInput("Enter your email: ");

        // Use locationHandler to get and validate delivery location
        final String locationAddress = locationHandler.getInput("Enter delivery location: ");
        final String postalCode = locationHandler.getInput("Enter postal code: ");
        final Location deliveryLocation = new Location(locationAddress, postalCode);

        // Use the correct method to get menu items
        final List<MenuItem> orderItems = menuManager.selectMenuItems(scanner, positiveIntegerHandler);

        // Create an order with the collected information
        final Order order = new Order(null, email, orderItems, deliveryLocation);

        // Add order to queue or process further
        this.orderQueue.enqueue(order);
    }

    @Override
    public OrderQueue getOrderQueue() {
        return this.orderQueue;
    }
}
