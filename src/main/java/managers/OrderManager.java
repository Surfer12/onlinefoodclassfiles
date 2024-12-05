package managers;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import CustomException.ValidationException;
import model.Driver;
import model.MenuItem;
import model.Order;
import queue.OrderQueue;
import services.OrderService;
import services.impl.OrderServiceImpl;
import validation.ConsoleInputHandler;
import validation.InputValidatorImpl;
import validation.PositiveLongValidator;
import validation.ValidationUtils;

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

    public Order createOrder(final List<MenuItem> menuItems) {
        // Validate input
        if (menuItems == null || menuItems.isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one menu item");
        }

        // Create the order using the OrderService with a default customer ID
        final Order newOrder = this.orderService.createOrder(menuItems, 0L);

        // Add the order to the order queue
        this.orderQueue.enqueue(newOrder);

        // Log the order creation
        OrderManager.logger.log(Level.INFO, "New order created with ID: {0}", newOrder.getOrderId());

        return newOrder;
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

    public void processOrderPlacement(final Scanner scanner, final MenuManager menuManager, final ConsoleInputHandler<Integer> positiveIntegerHandler) {
        // Prompt for customer ID with option to use default
        final Long customerId = this.promptForCustomerId(scanner, positiveIntegerHandler);

        // Rest of the existing order placement logic
        final List<MenuItem> orderItems = new ArrayList<>();
        Integer menuItem;
        do {
            menuItem = positiveIntegerHandler.handleInput(scanner, "Enter menu item number to add (0 to finish): ");

            if (menuItem != null && menuItem > 0) {
                if (menuItem > menuManager.getMenuService().getMenuSize()) {
                    System.out.println("Invalid menu item number. Please choose a number between 1 and "
                            + menuManager.getMenuService().getMenuSize());
                    continue;
                }

                try {
                    // Get the menu item using the correct index (menuItem - 1)
                    final List<MenuItem> menuItems = menuManager.getMenuService().getAllMenuItems();
                    final MenuItem selectedItem = menuItems.get(menuItem - 1);

                    Integer quantity;
                    do {
                        quantity = positiveIntegerHandler.handleInput(scanner, "Enter quantity (1-10): ");
                        if (quantity != null) {
                            if (quantity <= 0) {
                                System.out.println("Quantity must be greater than 0. Please try again.");
                            } else if (quantity > 10) {
                                System.out.println("Maximum quantity is 10. Please try again.");
                            } else {
                                // Add the item to the order with the specified quantity
                                for (int i = 0; i < quantity; i++) {
                                    orderItems.add(selectedItem);
                                }
                                System.out.printf("Added %d x %s to your order%n", quantity, selectedItem.getName());
                                break;
                            }
                        }
                    } while (quantity == null || quantity <= 0 || quantity > 10);
                } catch (final IndexOutOfBoundsException e) {
                    System.out.println("Invalid menu item number. Please choose a number between 1 and "
                            + menuManager.getMenuService().getMenuSize());
                }
            } else if (menuItem != null && menuItem < 0) {
                System.out.println("Please enter a valid menu item number (1-"
                        + menuManager.getMenuService().getMenuSize() + ") or 0 to finish.");
            }
        } while (menuItem == null || menuItem > 0);

        if (orderItems.isEmpty()) {
            System.out.println("Order cancelled - no items were added.");
            return;
        }

        // Create the order with the customer ID
        final Order order = this.createOrder(orderItems);
        System.out.println("Order created successfully with ID: " + order.getOrderId());
    }

    public Long promptForCustomerId(final Scanner scanner, final ConsoleInputHandler<Integer> positiveIntegerHandler) {
        while (true) {
            System.out.println("\nCustomer ID Options:");
            System.out.println("1. Enter a specific Customer ID");
            System.out.println("2. Use Automatic Customer ID");

            final Integer choice = positiveIntegerHandler.handleInput(scanner, "Enter your choice (1-2): ");

            if (null == choice) {
                System.out.println("Invalid choice. Please enter 1 or 2.");
            } else switch (choice) {
                case 1 -> {
                    // Prompt for specific customer ID
                    final Long customerId = this.getOrderIdHandler().handleInput(scanner, "Enter Customer ID: ");
                    if (customerId != null) {
                        return customerId;
                    }
                    System.out.println("Invalid Customer ID. Please try again.");
                }
                case 2 -> {
                    // Use automatic customer ID
                    final Long autoGeneratedId = this.generateDefaultCustomerId();
                    try {
                        ValidationUtils.validateCustomerId(autoGeneratedId);
                        return autoGeneratedId;
                    } catch (final ValidationException e) {
                        System.out.println("Generated Customer ID is invalid: " + e.getMessage());
                    }
                }
                default -> System.out.println("Invalid choice. Please enter 1 or 2.");
            }
        }
    }

    private Long generateDefaultCustomerId() {
        // This could be a simple incremental ID, a random number, or pulled from a configuration
        // For this example, we'll use a timestamp-based approach
        return System.currentTimeMillis();
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
