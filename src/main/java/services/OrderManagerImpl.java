package services;

import java.util.List;
import java.util.Scanner;

import managers.MenuManager;
import model.MenuItem;
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
        final String location = locationHandler.getInput("Enter delivery location: ");

        // Use the correct method to get menu items
        final List<MenuItem> orderItems = menuManager.selectMenuItems(scanner, positiveIntegerHandler);

        // Additional order processing logic...
    }

    @Override
    public OrderQueue getOrderQueue() {
        return this.orderQueue;
    }
}
