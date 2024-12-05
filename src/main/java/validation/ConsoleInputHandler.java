package validation;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Predicate;

/**
 * The ConsoleInputHandler class handles console input and validates it using
 * the provided InputValidator with improved resource management and timeout
 * handling.
 *
 * @param <T> the type of input to handle
 */
public class ConsoleInputHandler<T> implements InputHandler<T>, AutoCloseable {
   private final InputValidator<T> inputValidator;
    private final Scanner scanner;
    private final long timeoutSeconds;
    private final ExecutorService executor;

    /**
     * Constructs a ConsoleInputHandler with the specified InputValidator.
     *
     * @param inputValidator the InputValidator to use for validating input
     * @param timeoutSeconds timeout in seconds for input operations
     */
    public ConsoleInputHandler(final InputValidator<T> inputValidator, final long timeoutSeconds) {
       this.inputValidator = inputValidator;
        this.scanner = new Scanner(System.in);
        this.timeoutSeconds = timeoutSeconds;
        this.executor = Executors.newSingleThreadExecutor();
     }

    /**
     * Constructs a ConsoleInputHandler with default 30-second timeout.
     *
     * @param inputValidator the InputValidator to use for validating input
     */
    public ConsoleInputHandler(final InputValidator<T> inputValidator) {
       this(inputValidator, 30);
    }

    @Override
    public void close() {
       this.scanner.close();
       this.executor.shutdownNow();
    }

    private String getInputWithTimeout(final String prompt) throws TimeoutException {
       System.out.print(prompt);
       final Future<String> future = this.executor.submit(() -> this.scanner.nextLine());
       try {
          return future.get(this.timeoutSeconds, TimeUnit.SECONDS);
       } catch (final Exception e) {
          future.cancel(true);
          throw new TimeoutException("Input operation timed out after " + this.timeoutSeconds + " seconds");
       }
    }

    @Override
    public T getInput(final String prompt) {
       T input = null;
       boolean valid = false;

        while (!valid) {
           try {
              final String userInput = this.getInputWithTimeout(prompt);
              try {
                 final T parsedInput = this.inputValidator.parse(userInput);
                 if (this.inputValidator.isValid(parsedInput)) {
                    input = parsedInput;
                    valid = true;
                 } else {
                    System.out.println(this.inputValidator.getErrorMessage());
                 }
                 } catch (final NumberFormatException e) {
                    System.out.println("Invalid number format: Please enter a valid " +
                          this.inputValidator.getTypeName());
                 } catch (final IllegalArgumentException e) {
                    System.out.println("Invalid input format: " + e.getMessage());
                 } catch (final Exception e) {
                   System.out.println("Error processing input: " + e.getMessage());
                }
             } catch (final TimeoutException e) {
                System.out.println(e.getMessage() + ". Please try again.");
             }
          }
          return input;
       }

    @Override
    public T[] getMultipleInputs(final String prompt, final String stopCommand) {
       final List<T> inputs = new ArrayList<>();

        while (true) {
           try {
              final String input = this.getInputWithTimeout(prompt + " (or enter '" + stopCommand + "' to finish): ");
              if (input.equalsIgnoreCase(stopCommand)) {
                 break;
              }

                try {
                   final T value = this.inputValidator.parse(input);
                   if (this.inputValidator.isValid(value)) {
                      inputs.add(value);
                   } else {
                       System.out.println("Invalid input. Please enter a valid " +
                             this.inputValidator.getTypeName() + ".");
                    }
                 } catch (final NumberFormatException e) {
                    System.out.println("Invalid number format: Please enter a valid " +
                          this.inputValidator.getTypeName());
                 } catch (final IllegalArgumentException e) {
                    System.out.println("Invalid input format: " + e.getMessage());
                 }
              } catch (final TimeoutException e) {
                 System.out.println(e.getMessage() + ". Please try again.");
              }
           }

        if (inputs.isEmpty()) {
           @SuppressWarnings("unchecked")
           final T[] emptyArray = (T[]) Array.newInstance(
                 this.inputValidator.getClass().getComponentType(), 0);
           return emptyArray;
        }

        @SuppressWarnings("unchecked")
        final T[] result = (T[]) Array.newInstance(
              inputs.get(0).getClass(), inputs.size());
        return inputs.toArray(result);
     }

    public T handleInput(final String prompt, final Predicate<T> condition) {
       while (true) {
          final T input = this.getInput(prompt);
          if (condition.test(input)) {
             return input;
          }
            System.out.println("Input does not meet the required condition. Please try again.");
         }
     }
}
