package validation;

public class PositiveLongValidator implements Validator<Long> {
    public boolean validate(Long input) {
        return input != null && input > 0;
    }

    @Override
    public Long parse(String input) {
        try {
            return Long.parseLong(input);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public boolean isValid(Long value) {
        return value != null && value > 0;
    }

    @Override
    public String getTypeName() {
        return "Positive Long";
    }
}
