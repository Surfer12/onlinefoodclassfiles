package model;

public abstract class Person {
    private final Long id;
    private final String name;
    private final String email;
    private final String address;

    public Person(final Long id, final String name, final String email, final String address) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.address = address;
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public String getAddress() {
        return this.address;
    }
}