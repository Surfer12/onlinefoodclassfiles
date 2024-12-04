## Getting Started

Welcome to the Online Food Delivery System project. This guide will help you set up and run the application.

## Prerequisites

- Java Development Kit (JDK) 8 or higher
- Gradle (for dependency management and building the project)

## Folder Structure

The workspace contains the following folders:

- `src`: the folder to maintain sources
  - `main`: contains the main application code
    - `java`: contains the Java source files
      - `app`: contains the main application classes
      - `CustomException`: contains custom exception classes
      - `notification`: contains notification-related classes
      - `observer`: contains observer-related classes
  - `test`: contains the test code
    - `java`: contains the Java test files
      - `app`: contains the test classes for the main application
- `lib`: the folder to maintain dependencies
- `bin`: the folder to maintain compiled output files

## Setting Up the Application

1. Clone the repository:
   ```
   git clone https://github.com/Surfer12/onlinefoodclassfiles.git
   cd onlinefoodclassfiles
   ```

2. Open the project in your preferred IDE (e.g., Visual Studio Code, IntelliJ IDEA).

3. Build the project using Gradle:
   ```
   ./gradlew build
   ```

4. Run the application:
   ```
   ./gradlew run
   ```

## Project Overview

The Online Food Delivery System is a Java-based application that allows users to place and manage food orders. The project is organized into several packages, each responsible for different aspects of the system:

- `app`: Contains the main application classes, including the entry point (`Application.java`) and the delivery system logic (`DeliverySystem.java`).
- `CustomException`: Contains custom exception classes used for error handling.
- `notification`: Contains classes related to sending notifications to customers and drivers.
- `observer`: Contains classes implementing the observer pattern for tracking order events.

## Functionality

The application provides the following functionality:

- Place a new order
- Check order status
- View menu
- Manage drivers
- Rate drivers
- Calculate order total
- Manage driver ratings
- Process orders in the correct order

## Testing

The project includes unit tests for major components and services. To run the tests, use the following command:
```
./gradlew test
```

## Logging

The application uses Java's built-in logging framework to log messages at different levels (INFO, WARN, ERROR). Log messages are used throughout the application to provide information about the application's execution and to help with debugging.

## Error Handling

The application uses custom exception classes (`OrderProcessingException`, `ValidationException`) to handle errors consistently across the system. These exceptions provide meaningful error messages and help in identifying the source of errors.

## Dependency Management

The project uses Gradle for dependency management and building the project. Gradle helps in maintaining a consistent project structure and simplifies the build process.

## Code Quality and Readability

The codebase follows consistent naming conventions and formatting. Large methods are broken down into smaller, more manageable ones to improve readability and maintainability.

## Modularization

The codebase is modularized by separating different concerns into distinct packages. For example, notification-related classes are in the `notification` package, and observer-related classes are in the `observer` package.

## Documentation

Javadoc comments are added to all public classes and methods to improve code documentation and maintainability. The `README.md` file provides detailed instructions on how to set up and run the application, as well as an overview of the project's structure and functionality.
