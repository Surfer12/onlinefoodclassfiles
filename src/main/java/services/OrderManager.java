package services;

import java.util.Scanner;

import managers.MenuManager;
import queue.OrderQueue;
import validation.ConsoleInputHandler;

public interface OrderManager {
    void processOrderPlacement(
            Scanner scanner,
            MenuManager menuManager,
            ConsoleInputHandler<Integer> positiveIntegerHandler,
            ConsoleInputHandler<String> emailHandler,
            ConsoleInputHandler<String> locationHandler);

    OrderQueue getOrderQueue();
}
