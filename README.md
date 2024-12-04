## Getting Started

Welcome to the VS Code Java world. Here is a guideline to help you get started to write Java code in Visual Studio Code.

## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## Dependency Management

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).

## Project Setup

To set up the project, follow these steps:

1. Clone the repository:
   ```sh
   git clone https://github.com/Surfer12/onlinefoodclassfiles.git
   cd onlinefoodclassfiles
   ```

2. Open the project in Visual Studio Code:
   ```sh
   code .
   ```

3. Ensure you have the necessary Java Development Kit (JDK) installed. The project requires JDK 11 or higher. You can download it from [here](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html).

4. Install the required extensions in Visual Studio Code:
   - Java Extension Pack
   - CheckStyle for Java

5. Build the project:
   ```sh
   ./gradlew build
   ```

## Running the Project

To run the project, follow these steps:

1. Open the terminal in Visual Studio Code.

2. Navigate to the project directory if not already there:
   ```sh
   cd onlinefoodclassfiles
   ```

3. Run the application:
   ```sh
   ./gradlew run
   ```

## Contributing to the Project

We welcome contributions to the project. To contribute, follow these steps:

1. Fork the repository on GitHub.

2. Clone your forked repository:
   ```sh
   git clone https://github.com/<your-username>/onlinefoodclassfiles.git
   cd onlinefoodclassfiles
   ```

3. Create a new branch for your feature or bug fix:
   ```sh
   git checkout -b feature-or-bugfix-name
   ```

4. Make your changes and commit them with a descriptive message:
   ```sh
   git add .
   git commit -m "Description of the changes"
   ```

5. Push your changes to your forked repository:
   ```sh
   git push origin feature-or-bugfix-name
   ```

6. Create a pull request on the original repository, describing your changes and the problem they solve.

7. Wait for the project maintainers to review your pull request. They may request changes or ask for additional information.

8. Once your pull request is approved, it will be merged into the main branch.

Thank you for contributing!
