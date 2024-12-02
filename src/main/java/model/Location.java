package model;

public class Location {
    private final String address;
    private final String postalCode;

    public Location(final String address, final String postalCode) {
        this.address = address;
        this.postalCode = postalCode;
    }

    public String getAddress() {
        return this.address;
    }

    public String getPostalCode() {
        return this.postalCode;
    }
}