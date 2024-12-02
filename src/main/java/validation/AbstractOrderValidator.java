package validation;

import java.util.List;

import model.MenuItem;
import model.Order;

/**
 * Abstract class that defines type-specific validation methods for orders.
 */
public abstract class AbstractOrderValidator extends AbstractValidator<Order> {

    /**
     * Validates the given order.
     *
     * @param order the order to validate
     * @return true if the order is valid, false otherwise
     */
    @Override
    public abstract boolean validate(Order order);

    /**
     * Parses the given input string into an Order object.
     *
     * @param input the input string to parse
     * @return the parsed Order object
     */
    @Override
    public abstract Order parse(String input);

    /**
     * Validates the customer ID of the order.
     *
     * @param customerId the customer ID to validate
     * @return true if the customer ID is valid, false otherwise
     */
    public abstract boolean validateCustomerId(Long customerId);

    /**
     * Validates the list of items in the order.
     *
     * @param items the list of items to validate
     * @return true if the list of items is valid, false otherwise
     */
    public abstract boolean validateItems(List<MenuItem> items);

    /**
     * Validates the total amount of the order.
     *
     * @param totalAmount the total amount to validate
     * @return true if the total amount is valid, false otherwise
     */
    public abstract boolean validateTotalAmount(double totalAmount);
}
