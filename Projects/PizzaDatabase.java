import java.sql.*;
import java.util.*;

// Utility class for database connection
class DBHelper {
    private static Connection connection;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pizza_shop", "root", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        return connection;
    }
}

class Menu {
    private Scanner scanner;

    public Menu() {
        scanner = new Scanner(System.in);
    }

    public void viewPizzas() {
        try (Statement stmt = DBHelper.getConnection().createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM pizza");
            System.out.println("\nAvailable Pizzas:");
            while (rs.next()) {
                System.out.println(rs.getInt("id") + ". " + rs.getString("name") + " - $" + rs.getDouble("price"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addPizzaToDatabase() {
        try {
            System.out.print("Enter the Pizza ID: ");
            int id = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            System.out.print("Enter Pizza Name: ");
            String pizzaName = scanner.nextLine();

            System.out.print("Enter Pizza Price: ");
            double pizzaPrice = scanner.nextDouble();

            String query = "INSERT INTO pizza (id, name, price) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = DBHelper.getConnection().prepareStatement(query)) {
                stmt.setInt(1, id);
                stmt.setString(2, pizzaName);
                stmt.setDouble(3, pizzaPrice);
                stmt.executeUpdate();
                System.out.println("Pizza added successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removePizzaFromDatabase() {
        try {
            System.out.print("Enter pizza ID to remove: ");
            int pizzaId = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            String query = "DELETE FROM pizza WHERE id = ?";
            try (PreparedStatement stmt = DBHelper.getConnection().prepareStatement(query)) {
                stmt.setInt(1, pizzaId);
                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Pizza removed successfully.");
                } else {
                    System.out.println("Pizza ID not found.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

class Order {
    private Scanner scanner;

    public Order() {
        scanner = new Scanner(System.in);
    }

    public void placeOrder() {
        try {
            System.out.print("Enter your name: ");
            String customerName = scanner.nextLine();
            List<String> orderedPizzas = new ArrayList<>();
            double totalCost = 0;

            while (true) {
                System.out.print("Enter pizza name to order (or 'done' to finish): ");
                String pizzaName = scanner.nextLine();
                if (pizzaName.equalsIgnoreCase("done")) break;

                String query = "SELECT * FROM pizza WHERE name = ?";
                try (PreparedStatement stmt = DBHelper.getConnection().prepareStatement(query)) {
                    stmt.setString(1, pizzaName);
                    ResultSet rs = stmt.executeQuery();

                    if (rs.next()) {
                        orderedPizzas.add(pizzaName);
                        totalCost += rs.getDouble("price");
                    } else {
                        System.out.println("Pizza not found in the menu.");
                    }
                }
            }

            if (!orderedPizzas.isEmpty()) {
                String query = "INSERT INTO orders (customer_name, pizzas, total_cost) VALUES (?, ?, ?)";
                try (PreparedStatement stmt = DBHelper.getConnection().prepareStatement(query)) {
                    stmt.setString(1, customerName);
                    stmt.setString(2, String.join(", ", orderedPizzas));
                    stmt.setDouble(3, totalCost);
                    stmt.executeUpdate();
                    System.out.println("Order placed successfully. Total cost: $" + totalCost);
                }
            } else {
                System.out.println("No pizzas were ordered.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewOrders() {
        try (Statement stmt = DBHelper.getConnection().createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM orders");
            System.out.println("\nOrders:");
            while (rs.next()) {
                System.out.println(rs.getInt("id") + ". " + rs.getString("customer_name") + " - " + rs.getString("pizzas") + " - $" + rs.getDouble("total_cost"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeOrderFromDatabase() {
        try {
            System.out.print("Enter order ID to remove: ");
            int orderId = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            String query = "DELETE FROM orders WHERE id = ?";
            try (PreparedStatement stmt = DBHelper.getConnection().prepareStatement(query)) {
                stmt.setInt(1, orderId);
                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Order removed successfully.");
                } else {
                    System.out.println("Order ID not found.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

public class PizzaDatabase {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Menu menu = new Menu();
        Order order = new Order();

        while (true) {
            System.out.println("\n1. View Menu");
            System.out.println("2. Add Pizza to Menu");
            System.out.println("3. Remove Pizza from Menu");
            System.out.println("4. Place Order");
            System.out.println("5. View Orders");
            System.out.println("6. Remove Order");
            System.out.println("7. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    menu.viewPizzas();
                    break;
                case 2:
                    menu.addPizzaToDatabase();
                    break;
                case 3:
                    menu.removePizzaFromDatabase();
                    break;
                case 4:
                    order.placeOrder();
                    break;
                case 5:
                    order.viewOrders();
                    break;
                case 6:
                    order.removeOrderFromDatabase();
                    break;
                case 7:
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
