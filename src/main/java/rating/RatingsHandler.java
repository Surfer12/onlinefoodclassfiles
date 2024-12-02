package rating;

import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import CustomException.QueueFullException;

public class RatingsHandler<T> implements RatingsBusinessLogic<T> {
   private final int maxRatings;
   private final ConcurrentLinkedDeque<T> ratingsQueue;
   private final Lock ratingsLock = new ReentrantLock();

   public RatingsHandler(int maxRatings) {
      try {
         if (maxRatings <= 0) {
            throw new IllegalArgumentException("Maximum ratings must be positive");
         }
         this.maxRatings = maxRatings;
         this.ratingsQueue = new ConcurrentLinkedDeque<>();
      } catch (IllegalArgumentException e) {
         System.err.println("Error in RatingsHandler constructor: " + e.getMessage());
         throw e;
      }
   }

   @Override
   public void addRating(T rating) {
      ratingsLock.lock();
      try {
         if (rating == null) {
            throw new IllegalArgumentException("Rating cannot be null");
         }
         if (isRatingQueueFull()) {
            throw new QueueFullException("Ratings queue is at maximum capacity: " + maxRatings);
         }
         ratingsQueue.addLast(rating);
         enforceRatingQueueMaxSize();
      } catch (IllegalArgumentException | QueueFullException e) {
         System.err.println("Error in addRating: " + e.getMessage());
         throw e;
      } finally {
         ratingsLock.unlock();
      }
   }

   @Override
   public Optional<T> removeOldestRating() {
      ratingsLock.lock();
      try {
         return Optional.ofNullable(ratingsQueue.pollFirst());
      } finally {
         ratingsLock.unlock();
      }
   }

   @Override
   public Optional<T> getLatestRating() {
      ratingsLock.lock();
      try {
         return Optional.ofNullable(ratingsQueue.peekLast());
      } finally {
         ratingsLock.unlock();
      }
   }

   @Override
   public void enforceRatingQueueMaxSize() {
      ratingsLock.lock();
      try {
         while (ratingsQueue.size() > maxRatings) {
            ratingsQueue.removeFirst();
         }
      } finally {
         ratingsLock.unlock();
      }
   }

   @Override
   public void clearAllRatings() {
      ratingsLock.lock();
      try {
         ratingsQueue.clear();
      } finally {
         ratingsLock.unlock();
      }
   }

   @Override
   public boolean isRatingQueueEmpty() {
      ratingsLock.lock();
      try {
         return ratingsQueue.isEmpty();
      } finally {
         ratingsLock.unlock();
      }
   }

   @Override
   public boolean isRatingQueueFull() {
      ratingsLock.lock();
      try {
         return ratingsQueue.size() >= maxRatings;
      } finally {
         ratingsLock.unlock();
      }
   }

   @Override
   public int getCurrentRatingCount() {
      ratingsLock.lock();
      try {
         return ratingsQueue.size();
      } finally {
         ratingsLock.unlock();
      }
   }

   @Override
   public double calculateAverageRating() {
      ratingsLock.lock();
      try {
         if (ratingsQueue.isEmpty()) {
            return 0.0;
         }
         return ratingsQueue.stream()
               .mapToDouble(rating -> (double) rating)
               .average()
               .orElse(0.0);
      } finally {
         ratingsLock.unlock();
      }
   }

   @Override
   public int getMaxRatings() {
      return maxRatings;
   }
}
