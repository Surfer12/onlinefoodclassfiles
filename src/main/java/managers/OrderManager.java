package managers;

import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

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
        this.orderQueue = new OrderQueue(MAX_QUEUE_SIZE);
        this.orderIdHandler = new ConsoleInputHandler<>(
                new InputValidatorImpl<>(
                        new PositiveLongValidator(),
                        "Order ID",
                        "Invalid Order ID"));
    }

    public Order createOrder(List<MenuItem> orderItems) throws CustomException.QueueFullException {
        if (orderItems.isEmpty()) {
            System.out.println("No items selected. Order cancelled.");
            return null;
        }

        try {
            Order newOrder = this.orderService.createOrder(orderItems);
            this.orderQueue.enqueue(newOrder);
            this.orderService.displayOrderDetails(newOrder);
            System.out.println("Order placed successfully! Order ID: " + newOrder.getOrderId());
            logger.info("New order added to queue: " + newOrder.getOrderId());
            return newOrder;
        } catch (CustomException.QueueFullException e) {
            System.out.println("Sorry, we are currently at maximum order capacity. Please try again later.");
            logger.warning("Order queue full: " + e.getMessage());
            throw e;
        }
    }

    public void checkOrderStatus(Scanner scanner) {
        try {
            Long orderId = this.orderIdHandler.handleInput(scanner, "Enter Order ID to check status: ");

            if (orderId == null)
                return;

            Order order = this.orderService.getOrderById(orderId);
            if (order != null) {
                System.out.println("Order Status: " + order.getStatus());
            } else {
                System.out.println("Order not found.");
            }
        } catch (Exception e) {
            System.out.println("Error checking order status: " + e.getMessage());
            logger.severe("Error in checkOrderStatus: " + e.getMessage());
        }
    }

    public ConsoleInputHandler<Long> getOrderIdHandler() {
        return this.orderIdHandler;
    }

    public OrderService getOrderService() {
        return this.orderService;
    }

    public void processOrderPlacement(Scanner scanner, MenuManager menuManager,
            ConsoleInputHandler<Integer> positiveIntegerHandler) {
        try {
            List<MenuItem> orderItems = menuManager.selectMenuItems(scanner, positiveIntegerHandler);

            if (!orderItems.isEmpty()) {
                Order newOrder = this.createOrder(orderItems);

                // Optional: Prompt for driver assignment after order creation
                if (newOrder != null) {
                    System.out.println("Would you like to assign a driver now? (Y/N)");
                    String response = scanner.nextLine().trim().toUpperCase();
                    if (response.equals("Y")) {
                        // You might want to pass the DriverManager as a parameter
                        // or create a method to handle driver assignment
                        this.assignDriverToNewOrder(scanner, newOrder);
                    }
                }
            }
        } catch (CustomException.QueueFullException e) {
            logger.warning("Order queue full: " + e.getMessage());
            System.out.println("Sorry, we cannot accept more orders at the moment.");
        }
    }

    private void assignDriverToNewOrder(Scanner scanner, Order order) {
        // This method could be moved to DriverManager if preferred
        DriverManager driverManager = new DriverManager();
        driverManager.assignDriverToOrder(scanner, order, this.orderIdHandler);
    }
}