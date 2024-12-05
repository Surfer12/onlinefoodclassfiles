package validation;

public class MenuItemValidator implements Validator<Integer> {
    private final int maxMenuSize;

    public MenuItemValidator(final int maxMenuSize) {
        this.maxMenuSize = maxMenuSize;
    }

    @Override
    public Integer parse(final String input) {
        try {
            return Integer.parseInt(input);
        } catch (final NumberFormatException e) {
            return null;
        }
    }

    @Override
    public boolean validate(final Integer input) {
        return this.isValid(input);
    }

    @Override
    public boolean isValid(final Integer value) {
        return value != null && ((value >= 0 && value <= 9) || (value > 0 && value <= this.maxMenuSize));
    }

    @Override
    public String getTypeName() {
        return "Menu Choice";
    }
}
