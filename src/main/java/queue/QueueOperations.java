package queue;

import java.util.Optional;

public interface QueueOperations<T> {
   void enqueue(T item);

   Optional<T> dequeue();

   Optional<T> peek();

   boolean isEmpty();

   int size();

   void clear();
}