# Airline Ticket Booking System

This is a desktop application for managing airline ticket bookings. It allows administrators to manage customer data, flight information, book tickets, and generate E-tickets.

## Key Features

*   **Admin Login:** Secure login for administrators to access the system.
*   **Customer Management:**
    *   Add new customers with details like name, contact, address, passport, national ID, and date of birth.
    *   Search for existing customers using their unique Customer ID.
*   **Flight Management:**
    *   Add new flights with details such as flight name, arrival/departure locations, duration, available seats, fare, and date of flight.
    *   (Implicitly, flight details are used in booking, though direct search by ID might not be a separate menu item but used internally).
*   **Ticket Booking:**
    *   Book tickets for customers by selecting arrival and departure locations.
    *   The system helps in finding available flights based on the selected route.
    *   Calculates total fare, potentially including discounts (if implemented).
*   **E-Ticket Generation:**
    *   Generate and view E-tickets using a unique Ticket ID. The ticket displays passenger and flight details.
*   **Admin Management:**
    *   Add new admin users who can perform all the above tasks.

## Technologies Used

*   **Java:** Core programming language.
*   **Swing:** For the graphical user interface (GUI).
*   **MySQL:** For the database to store customer, flight, ticket, and admin information.
*   **JDBC (Java Database Connectivity):** To interact with the MySQL database.
*   **JDateChooser (from JCalendar library):** For easy date selection in forms.

## Prerequisites

Before running the project, ensure you have the following installed on your system:

1.  **Java Development Kit (JDK):** Version 8 or higher.
2.  **MySQL Server:** Version 5.7 or higher (or compatible MariaDB).
3.  **MySQL JDBC Driver:** The `mysql-connector-j-X.X.X.jar` file. This needs to be included in your project's classpath.
4.  **JCalendar Library:** The `jcalendar-X.X.jar` file (which includes JDateChooser). This also needs to be in the classpath.
5.  **An IDE (Integrated Development Environment):**
    *   NetBeans (recommended, as the project seems to be structured for it with `.form` files).
    *   IntelliJ IDEA or Eclipse (can also be used, but you might need to configure the project manually).

## Database Setup

1.  **Create a Database:**
    *   Open your MySQL command-line client or a GUI tool like phpMyAdmin or MySQL Workbench.
    *   Create a new database. For this project, the default name used in the code is `airline-database`.
        ```sql
        CREATE DATABASE airline_database;
        ```
    *   Switch to the newly created database:
        ```sql
        USE airline_database;
        ```

2.  **Create Tables:**
    Execute the following SQL scripts to create the necessary tables.

    *   **`admin` table:**
        ```sql
        CREATE TABLE admin (
            adminID VARCHAR(10) PRIMARY KEY,
            firstName VARCHAR(50) NOT NULL,
            lastName VARCHAR(50),
            username VARCHAR(50) UNIQUE NOT NULL,
            password VARCHAR(255) NOT NULL -- Store hashed passwords in a real application
        );
        ```

    *   **`customer` table:**
        ```sql
        CREATE TABLE customer (
            customerID VARCHAR(10) PRIMARY KEY,
            firstName VARCHAR(50) NOT NULL,
            lastName VARCHAR(50),
            passport VARCHAR(20) UNIQUE,
            nationalID VARCHAR(20) UNIQUE,
            Address TEXT,
            contact VARCHAR(15),
            Gender VARCHAR(10),
            DOB DATE
        );
        ```

    *   **`flight` table:**
        ```sql
        CREATE TABLE flight (
            flightID VARCHAR(10) PRIMARY KEY,
            flightName VARCHAR(100) NOT NULL,
            arrival VARCHAR(50) NOT NULL,
            departure VARCHAR(50) NOT NULL,
            duration VARCHAR(20),
            seats INT,
            Fare DOUBLE,
            DateOfFlight DATE
        );
        ```

    *   **`ticket` table:**
        ```sql
        CREATE TABLE ticket (
            ticketID VARCHAR(10) PRIMARY KEY,
            flightID VARCHAR(10),
            customerID VARCHAR(10),
            arrival VARCHAR(50),
            departure VARCHAR(50),
            firstName VARCHAR(50),
            lastName VARCHAR(50),
            Gender VARCHAR(10),
            contact VARCHAR(15), -- Added this based on GetTicket form
            -- Add other fields as needed, e.g., bookingDate, seatNumber, totalFare
            FOREIGN KEY (flightID) REFERENCES flight(flightID) ON DELETE SET NULL,
            FOREIGN KEY (customerID) REFERENCES customer(customerID) ON DELETE CASCADE
        );
        ```
    *   **Note on `ticket` table:** The `GetTicket.java` form retrieves `contact` and `FligthID` (potential typo, should be `flightID`). Ensure your `ticket` table schema matches what the application expects or update the application code. The above schema includes `contact` and uses `flightID`.

3.  **Database Connection Configuration:**
    *   The application connects to MySQL using the following details (found in `getConnection()` methods of various classes):
        *   URL: `jdbc:mysql://127.0.0.1/airline-database`
        *   Username: `your mysql database username for example root`
        *   Password: `your mysql database password`
    *   **IMPORTANT:** If your MySQL setup uses a different username, password, port, or database name, you **must** update these connection details in all relevant Java files (`AddAdmin.java`, `AddCustomer.java`, `AddFlight.java`, `BookTicket.java`, `GetTicket.java`, `LoginFrame.java`, `SearchCustomer.java`).

4.  **Add a Default Admin (Optional but Recommended for First Run):**
    To log in for the first time, you can manually insert an admin record or use the "Add Admin" feature *after* logging in (which creates a chicken-and-egg problem if no admin exists).
    It's easier to add one directly to the database initially:
    ```sql
    INSERT INTO admin (adminID, firstName, lastName, username, password)
    VALUES ('AD001', 'Default', 'Admin', 'admin', 'password'); -- Change 'password' to something secure
    ```
    **Security Note:** In a real application, passwords should always be hashed before storing. This example uses plain text for simplicity.

## How to Run the Project

### Using NetBeans IDE (Recommended)

1.  **Clone or Download the Project:**
    Get the project files onto your local machine.
2.  **Open Project in NetBeans:**
    *   Launch NetBeans IDE.
    *   Go to `File > Open Project...`.
    *   Navigate to the project's root directory and click `Open Project`.
3.  **Add Libraries (JDBC Driver & JCalendar):**
    *   In the "Projects" window, right-click on the "Libraries" folder under your project.
    *   Select "Add JAR/Folder...".
    *   Browse to and select the `mysql-connector-j-X.X.X.jar` file.
    *   Repeat the process to add the `jcalendar-X.X.jar` file.
4.  **Verify Database Connection:**
    Ensure your MySQL server is running and the connection details in the Java files are correct (as mentioned in Database Setup).
5.  **Set Main Class:**
    *   Right-click on the project in the "Projects" window.
    *   Go to `Properties`.
    *   In the Project Properties dialog, select the `Run` category.
    *   For `Main Class`, click `Browse...` and select `com.mycompany.airline_project.LoginFrame` (or `com.mycompany.airline_project.Main` if you want to bypass login for testing, but `LoginFrame` is the intended entry point).
    *   Click `OK`.
6.  **Run the Project:**
    *   Click the "Run Project" button (green play icon) on the toolbar, or press `F6`.
    *   The `LoginFrame` should appear. Use the credentials for an admin user (e.g., username: `admin`, password: `password` if you used the default insert).

### Using Other IDEs (IntelliJ IDEA, Eclipse)

1.  **Clone or Download the Project.**
2.  **Import Project:**
    *   **IntelliJ IDEA:** `File > New > Project from Existing Sources...` and select the project's root folder.
    *   **Eclipse:** `File > Import... > General > Existing Projects into Workspace` and select the project's root folder.
3.  **Add Libraries to Classpath:**
    *   **IntelliJ IDEA:** `File > Project Structure... > Libraries > + > Java`. Add `mysql-connector-j-X.X.X.jar` and `jcalendar-X.X.jar`.
    *   **Eclipse:** Right-click project > `Build Path > Configure Build Path... > Libraries` tab > `Add External JARs...`. Add both JAR files.
4.  **Verify Database Connection:**
    Same as with NetBeans.
5.  **Configure Run Configuration:**
    *   Create a new run configuration.
    *   Set the main class to `com.mycompany.airline_project.LoginFrame`.
6.  **Run the Project.**

### Running from Command Line (More Advanced)

1.  **Compile the Code:**
    Navigate to the project's `src` directory (or where your `.java` files are).
    ```bash
    javac -cp "path/to/mysql-connector.jar;path/to/jcalendar.jar;." com/mycompany/airline_project/*.java
    # On Linux/macOS, use ':' instead of ';' as classpath separator
    # javac -cp "path/to/mysql-connector.jar:path/to/jcalendar.jar:." com/mycompany/airline_project/*.java
    ```
    This assumes your compiled `.class` files will go into a structure matching the package.
2.  **Run the Application:**
    Navigate to the directory *above* `com` (i.e., the project's effective root for compiled classes).
    ```bash
    java -cp "path/to/mysql-connector.jar;path/to/jcalendar.jar;." com.mycompany.airline_project.LoginFrame
    # On Linux/macOS
    # java -cp "path/to/mysql-connector.jar:path/to/jcalendar.jar:." com.mycompany.airline_project.LoginFrame
    ```

## Usage Flow

1.  **Login:** Start the application. The `LoginFrame` will appear. Enter admin credentials.
2.  **Main Window:** Upon successful login, the `Main` window (with a JDesktopPane) will open.
3.  **Navigate:** Use the menu bar at the top to access different functionalities:
    *   **Customer Menu:** Add or Search for customers.
    *   **Flight Menu:** Add new flights or Book tickets.
    *   **Ticket Menu:** View existing E-tickets.
    *   **Admin Menu:** Add new administrators.
4.  **Perform Actions:** Each menu item will open an internal frame within the main desktop pane where you can perform the respective operations.

## Troubleshooting

*   **`ClassNotFoundException: com.mysql.cj.jdbc.Driver`**: The MySQL JDBC driver JAR is not in the classpath or is the wrong version.
*   **`java.sql.SQLException: Access denied for user...`**: Incorrect MySQL username/password in the Java code, or the user doesn't have privileges for the `airline-database`.
*   **`java.sql.SQLException: No suitable driver found...`**: JDBC URL might be incorrect, or the MySQL driver is not loaded.
*   **`NoClassDefFoundError: com/toedter/calendar/JDateChooser`**: The JCalendar library JAR is missing from the classpath.
*   **GUI forms (.form files) not rendering correctly in IntelliJ/Eclipse:** These IDEs might not have native support for NetBeans' GUI builder format. You might need to manually review/edit the generated `initComponents()` code or try to find plugins that can handle NetBeans forms. For viewing, the Java code itself is what matters.

## Future Enhancements (Suggestions)

*   Implement password hashing for admin credentials.
*   Add functionality to search/edit/delete flights.
*   Add functionality to edit/delete customer records.
*   Implement seat selection during booking.
*   Add a reports section (e.g., daily bookings, flight occupancy).
*   More robust input validation for all fields.
*   User roles (e.g., Admin vs. Booking Agent).
*   Refine UI for a more modern look and feel.

---

This README provides a comprehensive guide to setting up and running your Airline Ticket Booking System.
