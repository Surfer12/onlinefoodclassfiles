package validation;

import java.util.regex.Pattern;

public class InputValidatorImpl<T> implements InputValidator<T> {
    private final Validator<T> validator;
    private final String typeName;
    private final String errorMessage;

    // RFC 5322 compliant email regex pattern
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");

    // Pattern for basic input sanitization
    private static final Pattern DANGEROUS_CHARS = Pattern.compile("[<>\"'%;()&+]");

    public InputValidatorImpl(final Validator<T> validator, final String typeName, final String errorMessage) {
        this.validator = validator;
        this.typeName = typeName;
        this.errorMessage = errorMessage;
    }

    private static String sanitizeInput(String input) {
        if (input == null) {
            return null;
        }
        return DANGEROUS_CHARS.matcher(input.trim()).replaceAll("");
    }

    public static InputValidator<String> emailValidator() {
        return new InputValidatorImpl<>(
            new Validator<String>() {
                @Override
                public String parse(String input) {
                        String sanitized = sanitizeInput(input);
                        if (sanitized == null || sanitized.isEmpty()) {
                            throw new IllegalArgumentException("Email cannot be empty");
                        }
                        return sanitized.toLowerCase();
                }

                @Override
                public boolean validate(String input) {
                        return input != null && EMAIL_PATTERN.matcher(input).matches();
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
                "Invalid email format. Please enter a valid email address (e.g., user@example.com)"
        );
    }

    public static InputValidator<String> deliveryLocationValidator() {
        return new InputValidatorImpl<>(
            new Validator<String>() {
                    private static final Pattern ADDRESS_PATTERN = Pattern.compile(
                            "^[\\p{L}0-9\\s,.-]+$");

                @Override
                public String parse(String input) {
                        String sanitized = sanitizeInput(input);
                        if (sanitized == null || sanitized.isEmpty()) {
                            throw new IllegalArgumentException("Delivery location cannot be empty");
                        }
                        return sanitized;
                }

                @Override
                public boolean validate(String input) {
                        if (input == null || input.trim().length() < 5) {
                            return false;
                        }
                        return ADDRESS_PATTERN.matcher(input).matches();
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
                "Invalid delivery location. Please enter a valid address with at least 5 characters, using only letters, numbers, spaces, and basic punctuation."
        );
    }

    @Override
    public T parse(final String input) {
        String sanitized = sanitizeInput(input);
        if (sanitized == null) {
            throw new IllegalArgumentException("Input cannot be null");
        }
        return this.validator.parse(sanitized);
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
