package observer;

import java.util.ArrayList;
import java.util.List;

import model.Order;

/**
 * Service for tracking orders and notifying observers of order events.
 */
public class OrderTrackingService implements OrderSubject {
   private List<OrderObserver> observers = new ArrayList<>();

   /**
    * Attaches an observer to the subject.
    *
    * @param observer the observer to attach
    */
   @Override
   public void attach(OrderObserver observer) {
      observers.add(observer);
   }

   /**
    * Detaches an observer from the subject.
    *
    * @param observer the observer to detach
    */
   @Override
   public void detach(OrderObserver observer) {
      observers.remove(observer);
   }

   /**
    * Notifies all attached observers of an order event.
    *
    * @param order the order associated with the event
    */
   @Override
   public void notifyObservers(Order order) {
      for (OrderObserver observer : observers) {
         observer.update(order);
      }
   }
}