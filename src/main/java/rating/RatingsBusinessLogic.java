package rating;

import java.util.Optional;

import CustomException.QueueFullException;

public interface RatingsBusinessLogic<T> {
    /**
     * Adds a new rating to the queue.
     * 
     * @throws QueueFullException if the queue is at maximum capacity
     */
    void addRating(T rating);

    /**
     * Removes and returns the oldest rating in the queue.
     * 
     * @return Optional containing the oldest rating, or empty if queue is empty
     */
    Optional<T> removeOldestRating();

    /**
     * Retrieves the most recently added rating without removing it.
     * 
     * @return Optional containing the newest rating, or empty if queue is empty
     */
    Optional<T> getLatestRating();

    void clearAllRatings();

    boolean isRatingQueueEmpty();

    boolean isRatingQueueFull();

    int getCurrentRatingCount();

    void enforceRatingQueueMaxSize();

    /**
     * Calculates the average of all ratings in the queue.
     * 
     * @return The average rating, or 0.0 if the queue is empty
     */
    double calculateAverageRating();

    /**
     * Gets the maximum number of ratings allowed in the queue.
     * 
     * @return The maximum capacity of the ratings queue
     */
    int getMaxRatings();
}
