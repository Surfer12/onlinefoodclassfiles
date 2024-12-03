package builder;

import java.util.ArrayList;
import java.util.List;

import model.Location;
import model.MenuItem;
import model.Order;
import model.OrderStatus;

public class OrderBuilder {
   private Long customerId;
   private String customerEmail;
   private final List<MenuItem> items = new ArrayList<>();
   private String deliveryAddress;
   private String postalCode;
   private OrderStatus status;

   public OrderBuilder withValidatedCustomerId(final Long customerId) {
      this.customerId = customerId;
      return this;
   }

   public OrderBuilder withCustomerEmail(final String email) {
      this.customerEmail = email;
      return this;
   }

   public OrderBuilder addItem(final MenuItem item) {
      this.items.add(item);
      return this;
   }

   public OrderBuilder withDeliveryLocation(final String address, final String postalCode) {
      this.deliveryAddress = address;
      this.postalCode = postalCode;
      return this;
   }

   public OrderBuilder withStatus(final OrderStatus status) {
      this.status = status;
      return this;
   }

   public Order build() {
      final Location location = new Location(this.deliveryAddress, this.postalCode);
      final Order order = new Order(this.customerId, this.customerEmail, this.items, location);
      if (this.status != null) {
         order.setStatus(this.status);
      }
      return order;
   }
}
