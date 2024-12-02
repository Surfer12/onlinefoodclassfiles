package validation;

import java.util.List;

import CustomException.ValidationException;
import payment.Payment;

/**
 * Utility class for validation methods.
 *
 * <p>Usage example:
 * <pre>
 * {@code
 * Long customerId = 123L;
 * List<String> items = List.of("item1", "item2");
 * double amount = 50.0;
 * double maxAmount = 100.0;
 * Payment payment = new Payment(1L, "CREDIT_CARD", amount);
 *
 * ValidationUtils.validateCustomerId(customerId);
 * ValidationUtils.validateItems(items, 5);
 * ValidationUtils.validateAmount(amount, maxAmount);
 * ValidationUtils.validatePayment(payment);
 * }
 * </pre>
 */
public class ValidationUtils {

   /**
    * Validates the customer ID.
    *
    * @param customerId the customer ID to validate
    * @throws ValidationException if the customer ID is invalid
    */
   public static void validateCustomerId(Long customerId) {
      try {
         if (customerId == null || customerId <= 0) {
            throw new ValidationException("Invalid customer ID");
         }
      } catch (ValidationException e) {
         System.err.println("Error in validateCustomerId: " + e.getMessage());
         throw e;
      }
   }

   /**
    * Validates the list of items.
    *
    * @param items the list of items to validate
    * @param maxItems the maximum number of items allowed
    * @throws ValidationException if the list is null, empty, or exceeds the maximum limit
    */
   public static void validateItems(List<?> items, int maxItems) {
      try {
         if (items == null || items.isEmpty()) {
            throw new ValidationException("List cannot be null or empty");
         }
         if (items.size() > maxItems) {
            throw new ValidationException("Exceeds maximum item limit of " + maxItems);
         }
      } catch (ValidationException e) {
         System.err.println("Error in validateItems: " + e.getMessage());
         throw e;
      }
   }

   /**
    * Validates the amount.
    *
    * @param amount the amount to validate
    * @param maxAmount the maximum amount allowed
    * @throws ValidationException if the amount is less than or equal to zero or exceeds the maximum limit
    */
   public static void validateAmount(double amount, double maxAmount) {
      try {
         if (amount <= 0) {
            throw new ValidationException("Amount must be greater than zero");
         }
         if (amount > maxAmount) {
            throw new ValidationException("Amount exceeds maximum limit of " + maxAmount);
         }
      } catch (ValidationException e) {
         System.err.println("Error in validateAmount: " + e.getMessage());
         throw e;
      }
   }

   /**
    * Validates the payment.
    *
    * @param payment the payment to validate
    * @throws ValidationException if the payment is not processed
    */
   public static void validatePayment(Payment payment) {
      try {
         if (payment != null && !payment.isProcessed()) {
            throw new ValidationException("Payment must be processed");
         }
      } catch (ValidationException e) {
         System.err.println("Error in validatePayment: " + e.getMessage());
         throw e;
      }
   }
}
