package model;

public class Drink implements MenuItem {
    private final Long id;
    private final String name;
    private final String description;
    private final double price;
    private final Size size;
    private final int quantity;

    public Drink(final Long id, final String name, final String description,
            final double price, final Size size, final int quantity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.size = size;
        this.quantity = quantity;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public double getPrice() {
        return this.price * this.quantity;
    }

    public Size getSize() {
        return this.size;
    }

    public int getQuantity() {
        return this.quantity;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public Long getId() {
        return this.id;
    }
}
