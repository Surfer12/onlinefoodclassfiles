package tracker;

import model.Order;

public interface OrderSubject {
    void attach(OrderObserver observer);

    void detach(OrderObserver observer);

    void notifyObservers(Order order);
}