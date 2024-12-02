package queue;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import model.Order;
import validation.OrderValidator;

public class OrderQueue implements QueueOperations<Order> {
   private final LinkedList<Order> queue;
   private final int maxSize;
   private final OrderValidator validator;

   public OrderQueue(int maxSize) {
      this.queue = new LinkedList<>();
      this.maxSize = maxSize;
      this.validator = new OrderValidator();
   }

   @Override
   public synchronized void enqueue(Order order) throws CustomException.QueueFullException {
      try {
         if (this.queue.size() >= this.maxSize) {
            throw new CustomException.QueueFullException("Order queue is at maximum capacity");
         }

         this.validator.validateOrder(order);
         this.queue.add(order);
      } catch (CustomException.QueueFullException e) {
         System.err.println("Error in enqueue: " + e.getMessage());
         throw e;
      }
   }

   @Override
   public synchronized Optional<Order> dequeue() {
      return Optional.ofNullable(this.queue.poll());
   }

   @Override
   public Optional<Order> peek() {
      return Optional.ofNullable(this.queue.peek());
   }

   @Override
   public boolean isEmpty() {
      return this.queue.isEmpty();
   }

   @Override
   public int size() {
      return this.queue.size();
   }

   @Override
   public void clear() {
      this.queue.clear();
   }

   public List<Order> getPendingOrders() {
      return new ArrayList<>(this.queue);
   }

   public synchronized int getPositionInQueue(Order order) {
      int position = 1;
      for (Order o : this.queue) {
         if (o.getId().equals(order.getId())) {
            return position;
         }
         position++;
      }
      return -1; // Order not found in queue
   }
}
