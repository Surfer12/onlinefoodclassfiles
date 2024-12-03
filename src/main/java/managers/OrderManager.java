package managers;

import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

import model.Driver;
import model.MenuItem;
import model.Order;
import queue.OrderQueue;
import services.OrderService;
import services.impl.OrderServiceImpl;
import validation.ConsoleInputHandler;
import validation.InputValidatorImpl;
import validation.PositiveLongValidator;

public class OrderManager {
    private static final Logger logger = Logger.getLogger(OrderManager.class.getName());
    private static final int MAX_QUEUE_SIZE = 10;

    private final OrderService orderService;
    private final OrderQueue orderQueue;
    private final ConsoleInputHandler<Long> orderIdHandler;

    public OrderManager() {
        this.orderService = new OrderServiceImpl();
        this.orderQueue = new OrderQueue(OrderManager.MAX_QUEUE_SIZE);
        this.orderIdHandler = new ConsoleInputHandler<>(
                new InputValidatorImpl<>(
                        new PositiveLongValidator(),
                        "Order ID",
                        "Invalid Order ID"));
    }

    public Order createOrder(final List<MenuItem> orderItems) throws CustomException.QueueFullException {
        if (orderItems.isEmpty()) {
            System.out.println("No items selected. Order cancelled.");
            return null;
        }

        try {
            final Order newOrder = this.orderService.createOrder(orderItems);
            this.orderQueue.enqueue(newOrder);
            this.orderService.displayOrderDetails(newOrder);
            System.out.println("Order placed successfully! Order ID: " + newOrder.getOrderId());
            OrderManager.logger.info("New order added to queue: " + newOrder.getOrderId());
            return newOrder;
        } catch (final CustomException.QueueFullException e) {
            System.out.println("Sorry, we are currently at maximum order capacity. Please try again later.");
            OrderManager.logger.warning("Order queue full: " + e.getMessage());
            throw e;
        }
    }

    public void checkOrderStatus(final Scanner scanner) {
        try {
            final Long orderId = this.orderIdHandler.handleInput(scanner, "Enter Order ID to check status: ");

            if (orderId == null)
                return;

            final Order order = this.orderService.getOrderById(orderId);
            if (order != null) {
                System.out.println("Order Status: " + order.getStatus());
            } else {
                System.out.println("Order not found.");
            }
        } catch (final Exception e) {
            System.out.println("Error checking order status: " + e.getMessage());
            OrderManager.logger.severe("Error in checkOrderStatus: " + e.getMessage());
        }
    }

    public ConsoleInputHandler<Long> getOrderIdHandler() {
        return this.orderIdHandler;
    }

    public OrderService getOrderService() {
        return this.orderService;
    }

    public void processOrderPlacement(final Scanner scanner, final MenuManager menuManager,
            final ConsoleInputHandler<Integer> positiveIntegerHandler) {
        try {
            final List<MenuItem> orderItems = menuManager.selectMenuItems(scanner, positiveIntegerHandler);

            if (!orderItems.isEmpty()) {
                final Order newOrder = this.createOrder(orderItems);

                // Optional: Prompt for driver assignment after order creation
                if (newOrder != null) {
                    System.out.println("Would you like to assign a driver now? (Y/N)");
                    final String response = scanner.nextLine().trim().toUpperCase();
                    if ("Y".equals(response)) {
                        // You might want to pass the DriverManager as a parameter
                        // or create a method to handle driver assignment
                        this.assignDriverToNewOrder(scanner, newOrder);
                    }
                }
            }
        } catch (final CustomException.QueueFullException e) {
            OrderManager.logger.warning("Order queue full: " + e.getMessage());
            System.out.println("Sorry, we cannot accept more orders at the moment.");
        }
    }

    private void assignDriverToNewOrder(final Scanner scanner, final Order order) {
        // This method could be moved to DriverManager if preferred
        final DriverManager driverManager = new DriverManager();
        driverManager.assignDriverToOrder(scanner, order, this.orderIdHandler);
    }

    public double calculateOrderTotal(final Order order) {
        return order.getItems().stream()
                .mapToDouble(MenuItem::getPrice)
                .sum();
    }

    public void manageDriverRatings(final Driver driver, final int rating) {
        driver.addRating(rating);
        System.out.println("Driver " + driver.getName() + " rated with " + rating + " stars.");
    }

    public void processOrdersInCorrectOrder(final OrderQueue orderQueue) {
        while (!orderQueue.isEmpty()) {
            final Order order = orderQueue.dequeue().orElse(null);
            if (order != null) {
                System.out.println("Processing order: " + order.getOrderId());
                // Process the order
            }
        }
    }

    public OrderQueue getOrderQueue() {
        return this.orderQueue;
    }
}
