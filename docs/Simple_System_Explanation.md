# Simple Guide to Our Online Food Delivery System

## What Is This System?
Think of this system as a digital restaurant waiter that helps you order food and get it delivered to your door. It's like having a friendly helper who takes your order, finds a delivery driver, and makes sure your food arrives safely.

## Main Parts of the System

### 1. The Menu System
- Just like a paper menu at a restaurant, but digital
- Shows you all available foods and drinks
- Tells you the prices
- Lets you pick what you want to eat

### 2. The Ordering System
- Works like an online shopping cart
- You can:
  - Add food items you want
  - See how much everything costs
  - Enter your delivery address
  - Place your order

### 3. The Delivery System
- Think of it as a team of delivery helpers who:
  - Find the best driver for your order
  - Make sure your food gets delivered
  - Let you track where your order is
  - Allow you to rate your delivery experience

### 4. The Driver System
- Like a scheduling system for delivery drivers
- Keeps track of:
  - Available drivers
  - How well drivers are rated
  - How many deliveries each driver is handling
  - Which drivers are free to take new orders

## How It All Works Together

1. **Placing an Order**
   - You browse the menu and pick your food
   - Add items to your order
   - Enter your address
   - Confirm your order

2. **Behind the Scenes**
   - The system finds an available driver
   - Assigns your order to the driver
   - Keeps track of your order's status

3. **Delivery Process**
   - Driver picks up your food
   - System updates you on the order status
   - Food gets delivered to your address
   - You can rate the delivery service

## Cool Features

- **Rating System**: Like giving stars on a review, you can rate your delivery experience
- **Order Tracking**: You can check where your order is at any time
- **Driver Management**: The system makes sure drivers aren't overloaded with too many orders
- **Easy Navigation**: Simple menus to help you find what you want

## Future Improvements

The system is being upgraded to work better online with new features like:
- More payment options
- Better tracking of orders
- Driver assignment algorithm and management
- Driver updates to customers
- Detailed feedback system
- Ability to modify orders
- Better order Handling of busy times for concurrent orders

## Current System Behaviors and Known Issues

### Navigation and Interface
- System initialization requires pressing enter twice to start
- Main menu navigation requires double enter when going back
- Exit option is needed from sub-menus (especially in driver management)
- Space or null inputs don't trigger proper error messages

### Order Management
- Customer ID requires specific number of digits (to be specified)
- Duplicate items may not be added properly after quantity is entered
- Order modifications available:
  - Add new items
  - Change item quantities
  - Delete items from order

### Driver System
- Currently limited to 5 hardcoded drivers
- New drivers are added to the beginning of the list
- License plate format needs standardization
- Driver ID is required for various operations
- Options 4 (Manage Drivers) and 7 (Manage Driver Ratings) have overlapping functionality
- Driver management:
  - Can add/delete drivers
  - Can view driver information
  - Rating updates not available in manager view

### Rating System
- Ratings are tied to Driver ID (not Order ID)
- Can add and delete ratings
- Process orders functionality needs improvement

## Known Limitations

1. **Input Validation**
   - Inconsistent error handling for empty inputs
   - Need better validation for customer IDs
   - License plate format needs standardization

2. **Driver Management**
   - Fixed limit of 5 drivers
   - Incomplete rating management in driver info view
   - Process orders functionality needs enhancement

3. **User Interface**
   - Multiple enter keypresses required for navigation
   - Unclear exit paths from sub-menus
   - Similar menu options causing confusion

## Simple Terms for Technical Words

- **CLI (Command Line Interface)**: The current text-based way to use the system
- **Input Validation**: Making sure the information you enter is correct
- **Driver Pool**: The group of available delivery drivers
- **Order Status**: Where your food is in the delivery process
```
