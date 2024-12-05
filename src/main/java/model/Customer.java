package model;

import java.util.ArrayList;
import java.util.List;

import rating.Rating;

/**
 * Represents a customer in the system.
 */
public class Customer extends Person {
   private final List<Order> orderHistory;

   /**
    * Constructs a Customer with the specified details.
    *
    * @param id      the ID of the customer
    * @param name    the name of the customer
    * @param email   the email address of the customer
    * @param address the address of the customer
    */
   public Customer(final Long id, final String name, final String email, final String address) {
      super(id, name, email, address);
      this.orderHistory = new ArrayList<>();
   }

   /**
    * Places an order for the customer.
    *
    * @param items  the list of menu items to order
    * @param driver the driver to rate
    */
   public void placeOrder(final List<MenuItem> items, final Driver driver) {
      final Order order = new Order(
            this.getId(),
            this.getEmail(),
            items,
            new Location(this.getAddress(), "zipcode"));

      this.orderHistory.add(order);
   }

   /**
    * Rates a driver.
    *
    * @param driver  the driver to rate
    * @param score   the rating score
    * @param comment the rating comment
    */
   public void rateDriver(final Driver driver, final int score, final String comment) {
      final Rating rating = new Rating.Builder()
            .customerId(this.getId())
            .driverId(driver.getId())
            .score(score)
            .comment(comment)
            .build();

      // Assuming Driver has a method to add a Rating object
      driver.addRating(rating);
   }

   /**
    * Returns the order history of the customer.
    *
    * @return a list of past orders
    */
   public List<Order> getOrderHistory() {
      return new ArrayList<>(this.orderHistory);
   }
}
