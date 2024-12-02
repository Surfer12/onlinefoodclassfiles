package builder;

import model.MenuItem;
import model.Order;

public class OrderBuilder {
   private Long customerId;
   private String customerEmail;
   private MenuItem item;
   private String deliveryAddress;
   private String postalCode;

   public OrderBuilder withValidatedCustomerId(final Long customerId) {
      this.customerId = customerId;
      return this;
   }

   public OrderBuilder withCustomerEmail(final String email) {
      this.customerEmail = email;
      return this;
   }

   public OrderBuilder addItem(final MenuItem item) {
      this.item = item;
      return this;
   }

   public OrderBuilder withDeliveryLocation(final String address, final String postalCode) {
      this.deliveryAddress = address;
      this.postalCode = postalCode;
      return this;
   }

   public Order build() {
      // Create a concrete implementation of Order
      return new Order(this.customerId, this.customerEmail, this.item, this.deliveryAddress, this.postalCode) {
         // You can override any methods if needed
      };
   }
}
