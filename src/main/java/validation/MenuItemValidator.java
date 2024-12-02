package validation;

public class MenuItemValidator implements Validator<Integer> {
    private final int maxMenuSize;

    public MenuItemValidator(int maxMenuSize) {
        this.maxMenuSize = maxMenuSize;
    }

    @Override
    public Integer parse(String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public boolean validate(Integer input) {
        return this.isValid(input);
    }

    @Override
    public boolean isValid(Integer value) {
        return value != null && value > 0 && value <= this.maxMenuSize;
    }

    @Override
    public String getTypeName() {
        return "Menu Choice";
    }
}
