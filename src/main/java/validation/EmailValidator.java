package validation;

import java.util.regex.Pattern;

public class EmailValidator implements Validator<String> {
   private static final Pattern EMAIL_PATTERN = Pattern.compile(
         "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

   @Override
   public String parse(final String input) {
      return input.trim();
   }

   @Override
   public boolean isValid(final String email) {
      return email != null && EmailValidator.EMAIL_PATTERN.matcher(email).matches();
   }

   @Override
   public String getTypeName() {
      return "email address";
   }

   @Override
   public boolean validate(final String input) {
      return input != null && EmailValidator.EMAIL_PATTERN.matcher(input).matches();
   }
}
