package validation;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;

/**
 * The ConsoleInputHandler class handles console input and validates it using
 * the provided InputValidator.
 *
 * @param <T> the type of input to handle
 *
 *            Usage example:
 * 
 *            Scanner scanner = new Scanner(System.in);
 *            InputValidator<Integer> validator = new
 *            PositiveIntegerValidator();
 *            ConsoleInputHandler<Integer> inputHandler = new
 *            ConsoleInputHandler<>(scanner, validator);
 *            Integer userInput = inputHandler.getInput("Enter a positive
 *            integer: ");
 *            System.out.println("You entered: " + userInput);
 */
public class ConsoleInputHandler<T> implements InputHandler<T> {
   private final InputValidator<T> inputValidator;
   private final Scanner scanner;

   /**
    * Constructs a ConsoleInputHandler with the specified InputValidator.
    *
    * @param inputValidator the InputValidator to use for validating input
    */
   public ConsoleInputHandler(final InputValidator<T> inputValidator) {
      this.inputValidator = inputValidator;
      this.scanner = new Scanner(System.in);
   }

   /**
    * Prompts the user for input and validates it.
    *
    * @param prompt the prompt to display to the user
    * @return the validated input
    */
   @Override
   public T getInput(final String prompt) {
      T input = null;
      boolean valid = false;
      while (!valid) {
         System.out.print(prompt);
         final String userInput = this.scanner.nextLine();
         try {
            final T parsedInput = this.inputValidator.parse(userInput);
            if (this.inputValidator.isValid(parsedInput)) {
               input = parsedInput;
               valid = true;
            } else {
               System.out.println(this.inputValidator.getErrorMessage());
            }
         } catch (final Exception e) {
            System.out.println("Error: " + e.getMessage());
         }
      }
      return input;
   }

   /**
    * Prompts the user for multiple inputs until the stop command is entered, and
    * validates each input.
    *
    * @param prompt      the prompt to display to the user
    * @param stopCommand the command to stop input
    * @return a list of validated inputs
    */

   @Override
   @SuppressWarnings("unchecked")
   public T[] getMultipleInputs(final String prompt, final String stopCommand) {
      final List<T> inputs = new ArrayList<>();
      while (true) {
         System.out.println(prompt);
         final String input = this.scanner.nextLine();
         if (input.equalsIgnoreCase(stopCommand)) {
            break;
         }
         try {
            final T value = this.inputValidator.parse(input);
            if (this.inputValidator.isValid(value)) {
               inputs.add(value);
            } else {
               System.out.println("Invalid input. Please enter a valid " + this.inputValidator.getTypeName() + ".");
            }
         } catch (final Exception e) {
            System.out.println("Error parsing input: " + e.getMessage());
         }
      }

      // Create an array of the correct type
      if (inputs.isEmpty()) {
         return (T[]) Array.newInstance(Object.class, 0);
      }

      // Use reflection to create an array of the correct type
      @SuppressWarnings("unchecked")
      final T[] result = inputs.toArray((T[]) Array.newInstance(inputs.get(0).getClass(), inputs.size()));
      return result;
   }

   public T handleInput(Scanner scanner, String prompt) {
       return this.getInput(prompt);
   }

   public T handleInput(Scanner scanner, String prompt, Predicate<T> condition) {
       while (true) {
           T input = this.getInput(prompt);
           if (condition.test(input)) {
               return input;
           }
           System.out.println("Input does not meet the required condition.");
       }
   }
}
