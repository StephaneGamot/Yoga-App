# Yoga App

Yoga App is an application designed to simplify the process for users to join or leave yoga classes, while also providing administrators with the tools to effectively schedule sessions based on the availability of instructors.

## Technologies

This project uses several key technologies:

- **Frontend:** Generated with [Angular CLI](https://github.com/angular/angular-cli) version 14.2.
- **Backend:** Java 11+, Spring Boot.
- **Database:** MySQL.
- **Testing:** Jest 28.1, Cypress, JUnit, Mockito, Jacoco for code coverage.

## Getting Started

### Prerequisites

Ensure you have the following installed:
- NodeJS v16.20
- Java JDK 11 or higher
- MySQL Database

### Setup

1. Clone the repository:
    ```
    git clone https://github.com/White-Wolf-Web/Yoga-App
    ```
2. Install dependencies:
    ```
    npm install
    ```
3. Start the frontend application:
    ```
    npm run start
    ```

### Configuring MySQL

1. A SQL script for creating the schema is available at `resources/sql/script.sql`.
2. By default, the admin account is:
    - login: yoga@studio.com
    - password: test!1234
3. Confirm the installation and correct setup of a MySQL database instance on your system.
4. Run the database setup script found in the `resources/sql` directory to create the required database structure.
5. Enter your database connection details in the `application.properties` configuration file.

### Backend Setup

1. Navigate to the backend project directory
2. Install Maven dependencies:
    ```
    mvn install
    ```

### Testing

#### Frontend Testing

##### E2E Testing with Cypress

- Launching E2E tests:
    ```
    npm run e2e
    ```
- Generate coverage report (launch E2E tests beforehand):
    ```
    npm run e2e:coverage
    ```
- The report is available at `front/coverage/lcov-report/index.html`. Right-click and open with Live Server.

##### Unit Testing with Jest

- Launching tests:
    ```
    npm run test
    ```
- For following changes:
    ```
    npm run test:watch
    ```
- Coverage report is at `front/coverage/jest/lcov-report/index.html`. Right-click and open with Live Server.

#### Backend Testing

##### Backend Testing with JUnit5 and Mockito

- In the backend project directory, execute `mvn test` to perform backend tests with JUnit and Mockito.
- Coverage reports are generated at `back/target/site/jacoco/index.html`.

### Launching the Backend

- In the backend project folder, start the backend server with:
    ```
    mvn spring-boot:run
    ```
- Don’t forget to run the database script located in the `resources/sql` folder before starting tests.


## Contributors

Stéphane Gamot
