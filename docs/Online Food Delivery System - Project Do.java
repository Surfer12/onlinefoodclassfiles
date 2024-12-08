Online Food Delivery System-Project Documentation 1. Object-Oriented Design Implementation 1.1 Encapsulation Our project demonstrates encapsulation through classes like Driver,which encapsulates driver-related data and behaviors:
public class Driver {
    private final String name;
    private final String licensePlate;
    private final List<Integer> ratings;
    private final List<Order> currentOrders;

    public Driver(final String name, final String licensePlate) {
        this.name = name;
        this.licensePlate = licensePlate;
        this.ratings = new ArrayList<>();
        this.currentOrders = new ArrayList<>();
    }

    public int getCurrentOrderCount() {
        return this.currentOrders.size();
    }

    public void assignOrder(final Order order) {
        this.currentOrders.add(order);
    }

    public void addRating(final int rating) {
        this.ratings.add(rating);
    }

    public double getAverageRating() {
        if (this.ratings.isEmpty())
            return 0.0;
        return this.ratings.stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);
    }

    public int getRatingCount() {
        return this.ratings.size();
    }

    // Getters
    public String getName() {
        return this.name;
    }

    public String getLicensePlate() {
        return this.licensePlate;
    }
}

1.2 Inheritance&Polymorphism

The system implements
inheritance through
the menu
item hierarchy
and polymorphic
behavior in
order processing.1.3
Abstraction We
use interfaces and
abstract classes to
provide abstraction layers,
particularly in:
Order processing
Payment handling
Driver management
Menu item management

2.
System Weaknesses&Limitations 2.1
User Interface
Issues
Menu
Navigation
Double
Enter required for
menu initialization
No clear
exit option
from sub-
menus Inconsistent
menu navigation
patterns
Input
Handling Lack
of proper validation for empty/null
inputs
No
standardized format for
customer IDs
Missing input constraints for
numerical values 2.2
Functional Limitations
Order Management
Duplicate item
handling issues
Limited order
modification capabilities
No order
history tracking
Driver System
Hardcoded limit of 5
drivers Inefficient
driver assignment
algorithm
Limited
driver rating
functionality

        .Proposed Improvements 3.1 Short-term Enhancements
User Interface
prompts
Input
Validation gracefully 3.2 Long-term Improvements
System Architecture
tracking
Business
Logic Validation for
types and values
.
