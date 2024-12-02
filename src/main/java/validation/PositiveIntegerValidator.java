package validation;

public class PositiveIntegerValidator implements Validator<Integer> {

    public PositiveIntegerValidator() {
    }

    @Override
    public Integer parse(final String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public boolean isValid(final Integer value) {
        return value > 0;
    }

    @Override
    public boolean validate(final Integer input) {
        return input != null && input > 0;
    }

    @Override
    public String getTypeName() {
        return "positive integer";
    }
}