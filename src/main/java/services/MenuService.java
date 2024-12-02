package services;

import java.util.List;

import model.MenuItem;

public interface MenuService {
    void displayMenu();

    List<MenuItem> getMenu();

    MenuItem getMenuItemByIndex(int index);

    int getMenuSize();

    List<MenuItem> getAllMenuItems();
}