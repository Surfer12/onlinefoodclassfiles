package validation;

/**
 * Abstract class that defines common methods for validating input.
 */
public abstract class AbstractValidator<T> {

    /**
     * Validates the given input.
     *
     * @param input the input to validate
     * @return true if the input is valid, false otherwise
     */
    public abstract boolean validate(T input);

    /**
     * Parses the given input string into a value of type T.
     *
     * @param input the input string to parse
     * @return the parsed value of type T
     */
    public abstract T parse(String input);
}
