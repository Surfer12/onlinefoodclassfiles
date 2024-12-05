package rating;

import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import CustomException.QueueFullException;
import queue.QueueOperations;

public class RatingsHandler<T> implements RatingsBusinessLogic<T>, QueueOperations<T> { // impliment queue operations
   private final int maxRatings;
   private final ConcurrentLinkedQueue<T> ratingsQueue;
   private final Lock ratingsLock = new ReentrantLock();

   public RatingsHandler(final int maxRatings) {
      try {
         if (maxRatings <= 0) {
            throw new IllegalArgumentException("Maximum ratings must be positive");
         }
         this.maxRatings = maxRatings;
         this.ratingsQueue = new ConcurrentLinkedQueue<>();
      } catch (final IllegalArgumentException e) {
         System.err.println("Error in RatingsHandler constructor: " + e.getMessage());
         throw e;
      }
   }

   @Override
   public void addRating(final T rating) { // adds rating to the start of the ratingsQueue
      this.ratingsLock.lock(); //
      try {
         if (rating == null) {
            throw new IllegalArgumentException("Rating cannot be null");
         }
         if (this.isRatingQueueFull()) {
            throw new QueueFullException("Ratings queue is at maximum capacity: " + this.maxRatings);
         }
         this.ratingsQueue.add(rating);
         this.enforceRatingQueueMaxSize();
      } catch (IllegalArgumentException | QueueFullException e) {
         System.err.println("Error in addRating: " + e.getMessage());
         throw e;
      } finally {
         this.ratingsLock.unlock();
      }
   }

   @Override
   public void enqueue(final T rating) {
      this.addRating(rating);
   }

   @Override
   public Optional<T> getLatestRating() { // polls the oldest rating and removes it from the queue
      this.ratingsLock.lock();
      try {
         return Optional.ofNullable(this.ratingsQueue.poll()); // impliment across project
      } finally {
         this.ratingsLock.unlock();
      }
   }

   @Override
   public Optional<T> removeOldestRating() { // removes the oldest rating from the queue
      this.ratingsLock.lock();
      try {
         return Optional.ofNullable(this.ratingsQueue.remove());
      } finally {
         this.ratingsLock.unlock();
      }
   }

   @Override
   public void enforceRatingQueueMaxSize() {
      this.ratingsLock.lock();
      try {
         while (this.ratingsQueue.size() >= this.maxRatings) {
            System.out.println("Ratings queue size: " + this.ratingsQueue.size());
            if (this.ratingsQueue.size() > this.maxRatings) {
               this.ratingsQueue.remove(); // add remove logging here
            }
         }
      } finally {
         this.ratingsLock.unlock();
      }
   }

   @Override
   public void clearAllRatings() {
      this.ratingsLock.lock();
      try {
         this.ratingsQueue.clear();
      } finally {
         this.ratingsLock.unlock();
      }
   }

   @Override
   public boolean isRatingQueueEmpty() {
      this.ratingsLock.lock();
      try {
         return this.ratingsQueue.isEmpty();
      } finally {
         this.ratingsLock.unlock();
      }
   }

   @Override
   public boolean isRatingQueueFull() {
      this.ratingsLock.lock();
      try {
         return this.ratingsQueue.size() >= this.maxRatings;
      } finally {
         this.ratingsLock.unlock();
      }
   }

   @Override
   public int getCurrentRatingCount() {
      this.ratingsLock.lock();
      try {
         return this.ratingsQueue.size();
      } finally {
         this.ratingsLock.unlock();
      }
   }

   @Override
   public double calculateAverageRating() {
      this.ratingsLock.lock();
      try {
         if (this.ratingsQueue.isEmpty()) {
            return 0.0;
         }
         return this.ratingsQueue.stream()
               .mapToDouble(rating -> (double) rating)
               .average()
               .orElse(0.0);
      } finally {
         this.ratingsLock.unlock();
      }
   }

   @Override
   public int getMaxRatings() {
      return this.maxRatings;
   }

   @Override
   public Optional<T> dequeue() {
      return this.getLatestRating();
   }

   @Override
   public Optional<T> peek() {
      this.ratingsLock.lock();
      try {
         return Optional.ofNullable(this.ratingsQueue.peek());
      } finally {
         this.ratingsLock.unlock();
      }
   }

   @Override
   public void clear() {
      this.clearAllRatings();
   }

   @Override
   public boolean isEmpty() {
      return this.isRatingQueueEmpty();
   }

   @Override
   public int size() {
      return this.getCurrentRatingCount();
   }
}
