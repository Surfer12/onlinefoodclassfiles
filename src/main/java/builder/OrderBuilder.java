package builder;

import model.MenuItem;
import model.Order;
import model.OrderStatus;

import java.util.ArrayList;
import java.util.List;

public class OrderBuilder {
   private Long customerId;
   private String customerEmail;
   private List<MenuItem> items = new ArrayList<>();
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
      return new Order(this.customerId, this.customerEmail, this.items, this.deliveryAddress, this.postalCode) {
         {
            setStatus(status);
         }
      };
   }
}
