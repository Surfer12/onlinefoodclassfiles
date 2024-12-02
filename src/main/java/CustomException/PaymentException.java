package CustomException;

/**
 * Exception thrown when an error occurs during payment processing.
 */
public class PaymentException extends RuntimeException {
   public PaymentException(String message) {
      super(message);
   }
}