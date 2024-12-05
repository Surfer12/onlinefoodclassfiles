package services;

import java.util.List;

import model.MenuItem;

public interface MenuService {
    void displayMenu();

    List<MenuItem> getMenu();

    MenuItem getMenuItemByIndex(final int index);

    int getMenuSize();

    String getMenuItemNameByIndex(final int index);

    List<MenuItem> getAllMenuItems();

    MenuItem getMenuItemById(final long id);
}
