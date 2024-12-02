package app;

import java.util.Scanner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import managers.DriverManager;
import managers.MenuManager;
import managers.OrderManager;
import services.OrderService;
import validation.ConsoleInputHandler;

@ExtendWith(MockitoExtension.class)
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
    private ConsoleInputHandler<Integer> menuChoiceHandler;
    @Mock
    private OrderService orderService;
    @Mock
    private ConsoleInputHandler<Long> orderIdHandler;

    @InjectMocks
    private DeliverySystemCLI deliverySystemCLI;

    @BeforeEach
    public void setUp() {
        when(this.menuManager.getMenuChoiceHandler()).thenReturn(this.menuChoiceHandler);
        when(this.orderManager.getOrderService()).thenReturn(this.orderService);
        when(this.orderManager.getOrderIdHandler()).thenReturn(this.orderIdHandler);
    }

    @Test
    public void testStartPlaceOrder() {
        when(this.menuChoiceHandler.handleInput(this.scanner, "Enter your choice below: "))
                .thenReturn(1)
                .thenReturn(null);
        when(this.positiveIntegerHandler.handleInput(this.scanner, "Enter your choice below: ")).thenReturn(1);

        this.deliverySystemCLI.start();

        verify(this.orderManager).processOrderPlacement(this.scanner, this.menuManager, this.positiveIntegerHandler);
    }

    @Test
    public void testStartCheckOrderStatus() {
        when(this.menuChoiceHandler.handleInput(this.scanner, "Enter your choice below: "))
                .thenReturn(2)
                .thenReturn(null);

        this.deliverySystemCLI.start();

        verify(this.orderManager).checkOrderStatus(this.scanner);
    }

    @Test
    public void testStartViewMenu() {
        when(this.menuChoiceHandler.handleInput(this.scanner, "Enter your choice below: "))
                .thenReturn(3)
                .thenReturn(null);

        this.deliverySystemCLI.start();

        verify(this.menuManager).displayMenu();
    }

    @Test
    public void testStartManageDrivers() {
        when(this.menuChoiceHandler.handleInput(this.scanner, "Enter your choice below: "))
                .thenReturn(4)
                .thenReturn(null);

        this.deliverySystemCLI.start();

        verify(this.driverManager).manageDriverMenu(this.scanner, this.orderManager);
    }

    @Test
    public void testStartRateDriver() {
        when(this.menuChoiceHandler.handleInput(this.scanner, "Enter your choice below: "))
                .thenReturn(5)
                .thenReturn(null);
        when(this.orderIdHandler.handleInput(this.scanner, "Enter Order ID to rate driver: "))
                .thenReturn(1L);
        when(this.orderService.getOrderById(1L)).thenReturn(null);

        this.deliverySystemCLI.start();

        verify(this.driverManager).rateDriver(this.scanner, null, this.menuChoiceHandler);
    }

    @Test
    public void testStartExit() {
        when(this.menuChoiceHandler.handleInput(this.scanner, "Enter your choice below: "))
                .thenReturn(6)
                .thenReturn(null);

        this.deliverySystemCLI.start();

        verify(this.scanner).close();
    }
}