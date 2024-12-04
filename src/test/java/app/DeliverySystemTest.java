package app;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import model.Driver;
import model.Order;
import services.DriverService;
import services.OrderService;

public class DeliverySystemTest {

    @Mock
    private OrderService orderService;
    @Mock
    private DriverService driverService;

    private DeliverySystem deliverySystem;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        deliverySystem = new DeliverySystem(orderService, driverService);
    }

    @Test
    public void testSubmitOrder() {
        Order order = new Order();
        deliverySystem.submitOrder(order);
        assertEquals("Pending", deliverySystem.getOrderStatus(order.getOrderId()));
    }

    @Test
    public void testAssignOrderToDriver() {
        Order order = new Order();
        Driver driver = new Driver();
        deliverySystem.assignOrderToDriver(order, driver);
        assertEquals("In Progress", deliverySystem.getOrderStatus(order.getOrderId()));
    }

    @Test
    public void testCompleteDelivery() {
        Long orderId = 1L;
        Long driverId = 1L;
        deliverySystem.completeDelivery(orderId, driverId);
        assertEquals("Delivered", deliverySystem.getOrderStatus(orderId));
    }
}
