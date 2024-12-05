package validation;

public class PositiveIntegerValidator implements Validator<Integer> {
    private static final int MAX_VALUE = Integer.MAX_VALUE;
    private static final String ERROR_MESSAGE = "Please enter a positive integer between 0 and "
            + PositiveIntegerValidator.MAX_VALUE;

    @Override
    public Integer parse(final String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("Input cannot be empty");
        }

        try {
            final String trimmed = input.trim();
            // Check for leading zeros (except single zero)
            if (trimmed.length() > 1 && trimmed.startsWith("0")) {
                throw new IllegalArgumentException("Invalid format: number cannot start with 0");
            }
            return Integer.parseInt(trimmed);
        } catch (final NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number format: " + PositiveIntegerValidator.ERROR_MESSAGE);
        }
    }

    @Override
    public boolean isValid(final Integer value) {
        return this.validate(value);
    }

    @Override
    public boolean validate(final Integer input) {
        if (input == null) {
            return false;
        }
        return input >= 0 && input <= PositiveIntegerValidator.MAX_VALUE;
    }

    @Override
    public String getTypeName() {
        return "positive integer";
    }

    @Override
    public boolean hasError(final Integer value) {
        return !this.validate(value);
    }
}
