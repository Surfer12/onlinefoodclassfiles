package validation;

/**
 * Abstract class that extends AbstractValidator and defines type-specific validation methods for input.
 */
public abstract class AbstractInputValidator<T> extends AbstractValidator<T> {

    /**
     * Validates the given input.
     *
     * @param input the input to validate
     * @return true if the input is valid, false otherwise
     */
    @Override
    public abstract boolean validate(T input);

    /**
     * Parses the given input string into a value of type T.
     *
     * @param input the input string to parse
     * @return the parsed value of type T
     */
    @Override
    public abstract T parse(String input);

    /**
     * Validates the format of the given input string.
     *
     * @param input the input string to validate
     * @return true if the input format is valid, false otherwise
     */
    public abstract boolean validateFormat(String input);

    /**
     * Validates the length of the given input string.
     *
     * @param input the input string to validate
     * @return true if the input length is valid, false otherwise
     */
    public abstract boolean validateLength(String input);
}
