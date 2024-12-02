package validation;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * The ValidationHandler class handles both input and validation.
 *
 * @param <T> the type of input to handle
 */
public class ValidationHandler<T> {
    private final Validator<T> validator;
    private final String typeName;
    private final String errorMessage;
    private final Scanner scanner;

    public ValidationHandler(final Validator<T> validator, final String typeName, final String errorMessage) {
        this.validator = validator;
        this.typeName = typeName;
        this.errorMessage = errorMessage;
        this.scanner = new Scanner(System.in);
    }

    public T getInput(String prompt) {
        while (true) {
            System.out.println(prompt);
            final String input = this.scanner.nextLine();
            try {
                final T value = this.validator.parse(input);
                if (this.validator.isValid(value)) {
                    return value;
                }
                System.out.println(this.errorMessage != null ? this.errorMessage
                        : "Invalid input. Please enter a valid " + this.typeName + ".");
            } catch (final Exception e) {
                System.out.println("Error parsing input: " + e.getMessage());
            }
        }
    }

    public T[] getMultipleInputs(final String prompt, final String stopCommand) {
        final List<T> inputs = new ArrayList<>();
        while (true) {
            System.out.println(prompt);
            final String input = this.scanner.nextLine();
            if (input.equalsIgnoreCase(stopCommand)) {
                break;
            }
            try {
                final T value = this.validator.parse(input);
                if (this.validator.isValid(value)) {
                    inputs.add(value);
                } else {
                    System.out.println("Invalid input. Please enter a valid " + this.typeName + ".");
                }
            } catch (final Exception e) {
                System.out.println("Error parsing input: " + e.getMessage());
            }
        }

        if (inputs.isEmpty()) {
            @SuppressWarnings("unchecked")
            final T[] emptyArray = (T[]) java.lang.reflect.Array.newInstance(
                    inputs.isEmpty() ? Object.class : inputs.get(0).getClass(), 0);
            return emptyArray;
        }

        @SuppressWarnings("unchecked")
        final T[] result = inputs.toArray((T[]) Array.newInstance(inputs.get(0).getClass(), inputs.size()));
        return result;
    }

    public String getTypeName() {
        return this.typeName;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }
}
