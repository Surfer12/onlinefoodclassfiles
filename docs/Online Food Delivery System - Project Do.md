

Notes on new implementation of the project :
"We are now onto theoretical improvements and online systems with less limited to unlimited input qualities and not constrained to the java cli input."


This alteration is significant and requires careful analysis of the project menu navigation, the driver assignment algorithm creation, and the order processing.




1. Object-Oriented Design Implementation

## Encapsulation

# Our project demonstrates encapsulation through classes like
- Driver,which encapsulates driver-related data and behaviors:
    - src/main/java/model/Driver.java
```java
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
```

## Inheritance & Polymorphism

The system implements
inheritance through
the

menu
item hierarchy
and

polymorphic
behavior in
order processing.

## Abstraction

Weuse interfaces and
abstract classes to
provide abstraction layers,
particularly in:


Order processing
Payment handling
Driver management
Menu item management

## System Weaknesses & Limitations

## User Interface Issues

### 2.1
User Interface Issues

#### Menu
Navigation

### Double Enter required for menu initialization

Should use a \ or / to navigate menus forwards and backwards.
```
Welcome to the Online Food Delivery System!
Press Enter to start... // this is pressed twice to get the menu

```
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

```
No clear exit option from sub-menus

Inconsistent menu navigation patterns

```
Input Handling
Lack of proper validation for empty/nul inputs as enter is used to navigate menus.

Eliminates the confusion around menu initialization

```
Current CLI Implementation (referenced in):
```java
    public void start() {
        System.out.println("Welcome to the Online Food Delivery System!");
        System.out.println("Press Enter to start...");
        this.scanner.nextLine(); // Single enter to initialize

        try {
            while (this.running && this.scanner.hasNextLine()) {
                this.displayMainMenu();

                final String input = this.scanner.nextLine().trim();

                // Handle exit command directly
                if ("exit".equalsIgnoreCase(input) || "9".equals(input)) {
                    this.cleanup();
                    break;
                }

                if (input.isEmpty()) {
                    System.out.println("Please enter a valid option.");
                    continue;
                }

                try {
                    final int choice = Integer.parseInt(input);
                    if (choice < 1 || choice > 9) {
                        System.out.println("Invalid menu choice. Please enter a number between 1 and 9.");
                        continue;
                    }
                    this.handleMenuChoice(choice);
                } catch (final NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number between 1 and 9.");
                } catch (final Exception e) {
                    DeliverySystemCLI.logger.log(Level.SEVERE, "An unexpected error occurred", e);
                    System.out.println("An unexpected error occurred: " + e.getMessage());
                }
            }
        } catch (final Exception e) {
            DeliverySystemCLI.logger.log(Level.SEVERE, "An unexpected error occurred", e);
        }
    }

Proposed improvement for the input handling and navigation system using a forward and back navigation system with the slash character as the forward navigation and the backslash character as the back navigation. This will allow for a more intuitive and user-friendly navigation experience.

```
```java
public class DeliverySystemCLI {
    private static final String FORWARD_NAVIGATION = "/";
    private static final String BACK_NAVIGATION = "\\";

    public void processInput(String input) {
        if (input == null || input.trim().isEmpty()) {
            notificationService.showError("Input cannot be empty. Use '/' to proceed or '\\' to go back.");
            return;
        }

        switch (input.trim()) {
            case FORWARD_NAVIGATION -> handleForwardNavigation();
            case BACK_NAVIGATION -> handleBackNavigation();
            default -> processMenuChoice(input);
        }
    }

    private void handleForwardNavigation() {
        if (currentMenu.hasNextLevel()) {
            currentMenu = currentMenu.getNextLevel();
            displayCurrentMenu();
        } else {
            notificationService.showInfo("Already at deepest menu level");
        }
    }

    private void handleBackNavigation() {
        if (currentMenu.hasPreviousLevel()) {
            currentMenu = currentMenu.getPreviousLevel();
            displayCurrentMenu();
        } else {
            notificationService.showInfo("Already at main menu");
        }
    }
}
```

## Standardized Customer ID Format

- No standardized format for customer IDs
- Missing input constraints for numerical values

## Functional Limitations
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

## Proposed Improvements
### Short-term Enhancements
#### User Interface
- Implement single-press menu initialization
- Add consistent exit options across all menus
- Standardize navigation patterns
- Add clear error messages and user prompts

### Prompts
#### Input
- Implement comprehensive input validation
- Add format requirements for IDs and numbers
- Create user-friendly error messages
- Handle empty/null inputs gracefully

### Validation gracefully
#### Long-term Improvements
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

## Validation for types and values

