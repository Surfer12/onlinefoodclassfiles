package observer;

import model.Order;

/**
 * Interface for managing order observers and notifying them of order events.
 */
public interface OrderSubject {

    /**
     * Attaches an observer to the subject.
     *
     * @param observer the observer to attach
     */
    void attach(OrderObserver observer);

    /**
     * Detaches an observer from the subject.
     *
     * @param observer the observer to detach
     */
    void detach(OrderObserver observer);

    /**
     * Notifies all attached observers of an order event.
     *
     * @param order the order associated with the event
     */
    void notifyObservers(Order order);
}