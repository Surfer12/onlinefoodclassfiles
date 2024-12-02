package CustomException;

/**
 * Exception thrown when the order queue is full.
 */
public class QueueFullException extends RuntimeException {
   public QueueFullException(String message) {
      super(message);
   }
}