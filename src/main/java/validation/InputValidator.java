package validation;

public interface InputValidator<T> {

    boolean isValid(T input);

    String getTypeName();

    T parse(String input);

    default String getErrorMessage() {

        return "Invalid input";

    }

}
