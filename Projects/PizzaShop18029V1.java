import java.util.*;

public class PizzaShop18029V1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Pizza Shop!");
        System.out.println("1. Login as Owner");
        System.out.println("2. Login as Customer");
        System.out.print("Choose your role (1/2): ");

        int roleChoice = scanner.nextInt();
        scanner.nextLine();

        if (roleChoice == 1) {
            Owner owner = new Owner();
            if (owner.login()) {
                owner.ownerMenu();
            } else {
                System.out.println("Invalid login. Exiting...");
            }
        } else if (roleChoice == 2) {
            Customer customer = new Customer();
            customer.customerMenu();
        } else {
            System.out.println("Invalid choice. Exiting...");
        }
        scanner.close();
    }
}

class Owner {
    String Owner_UserName = "owner";
    String Owner_Pass = "Pass123";

    Scanner scan = new Scanner(System.in);

    Menu menu = new Menu();
    Order order = new Order();

    public boolean login() {
        System.out.print("Enter username: ");
        String username = scan.nextLine();
        System.out.print("Enter password: ");
        String password = scan.nextLine();
        return Owner_UserName.equals(username) && Owner_Pass.equals(password);
    }

    public void ownerMenu() {
        int choice;
        do {
            System.out.println("\n=== Owner Menu ===");
            System.out.println("1. Manage Menu");
            System.out.println("2. View Orders");
            System.out.println("3. Logout");
            System.out.print("Enter your choice: ");
            choice = scan.nextInt();
            scan.nextLine();

            switch (choice) {
                case 1 -> manageMenu();
                case 2 -> viewOrders();
                case 3 -> System.out.println("Logging out...");
                default -> System.out.println("Invalid choice, please try again.");
            }

        } while (choice != 3);
    }

    public void manageMenu() {
        int choice;
        do {
            System.out.println("\n=== Manage Menu ===");
            System.out.println("1. Add Pizza");
            System.out.println("2. Remove Pizza");
            System.out.println("3. View Menu");
            System.out.println("4. Back to Main Menu");
            System.out.print("Enter your choice: ");
            choice = scan.nextInt();
            scan.nextLine();

            switch (choice) {
                case 1 -> menu.addPizza();
                case 2 -> menu.removePizza();
                case 3 -> menu.viewMenu();
                case 4 -> System.out.println("Returning to Owner Menu...");
                default -> System.out.println("Invalid choice, please try again.");
            }
        } while (choice != 4);
    }

    public void viewOrders() {
        order.viewOrders();
    }
}

class Customer {
    private Scanner scanner = new Scanner(System.in);
    Menu menu = new Menu();
    Order order = new Order();

    public void customerMenu() {
        int choice;
        do {
            System.out.println("\n=== Customer Menu ===");
            System.out.println("1. View Menu");
            System.out.println("2. Place Order");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> menu.viewMenu();
                case 2 -> order.placeOrder();
                case 3 -> System.out.println("Exiting...");
                default -> System.out.println("Invalid choice, please try again.");
            }
        } while (choice != 3);
    }
}

class Menu {
    private String[] pizzas = new String[50];
    private double[] prices = new double[50];
    private int count = 0;

    Scanner scanner = new Scanner(System.in);

    public Menu() {
        addDefaultPizzas();
    }

    private void addDefaultPizzas() {
        addPizzaToMenu("Margherita", 5.99);
        addPizzaToMenu("Pepperoni", 7.99);
        addPizzaToMenu("BBQ Chicken", 8.99);
        addPizzaToMenu("Veggie", 6.99);
        addPizzaToMenu("Cheese Burst", 8.99);
        addPizzaToMenu("Veg Paneer", 5.99);
    }

    private void addPizzaToMenu(String pizzaName, double pizzaPrice) {
        if (count >= pizzas.length) {
            System.out.println("Menu is full. Cannot add more pizzas.");
            return;
        }

        pizzas[count] = pizzaName;
        prices[count] = pizzaPrice;
        count++;
    }

    public void addPizza() {
        if (count >= pizzas.length) {
            System.out.println("Menu is full. Cannot add more pizzas.");
            return;
        }

        System.out.print("Enter pizza name: ");
        String pizzaName = scanner.nextLine();
        System.out.print("Enter pizza price: ");
        double pizzaPrice = scanner.nextDouble();
        scanner.nextLine();

        pizzas[count] = pizzaName;
        prices[count] = pizzaPrice;
        count++;

        System.out.println("Pizza added successfully.");
    }

    public void removePizza() {
        System.out.print("Enter pizza name to remove: ");
        String pizzaName = scanner.nextLine();

        boolean found = false;
        for (int i = 0; i < count; i++) {
            if (pizzas[i].equalsIgnoreCase(pizzaName)) {
                for (int j = i; j < count - 1; j++) {
                    pizzas[j] = pizzas[j + 1];
                    prices[j] = prices[j + 1];
                }
                pizzas[count - 1] = null;
                prices[count - 1] = 0.0;
                count--;
                found = true;
                System.out.println("Pizza removed successfully.");
                break;
            }
        }

        if (!found) {
            System.out.println("Pizza not found in the menu.");
        }
    }

    public void viewMenu() {
        if (count == 0) {
            System.out.println("No pizzas available in the menu.");
        } else {
            System.out.println("\n=== Pizza Menu ===");
            for (int i = 0; i < count; i++) {
                System.out.println((i + 1) + ". " + pizzas[i] + " - $" + prices[i]);
            }
        }
    }

    // Getter methods for Order class
    public int getCount() {
        return count;
    }

    public String[] getPizzas() {
        return pizzas;
    }

    public double[] getPrices() {
        return prices;
    }
}


class Order {
    private String[] customerNames = new String[10];
    private String[][] orderedPizzas = new String[10][5];
    private double[] totalCosts = new double[10];
    private int orderCount = 0;

    Menu menu = new Menu();
    Scanner scanner = new Scanner(System.in);

    public void placeOrder() {
        if (orderCount >= customerNames.length) {
            System.out.println("Maximum order limit reached. Cannot place more orders.");
            return;
        }

        System.out.print("Enter your name: ");
        customerNames[orderCount] = scanner.nextLine();

        System.out.println("Available pizzas:");
        menu.viewMenu();

        double totalCost = 0.0;
        int pizzaIndex = 0;
        while (true) {
            System.out.print("Enter the pizza name to order (or type 'done' to finish): ");
            String pizzaName = scanner.nextLine();

            if (pizzaName.equalsIgnoreCase("done")) {
                break;
            }

            boolean found = false;
            for (int i = 0; i < menu.getCount(); i++) {
                if (menu.getPizzas()[i] != null && menu.getPizzas()[i].equalsIgnoreCase(pizzaName)) {
                    orderedPizzas[orderCount][pizzaIndex++] = pizzaName;
                    totalCost += menu.getPrices()[i];
                    found = true;
                    break;
                }
            }

            if (!found) {
                System.out.println("Pizza not found in the menu. Please try again.");
            }
        }

        totalCosts[orderCount] = totalCost;
        orderCount++;
        System.out.println("Order placed successfully! Total cost: $" + totalCost);
    }

    public void viewOrders() {
        if (orderCount == 0) {
            System.out.println("No orders placed yet.");
            return;
        }

        for (int i = 0; i < orderCount; i++) {
            System.out.println("\nOrder #" + (i + 1) + " by " + customerNames[i]);
            System.out.print("Pizzas: ");
            for (String pizza : orderedPizzas[i]) {
                if (pizza != null) {
                    System.out.print(pizza + ", ");
                }
            }
            System.out.println("\nTotal Cost: $" + totalCosts[i]);
            System.out.println("-------------------------");
        }
    }
}
