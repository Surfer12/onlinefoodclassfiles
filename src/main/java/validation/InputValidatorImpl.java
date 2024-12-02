package validation;

public class InputValidatorImpl<T> implements InputValidator<T> {
    private final Validator<T> validator;
    private final String typeName;
    private final String errorMessage;

    public InputValidatorImpl(final Validator<T> validator, final String typeName, final String errorMessage) {
        this.validator = validator;
        this.typeName = typeName;
        this.errorMessage = errorMessage;
    }

    // Add validators for email and delivery location
    public static InputValidator<String> emailValidator() {
        return new InputValidatorImpl<>(
            new Validator<String>() {
                @Override
                public String parse(String input) {
                    return input.trim();
                }

                @Override
                public boolean validate(String input) {
                    return input.matches("^[A-Za-z0-9+_.-]+@(.+)$");
                }

                @Override
                public boolean isValid(String value) {
                    return validate(value);
                }

                @Override
                public String getTypeName() {
                    return "Email";
                }
            },
            "Email",
            "Invalid email format. Please enter a valid email."
        );
    }

    public static InputValidator<String> deliveryLocationValidator() {
        return new InputValidatorImpl<>(
            new Validator<String>() {
                @Override
                public String parse(String input) {
                    return input.trim();
                }

                @Override
                public boolean validate(String input) {
                    return !input.isEmpty();
                }

                @Override
                public boolean isValid(String value) {
                    return validate(value);
                }

                @Override
                public String getTypeName() {
                    return "Delivery Location";
                }
            },
            "Delivery Location",
            "Delivery location cannot be empty. Please enter a valid location."
        );
    }

    @Override
    public T parse(final String input) {
        return this.validator.parse(input);
    }

    @Override
    public boolean isValid(final T value) {
        return this.validator.isValid(value);
    }

    @Override
    public String getTypeName() {
        return this.typeName;
    }

    @Override
    public String getErrorMessage() {
        return this.errorMessage;
    }
}
