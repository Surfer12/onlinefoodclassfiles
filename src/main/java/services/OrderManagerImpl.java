package services;

import java.util.Scanner;

import managers.MenuManager;
import validation.ConsoleInputHandler;

public class OrderManagerImpl implements OrderManager {
    // ...existing code...

    @Override
    public void processOrderPlacement(
            final Scanner scanner,
            final MenuManager menuManager,
            final ConsoleInputHandler<Integer> positiveIntegerHandler,
            final ConsoleInputHandler<String> emailHandler,
            final ConsoleInputHandler<String> locationHandler) {
        // ...existing code...

        // Use emailHandler to get and validate email
        final String email = emailHandler.getInput("Enter your email: ");

        // Use locationHandler to get and validate delivery location
        final String location = locationHandler.getInput("Enter delivery location: ");

        // ...existing code to process the order with email and location...
    }

    // ...existing code...
}
