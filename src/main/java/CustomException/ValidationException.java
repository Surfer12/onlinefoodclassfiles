package CustomException;

/**
 * Exception thrown when a validation error occurs.
 */
public class ValidationException extends RuntimeException {
   public ValidationException(String message) {
      super(message);
   }
}