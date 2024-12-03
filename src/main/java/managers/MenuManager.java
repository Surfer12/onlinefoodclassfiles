package managers;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import model.MenuItem;
import services.MenuService;
import services.impl.MenuServiceImpl;
import validation.ConsoleInputHandler;
import validation.InputValidatorImpl;
import validation.MenuItemValidator;

public class MenuManager {
    private final MenuService menuService;
    private final ConsoleInputHandler<Integer> menuChoiceHandler;

    public MenuManager() {
        this.menuService = new MenuServiceImpl();
        final int menuSize = this.menuService.getMenuSize();
        this.menuChoiceHandler = new ConsoleInputHandler<>(
                new InputValidatorImpl<>(
                        new MenuItemValidator(menuSize),
                        "Menu Choice",
                        "Invalid menu choice"));
    }

    public void displayMenu() {
        final List<MenuItem> menu = this.menuService.getAllMenuItems();
        System.out.println("\n--- Current Menu ---");
        for (int i = 0; i < menu.size(); i++) {
            final MenuItem item = menu.get(i);
            System.out.printf("%d. %s - $%.2f\n", i + 1, item.getName(), item.getPrice());
        }
    }

    public List<MenuItem> selectMenuItems(final Scanner scanner, final ConsoleInputHandler<Integer> positiveIntegerHandler) {
        System.out.println("\n=== Place Order ===");
        this.displayMenu();

        final List<MenuItem> orderItems = new ArrayList<>();
        final boolean addingItems = true;

        while (addingItems) {
            System.out.print("Enter menu item number to add (0 to finish): ");
            final Integer itemChoice = positiveIntegerHandler.handleInput(
                    scanner,
                    "Select a menu item: ");

            if (itemChoice == null) {
                System.out.println("Invalid input. Please enter a valid menu item number.");
                continue;
            }

            if (itemChoice == 0) {
                break;
            }

            if (itemChoice < 1 || itemChoice > this.menuService.getMenuSize()) {
                System.out.println("Invalid menu item number. Please choose a number between 1 and " + this.menuService.getMenuSize());
                continue;
            }

            final MenuItem selectedItem = this.menuService.getMenuItemByIndex(itemChoice);
            System.out.print("Enter quantity: ");
            final Integer quantity = positiveIntegerHandler.handleInput(
                    scanner,
                    "Enter quantity: ");

            if (quantity == null || quantity <= 0 || quantity > 10) {
                System.out.println("Invalid quantity. Please enter a number between 1 and 10.");
                continue;
            }

            for (int i = 0; i < quantity; i++) {
                orderItems.add(selectedItem);
            }
        }

        return orderItems;
    }

    public ConsoleInputHandler<Integer> getMenuChoiceHandler() {
        return this.menuChoiceHandler;
    }

    public MenuService getMenuService() {
        return this.menuService;
    }
}
