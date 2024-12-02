package services.impl;

import java.util.ArrayList;
import java.util.List;

import model.Drink;
import model.Fries;
import model.Hamburger;
import model.MenuItem;
import model.Size;
import services.MenuService;

public class MenuServiceImpl implements MenuService {
    private final List<MenuItem> menu;

    public MenuServiceImpl() {
        this.menu = new ArrayList<>();
        this.initializeMenu();
    }

    private void initializeMenu() {
        this.menu.add(new Hamburger(0L, "Hamburger", "A simple hamburger", 5.99, Size.MEDIUM, 1));
        this.menu.add(new Hamburger(0L, "Cheeseburger", "A cheeseburger with cheese", 6.49, Size.MEDIUM, 1));
        this.menu.add(new Fries(0L, "Fries", "A simple fries", 2.99, Size.MEDIUM, 1));
        this.menu.add(new Drink(0L, "Soft Drink", "A soft drink", 1.99, Size.MEDIUM, 1));
    }

    @Override
    public void displayMenu() {
        System.out.println("\n=== Menu ===");
        for (int i = 0; i < this.menu.size(); i++) {
            MenuItem item = this.menu.get(i);
            System.out.printf("%d. %s - $%.2f\n",
                    i + 1,
                    item.getName(),
                    item.getPrice());
        }
    }

    @Override
    public List<MenuItem> getMenu() {
        return new ArrayList<>(this.menu);
    }

    @Override
    public MenuItem getMenuItemByIndex(int index) {
        if (index > 0 && index <= this.menu.size()) {
            return this.menu.get(index - 1);
        }
        throw new IllegalArgumentException("Invalid menu item index");
    }

    @Override
    public int getMenuSize() {
        return this.menu.size();
    }

    @Override
    public List<MenuItem> getAllMenuItems() {
        return new ArrayList<>(this.menu);
    }
}