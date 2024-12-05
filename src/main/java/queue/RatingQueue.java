package queue;

import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;

public class RatingQueue<T> implements QueueOperations<T> {
    private final ConcurrentLinkedQueue<T> queue = new ConcurrentLinkedQueue<>();

    @Override
    public void enqueue(final T item) {
        this.queue.add(item);
    }

    @Override
    public Optional<T> dequeue() {
        return Optional.ofNullable(this.queue.poll());
    }

    @Override
    public Optional<T> peek() {
        return Optional.ofNullable(this.queue.peek());
    }

    @Override
    public void clear() {
        this.queue.clear();
    }

    @Override
    public boolean isEmpty() {
        return this.queue.isEmpty();
    }

    @Override
    public int size() {
        return this.queue.size();
    }
}
