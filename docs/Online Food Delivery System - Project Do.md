Online Food Delivery System-Project Documentation

Notes on new implementation of the project :
 "We are now onto theroretical improvements and online systems with less limited to unlimited input qualitities and not constrained to the java cli input."

This alteration is significant a requires careful analysis of the project menu navigation, the driver assignement algorithm creation, and the order processing.


1. Object-Oriented Design Implementation

1.1 Encapsulation Our project demonstrates encapsulation through classes like Driver,which encapsulates driver-related data and behaviors:


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

1.2

Inheritance&Polymorphism

The system implements
inheritance through
the

menu
item hierarchy
and

polymorphic
behavior in
order processing.

1.3

Abstraction

Weuse interfaces and
abstract classes to
provide abstraction layers,
particularly in:


Order processing
Payment handling
Driver management
Menu item management

2.
System Weaknesses & Limitations


2.1
User Interface Issues

Menu
Navigation

Double
Enter required for
menu initialization


Welcome to the Online Food Delivery System!
Press Enter to start... // this is pressed twice to get the menu



=== Online Food Delivery System ===
1. Place a New Order (Add items to cart and checkout)
2. Check Order Status (View status of an existing order)
3. View Menu (See available items and prices)
4. Manage Drivers (Add/Remove/List drivers)
5. Rate Driver (Rate a driver for a completed order)
6. Calculate Order Total (View total for an existing order)
7. Manage Driver Ratings (View/Update driver ratings)
8. Process Orders (Process all pending orders for delivery)
9. Exit
=====================================
Please choose an option (1-9): Please enter a valid option.
2

=== Online Food Delivery System ===
1. Place a New Order (Add items to cart and checkout)
2. Check Order Status (View status of an existing order)
3. View Menu (See available items and prices)
4. Manage Drivers (Add/Remove/List drivers)
5. Rate Driver (Rate a driver for a completed order)
6. Calculate Order Total (View total for an existing order)
7. Manage Driver Ratings (View/Update driver ratings)
8. Process Orders (Process all pending orders for delivery)
9. Exit
=====================================
Please choose an option (1-9):
=== Check Order Status ===
Enter your order ID to check its current status.
Enter Order ID to check status: 9
// should be have an option to exit the chooseing of an option without returning needing to enter an incorrect number option
Order not found. // we then get this once it's not found
// Then press enter to get the menu.


No clear
exit option
from sub-
menus

Inconsistent
menu navigation
patterns


Input
Handling Lack
of proper validation for empty/null
inputs as enter is used to navigate menus. This lack of proper null and empty validation is a major issue, as it can lead to unexpected behavior and errors in the system.

No
standardized format for
customer IDs

Missing input constraints for
numerical values

2.2
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

drivers

absent
driver assignment
algorithm

Proposed Improvements 3.1 Short-term Enhancements
User Interface
- Implement single-press menu initialization
- Add consistent exit options across all menus
- Standardize navigation patterns
- Add clear error messages and user prompts
Prompts
Input
- Implement comprehensive input validation
- Add format requirements for IDs and numbers
- Create user-friendly error messages
- Handle empty/null inputs gracefully

Validation gracefully 3.2 Long-term Improvements
System Architecture
- Implement database integration for persistent storage
- Add user authentication system
- Create separate admin and user interfaces
- Implement real-time order tracking
Tracking and logging of business logic through the system by improving the custom errors that were created.

The use of these custom errors were invaluable in inital development, as they provided a clear and concise explanation of the issue at hand.


Business Logic Enhancements for Online System Creation
- Dynamic driver pool management
- Improved order assignment algorithm
- Enhanced rating system with detailed feedback
- Comprehensive order modification capabilities

Validation for types and values

