package app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import managers.OrderManager;
import model.MenuItem;
import model.Order;
import model.OrderStatus;

class OrderManagerTest {

    private OrderManager orderManager;

    @BeforeEach
    void setUp() {
        orderManager = new OrderManager();
    }

    @Test
    void testCreateOrder() {
        List<MenuItem> items = new ArrayList<>();
        items.add(new MenuItem() {
            @Override
            public String getName() {
                return "Pizza";
            }

            @Override
            public double getPrice() {
                return 10.0;
            }
        });

        Order order = orderManager.createOrder(items);
        assertNotNull(order);
        assertEquals(OrderStatus.SUBMITTED, order.getStatus());
        assertEquals(1, order.getItems().size());
    }

    @Test
    void testCreateOrderWithEmptyItems() {
        List<MenuItem> items = new ArrayList<>();
        assertThrows(IllegalArgumentException.class, () -> orderManager.createOrder(items));
    }

    @Test
    void testCheckOrderStatus() {
        List<MenuItem> items = new ArrayList<>();
        items.add(new MenuItem() {
            @Override
            public String getName() {
                return "Pizza";
            }

            @Override
            public double getPrice() {
                return 10.0;
            }
        });

        Order order = orderManager.createOrder(items);
        String status = orderManager.getOrderService().getOrderStatus(order.getOrderId());
        assertEquals(OrderStatus.SUBMITTED.toString(), status);
    }

    @Test
    void testCalculateOrderTotal() {
        List<MenuItem> items = new ArrayList<>();
        items.add(new MenuItem() {
            @Override
            public String getName() {
                return "Pizza";
            }

            @Override
            public double getPrice() {
                return 10.0;
            }
        });
        items.add(new MenuItem() {
            @Override
            public String getName() {
                return "Burger";
            }

            @Override
            public double getPrice() {
                return 5.0;
            }
        });

        Order order = orderManager.createOrder(items);
        double total = orderManager.calculateOrderTotal(order);
        assertEquals(15.0, total);
    }
}
