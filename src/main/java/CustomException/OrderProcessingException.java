package CustomException;

/**
 * Exception thrown when an error occurs during order processing.
 */
public class OrderProcessingException extends RuntimeException {
    public OrderProcessingException(String message) {
        super(message);
    }
}