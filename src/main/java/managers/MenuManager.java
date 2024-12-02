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
        int menuSize = this.menuService.getMenuSize();
        this.menuChoiceHandler = new ConsoleInputHandler<>(
                new InputValidatorImpl<>(
                        new MenuItemValidator(menuSize),
                        "Menu Choice",
                        "Invalid menu choice"));
    }

    public void displayMenu() {
        List<MenuItem> menu = this.menuService.getAllMenuItems();
        System.out.println("\n--- Current Menu ---");
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.get(i);
            System.out.printf("%d. %s - $%.2f\n", i + 1, item.getName(), item.getPrice());
        }
    }

    public List<MenuItem> selectMenuItems(Scanner scanner, ConsoleInputHandler<Integer> positiveIntegerHandler) {
        System.out.println("\n=== Place Order ===");
        this.displayMenu();

        List<MenuItem> orderItems = new ArrayList<>();
        boolean addingItems = true;

        while (addingItems) {
            System.out.print("Enter menu item number to add (0 to finish): ");
            Integer itemChoice = positiveIntegerHandler.handleInput(
                    scanner,
                    "Select a menu item: ",
                    input -> input >= 0 && input <= this.menuService.getMenuSize());

            if (itemChoice == null || itemChoice == 0) {
                addingItems = false;
                continue;
            }

            MenuItem selectedItem = this.menuService.getMenuItemByIndex(itemChoice);
            System.out.print("Enter quantity: ");
            Integer quantity = positiveIntegerHandler.handleInput(
                    scanner,
                    "Enter quantity: ",
                    input -> input > 0 && input <= 10);

            if (quantity != null) {
                for (int i = 0; i < quantity; i++) {
                    orderItems.add(selectedItem);
                }
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