package validation;

import model.MenuItem;

/**
 * Abstract class that defines common methods for validating menu items.
 */
public abstract class AbstractMenuItemValidator extends AbstractValidator<MenuItem> {

    /**
     * Validates the given menu item.
     *
     * @param menuItem the menu item to validate
     * @return true if the menu item is valid, false otherwise
     */
    @Override
    public abstract boolean validate(MenuItem menuItem);

    /**
     * Parses the given input string into a MenuItem object.
     *
     * @param input the input string to parse
     * @return the parsed MenuItem object
     */
    @Override
    public abstract MenuItem parse(String input);
}
