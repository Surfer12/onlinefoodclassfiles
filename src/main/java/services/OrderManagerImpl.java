package services;

import java.util.Scanner;

import managers.MenuManager;
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

        // Existing code to process the order with email and location...
    }

    @Override
    public OrderQueue getOrderQueue() {
        return this.orderQueue;
    }
}
