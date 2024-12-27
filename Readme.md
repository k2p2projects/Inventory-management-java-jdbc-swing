# Inventory Management System

- A Java-based desktop application for managing inventory, built using Swing and JDBC. This project allows users to add, update, and view inventory items with an easy-to-use graphical interface.

- This project was developed by @aloks1490 and @  .

- check out [Alok's GitHub profile](https://github.com/aloks1490).
- check out [Kanchan's GitHub profile](https://github.com/aloks1490).

## Features

- **Add Items**: Add new items to the inventory with a name, quantity, and cost.
- **Update Items**: Update the quantity of existing items via a dropdown menu of current inventory items.
- **View Inventory**: Display all inventory items in a table format with columns for ID, name, quantity, and cost.
- **Clear All Items**: Remove all items from the inventory with a single click (with confirmation).

## Prerequisites

1. **Java Development Kit (JDK)**: Ensure you have JDK 8 or higher installed.
2. **MySQL Database**: Install and configure MySQL on your system.
3. **JDBC Driver**: Ensure the MySQL JDBC driver (`mysql-connector-java`) is added to your classpath.


## Database Setup

1. Create a new database named `inventory_management`:
    ```sql
    CREATE DATABASE inventory_management;
    ```

2. Use the following table schema for the `inventory` table:
    ```sql
    CREATE TABLE inventory (
        id INT AUTO_INCREMENT PRIMARY KEY,
        item_name VARCHAR(100) NOT NULL,
        item_quantity INT NOT NULL,
        item_cost DOUBLE NOT NULL
    );
    ```

3. Configure your MySQL username and password in the source code:
    ```java
    connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/inventory_management?", "root", "root");
    ```

---

## How to Run

1. Clone the repository or download the source code.
2. Compile and run the program using a Java IDE like IntelliJ IDEA or execute the following commands in the terminal:
    ```bash
    javac InventoryManagement.java
    java InventoryManagement
    ```
3. The application GUI will launch.

---

## Usage

### Add Items
- Go to the **Add Items** tab.
- Enter the item name, quantity, and cost.
- Click the "Add" button to save the item to the database.

### Update Items
- Go to the **Update Items** tab.
- Select an item from the dropdown.
- Enter the new quantity in the "Quantity" field.
- Click the "Update" button to save changes.

### View Inventory
- Go to the **Inventory Table** tab.
- View all items in the inventory table.
- Click "Clear All Items" to delete all inventory data (requires confirmation).

---
#### Team members
- github.com/aloks1490
- github.com/

