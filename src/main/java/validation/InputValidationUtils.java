package validation;

import CustomException.ValidationException;

/**
 * Utility class for input validation.
 *
 * <p>Usage example:
 * <pre>
 * {@code
 * String input = "example";
 * InputValidationUtils.validateTextInput(input, "Field");
 * }
 * </pre>
 */
public class InputValidationUtils {

   /**
    * Validates that the input text is not null or empty.
    *
    * @param input the input text to validate
    * @param fieldName the name of the field being validated
    * @throws ValidationException if the input is null or empty
    */
   public static void validateTextInput(String input, String fieldName) {
      try {
         if (input == null || input.trim().isEmpty()) {
            throw new ValidationException(fieldName + " cannot be null or empty");
         }
      } catch (ValidationException e) {
         System.err.println("Error in validateTextInput: " + e.getMessage());
         throw e;
      }
   }

   /**
    * Validates that the input text is a valid number.
    *
    * @param input the input text to validate
    * @param fieldName the name of the field being validated
    * @throws ValidationException if the input is not a valid number
    */
   public static void validateNumericInput(String input, String fieldName) {
      try {
         Double.parseDouble(input);
      } catch (NumberFormatException e) {
         throw new ValidationException(fieldName + " must be a valid number");
      } catch (ValidationException e) {
         System.err.println("Error in validateNumericInput: " + e.getMessage());
         throw e;
      }
   }

   /**
    * Validates that the number is positive.
    *
    * @param number the number to validate
    * @param fieldName the name of the field being validated
    * @throws ValidationException if the number is not positive
    */
   public static void validatePositiveNumber(double number, String fieldName) {
      try {
         if (number <= 0) {
            throw new ValidationException(fieldName + " must be greater than zero");
         }
      } catch (ValidationException e) {
         System.err.println("Error in validatePositiveNumber: " + e.getMessage());
         throw e;
      }
   }

   /**
    * Validates that the email format is valid.
    *
    * @param email the email to validate
    * @throws ValidationException if the email format is invalid
    */
   public static void validateEmailFormat(String email) {
      try {
         String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
         if (!email.matches(emailRegex)) {
            throw new ValidationException("Invalid email format");
         }
      } catch (ValidationException e) {
         System.err.println("Error in validateEmailFormat: " + e.getMessage());
         throw e;
      }
   }

   /**
    * Validates that the phone number format is valid.
    *
    * @param phoneNumber the phone number to validate
    * @throws ValidationException if the phone number format is invalid
    */
   public static void validatePhoneNumber(String phoneNumber) {
      try {
         String phoneRegex = "^\\+?[0-9]{10,15}$";
         if (!phoneNumber.matches(phoneRegex)) {
            throw new ValidationException("Invalid phone number format");
         }
      } catch (ValidationException e) {
         System.err.println("Error in validatePhoneNumber: " + e.getMessage());
         throw e;
      }
   }
}
