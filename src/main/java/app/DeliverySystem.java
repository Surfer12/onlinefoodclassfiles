package app;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import CustomException.OrderProcessingException;
import CustomException.PaymentException;
import CustomException.QueueFullException;
import CustomException.ValidationException;
import managers.OrderStatusManager;
import model.Driver;
import model.Order;
import model.OrderStatus;
import notification.NotificationService;
import queue.OrderQueue;
import services.DriverService;

public class DeliverySystem {
    private final Map<Long, String> orderStatuses = new HashMap<>();
    private final NotificationService notificationService;
    private final OrderStatusManager statusManager;
    private final DriverService driverService;

    public DeliverySystem(final NotificationService notificationService, final OrderStatusManager statusManager,
            final DriverService driverService) {
        this.notificationService = notificationService;
        this.statusManager = statusManager;
        this.driverService = driverService;
    }

    public void submitOrder(final Order order) {
        try {
            if (this.isValidStatusTransition(OrderStatus.SUBMITTED, OrderStatus.PENDING)) {
                System.out.println("Order submitted: " + order.getOrderId());
                this.orderStatuses.put(order.getOrderId(), "Pending");
                this.notificationService.sendOrderConfirmationToCustomer(order);
            } else {
                throw new OrderProcessingException("Invalid status transition from SUBMITTED to PENDING");
            }
        } catch (final OrderProcessingException | PaymentException | QueueFullException | ValidationException e) {
            throw new OrderProcessingException("Failed to submit order: " + e.getMessage(), e);
        }
    }

    public void updateOrderStatus(final Order order, final OrderStatus newStatus) {
        try {
            if (this.isValidStatusTransition(order.getStatus(), newStatus)) {
                this.statusManager.updateOrderStatus(order, newStatus);
                this.orderStatuses.put(order.getOrderId(), newStatus.toString());
            } else {
                throw new OrderProcessingException("Invalid status transition from " + order.getStatus() + " to " + newStatus);
            }
        } catch (final OrderProcessingException | PaymentException | QueueFullException | ValidationException e) {
            throw new OrderProcessingException("Failed to update order status: " + e.getMessage(), e);
        }
    }

    private boolean isValidStatusTransition(final OrderStatus current, final OrderStatus next) {
        // Define valid transitions
        return switch (current) {
            case SUBMITTED -> next == OrderStatus.PENDING || next == OrderStatus.CANCELLED;
            case PENDING -> next == OrderStatus.IN_PROGRESS || next == OrderStatus.CANCELLED;
            case IN_PROGRESS -> next == OrderStatus.PREPARING || next == OrderStatus.CANCELLED;
            case PREPARING -> next == OrderStatus.OUT_FOR_DELIVERY || next == OrderStatus.CANCELLED;
            case OUT_FOR_DELIVERY -> next == OrderStatus.DELIVERED || next == OrderStatus.CANCELLED;
            case DELIVERED, CANCELLED -> false; // Terminal states
            default -> false;
        };
    }

    public void assignOrderToDriver(final Order order, final Optional<Driver> driver) {
        try {
            if (driver.isPresent()) {
                if (this.isValidStatusTransition(order.getStatus(), OrderStatus.IN_PROGRESS)) {
                    System.out.println("Order " + order.getOrderId() + " assigned to driver " + driver.get().getName());
                    this.orderStatuses.put(order.getOrderId(), "In Progress");
                    this.notificationService.sendDriverAssignmentNotification(order, driver.get());
                } else {
                    throw new OrderProcessingException("Invalid status transition from " + order.getStatus() + " to IN_PROGRESS");
                }
            } else {
                System.out.println("No available driver for order " + order.getOrderId());
            }
        } catch (final OrderProcessingException | PaymentException | QueueFullException | ValidationException e) {
            throw new OrderProcessingException("Failed to assign order to driver: " + e.getMessage(), e);
        }
    }

    public void completeDelivery(final Long orderId, final Long driverId) {
        try {
            System.out.println("Delivery completed for order " + orderId + " by driver " + driverId);
            this.orderStatuses.put(orderId, "Delivered");
            this.notificationService.sendDeliveryCompletionNotification(orderId);
        } catch (final OrderProcessingException | PaymentException | QueueFullException | ValidationException e) {
            throw new OrderProcessingException("Failed to complete delivery: " + e.getMessage(), e);
        }
    }

    public String getOrderStatus(final Long orderId) {
        return this.orderStatuses.getOrDefault(orderId, "Order Not Found");
    }

    public void manageDriverRatings(final Driver driver, final int rating) {
        driver.addRating(rating);
        System.out.println("Driver " + driver.getName() + " rated with " + rating + " stars.");
    }

    public void processOrdersInCorrectOrder(final OrderQueue orderQueue) {
        orderQueue.getPendingOrders().stream()
            .forEach(order -> {
                System.out.println("Processing order: " + order.getOrderId());
                final Optional<Driver> driver = this.selectDriverForOrder(order);
                this.assignOrderToDriver(order, driver);
            });
    }

    public Optional<Driver> selectDriverForOrder(final Order order) {
        return this.findAvailableDriverForOrderType();
    }

    private Optional<Driver> findAvailableDriverForOrderType() {
        return this.driverService.getAvailableDrivers().stream().findFirst();
    }

    public void assignOrderToLeastBusyDriver(Order order) {
        Optional<Driver> leastBusyDriver = this.driverService.getAvailableDrivers().stream()
                .min((d1, d2) -> Integer.compare(d1.getActiveOrderCount(), d2.getActiveOrderCount()));
        if (leastBusyDriver.isPresent()) {
            this.assignOrderToDriver(order, leastBusyDriver);
        } else {
            this.handleFailedOrderProcessing(order);
        }
    }

    public void handleFailedOrderProcessing(Order order) {
        System.out.println("Failed to process order " + order.getOrderId() + ": No available drivers");
        this.notificationService.sendOrderFailureNotification(order);
    }
}
