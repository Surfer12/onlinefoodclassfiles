package app;

import java.util.List;
import java.util.Optional;

import model.Driver;
import model.Order;
import services.DriverService;

public class OrderProcessor {
    private final DriverService driverService;

    public OrderProcessor(DriverService driverService) {
        this.driverService = driverService;
    }

    public void assignOrder(Order order) {
        Optional<Driver> leastBusyDriver = driverService.getAvailableDrivers().stream()
                .min((d1, d2) -> Integer.compare(d1.getActiveOrderCount(), d2.getActiveOrderCount()));

        if (leastBusyDriver.isPresent()) {
            Driver driver = leastBusyDriver.get();
            driverService.assignDriverToOrder(driver, order);
            System.out.println("Order " + order.getOrderId() + " assigned to driver " + driver.getName());
        } else {
            handleFailedOrderProcessing(order);
        }
    }

    private void handleFailedOrderProcessing(Order order) {
        System.out.println("Failed to process order " + order.getOrderId() + ": No available drivers");
    }

    public void processOrder(Order order) {
        // Implement order processing logic here
        System.out.println("Processing order: " + order.getOrderId());
        assignOrder(order);
    }

    public void updateOrderStatus(Order order, String status) {
        // Implement order status update logic here
        System.out.println("Updating order " + order.getOrderId() + " status to " + status);
        order.setStatus(OrderStatus.valueOf(status));
    }
}
