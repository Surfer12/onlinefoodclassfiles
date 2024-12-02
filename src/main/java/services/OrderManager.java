package services;

import java.util.Scanner;

import managers.MenuManager;
import validation.ConsoleInputHandler;

public interface OrderManager {
    // ...existing code...
    void processOrderPlacement(
            Scanner scanner,
            MenuManager menuManager,
            ConsoleInputHandler<Integer> positiveIntegerHandler,
            ConsoleInputHandler<String> emailHandler,
            ConsoleInputHandler<String> locationHandler);
    // ...existing code...
}
