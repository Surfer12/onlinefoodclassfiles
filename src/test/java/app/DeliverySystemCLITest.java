package app;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import managers.DriverManager;
import managers.MenuManager;
import managers.OrderManager;
import model.Driver;
import model.Order;
import validation.ConsoleInputHandler;

public class DeliverySystemCLITest {

    @Mock
    private Scanner scanner;
    @Mock
    private MenuManager menuManager;
    @Mock
    private OrderManager orderManager;
    @Mock
    private DriverManager driverManager;
    @Mock
    private ConsoleInputHandler<Integer> positiveIntegerHandler;
    @Mock
    private ConsoleInputHandler<String> emailHandler;
    @Mock
    private ConsoleInputHandler<String> locationHandler;

    private DeliverySystemCLI deliverySystemCLI;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        deliverySystemCLI = new DeliverySystemCLI(scanner, menuManager, orderManager, driverManager, positiveIntegerHandler, emailHandler, locationHandler);
    }

    @Test
    public void testHandlePlaceNewOrder() {
        deliverySystemCLI.handlePlaceNewOrder();
        verify(orderManager).processOrderPlacement(scanner, menuManager, positiveIntegerHandler);
    }

    @Test
    public void testHandleCheckOrderStatus() {
        deliverySystemCLI.handleCheckOrderStatus();
        verify(orderManager).checkOrderStatus(scanner);
    }

    @Test
    public void testHandleViewMenu() {
        deliverySystemCLI.handleViewMenu();
        verify(menuManager).displayMenu();
    }

    @Test
    public void testHandleManageDrivers() {
        deliverySystemCLI.handleManageDrivers();
        verify(driverManager).manageDriverMenu(scanner, orderManager);
    }

    @Test
    public void testHandleRateDriver() {
        when(orderManager.getOrderIdHandler().handleInput(scanner, "Enter Order ID to rate driver: ")).thenReturn(1L);
        when(orderManager.getOrderService().getOrderById(1L)).thenReturn(new Order());
        deliverySystemCLI.handleRateDriver();
        verify(driverManager).rateDriver(eq(scanner), any(Order.class), eq(menuManager.getMenuChoiceHandler()));
    }

    @Test
    public void testHandleCalculateOrderTotal() {
        when(orderManager.getOrderIdHandler().handleInput(scanner, "Order ID: ")).thenReturn(1L);
        when(orderManager.getOrderService().getOrderById(1L)).thenReturn(new Order());
        deliverySystemCLI.handleCalculateOrderTotal();
        verify(orderManager).calculateOrderTotal(any(Order.class));
    }

    @Test
    public void testHandleManageDriverRatings() {
        when(orderManager.getOrderIdHandler().handleInput(scanner, "Driver ID: ")).thenReturn(1L);
        when(driverManager.getDriverService().getDriverById(1L)).thenReturn(new Driver(1L, "John Doe", "Car", "ABC123"));
        deliverySystemCLI.handleManageDriverRatings();
        verify(driverManager).getDriverService().getDriverById(1L);
    }

    @Test
    public void testHandleProcessOrdersInCorrectOrder() {
        deliverySystemCLI.handleProcessOrdersInCorrectOrder();
        verify(orderManager).getOrderQueue();
    }

    @Test
    public void testHandleExit() {
        deliverySystemCLI.handleExit();
        assertFalse(deliverySystemCLI.isRunning());
    }

    @Test
    public void testErrorHandling() {
        doThrow(new RuntimeException("Test Exception")).when(menuManager).displayMenu();
        deliverySystemCLI.handleViewMenu();
        verify(deliverySystemCLI.getLogger()).log(eq(Level.SEVERE), eq("An unexpected error occurred"), any(RuntimeException.class));
    }

    @Test
    public void testHandleMenuChoice() {
        // Test for handleMenuChoice method
        when(scanner.nextInt()).thenReturn(1);
        deliverySystemCLI.handleMenuChoice(1);
        verify(orderManager).processOrderPlacement(scanner, menuManager, positiveIntegerHandler);
    }
}
