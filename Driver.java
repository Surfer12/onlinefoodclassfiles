import java.util.List;
import java.util.ArrayList;

public class Driver {
    private String name;
    private String licensePlate;
    private List<Integer> ratings;
    private List<Order> currentOrders;

    public Driver(String name, String licensePlate) {
        this.name = name;
        this.licensePlate = licensePlate;
        this.ratings = new ArrayList<>();
        this.currentOrders = new ArrayList<>();
    }

    public int getCurrentOrderCount() {
        return currentOrders.size();
    }

    public void assignOrder(Order order) {
        currentOrders.add(order);
    }

    public void addRating(int rating) {
        ratings.add(rating);
    }

    public double getAverageRating() {
        if (ratings.isEmpty())
            return 0.0;
        return ratings.stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);
    }

    public int getRatingCount() {
        return ratings.size();
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getLicensePlate() {
        return licensePlate;
    }
}
