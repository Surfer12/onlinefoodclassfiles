package validation;

import CustomException.ValidationException;
import model.Order;

/**
 * The OrderValidator class provides methods to validate an Order object.
 */
public class OrderValidator {
   private static final int MAX_ITEMS_PER_ORDER = 20;
   private static final double MAX_ORDER_AMOUNT = 500.0;

   /**
    * Validates the given order.
    *
    * @param order the order to validate
    * @throws ValidationException if the order is invalid
    */
   public void validateOrder(Order order) {
      if (order == null) {
         throw new ValidationException("Order cannot be null");
      }

      try {
         ValidationUtils.validateCustomerId(order.getCustomerId());
         ValidationUtils.validateItems(order.getItems(), MAX_ITEMS_PER_ORDER);
         ValidationUtils.validateAmount(order.getTotalAmount(), MAX_ORDER_AMOUNT);
      } catch (ValidationException e) {
         throw new ValidationException("Order validation failed: " + e.getMessage());
      }
   }
}