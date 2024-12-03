package services;

import java.util.ArrayList;
import java.util.List;

import model.MenuItem;
import model.Drink;
import model.Fries;
import model.Size;

public class MenuServiceImpl implements MenuService {
    private List<MenuItem> menuItems = new ArrayList<>();

    public MenuServiceImpl() {
        // Initialize menu items
        this.menuItems.add(new Drink(null, "Cola", "Refreshing cola drink", 1.99, Size.MEDIUM, 1));
        this.menuItems.add(new Fries(null, "Crispy Fries", "Golden and crispy fries", 3.49, Size.MEDIUM, 1));
    }

    @Override
    public void displayMenu() {
        System.out.println("\n=== Menu ===");
        for (int i = 0; i < this.menuItems.size(); i++) {
            MenuItem item = this.menuItems.get(i);
            System.out.printf("%d. %s - $%.2f\n",
                    i + 1,
                    item.getName(),
                    item.getPrice());
        }
    }

    @Override
    public List<MenuItem> getMenu() {
        return new ArrayList<>(this.menuItems);
    }

    @Override
    public MenuItem getMenuItemByIndex(int index) {
        if (index > 0 && index <= this.menuItems.size()) {
            return this.menuItems.get(index - 1);
        }
        throw new IllegalArgumentException("Invalid menu item index");
    }

    @Override
    public int getMenuSize() {
        return this.menuItems.size();
    }

    @Override
    public List<MenuItem> getAllMenuItems() {
        return new ArrayList<>(this.menuItems);
    }
}
