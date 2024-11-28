import java.util.*;

class Menu {
    private Vector<String> pizzas = new Vector<>();
    private Vector<Double> prices = new Vector<>();

    Scanner scanner = new Scanner(System.in);

    public Menu() {
        addDefaultPizzas();
    }

    public int getCount() {
        return pizzas.size();
    }

    public Vector<String> getPizzas() {
        return pizzas;
    }

    public Vector<Double> getPrices() {
        return prices;
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
        pizzas.add(pizzaName);
        prices.add(pizzaPrice);
    }

    public void addPizza() {
        System.out.print("Enter pizza name: ");
        String pizzaName = scanner.nextLine();
        System.out.print("Enter pizza price: ");
        double pizzaPrice = scanner.nextDouble();
        scanner.nextLine();

        pizzas.add(pizzaName);
        prices.add(pizzaPrice);

        System.out.println("Pizza added successfully.");
    }

    public void removePizza() {
        System.out.print("Enter pizza name to remove: ");
        String pizzaName = scanner.nextLine();

        int index = pizzas.indexOf(pizzaName);
        if (index != -1) {
            pizzas.remove(index);
            prices.remove(index);
            System.out.println("Pizza removed successfully.");
        } else {
            System.out.println("Pizza not found in the menu.");
        }
    }

    public void viewMenu() {
        if (pizzas.isEmpty()) {
            System.out.println("No pizzas available in the menu.");
        } else {
            System.out.println("\n=== Pizza Menu ===");
            for (int i = 0; i < pizzas.size(); i++) {
                System.out.println((i + 1) + ". " + pizzas.get(i) + " - $" + prices.get(i));
            }
        }
    }
}

class Order {
    private Vector<String> customerNames = new Vector<>();
    private Vector<Vector<String>> orderedPizzas = new Vector<>();
    private Vector<Double> totalCosts = new Vector<>();

    Menu menu = new Menu();
    Scanner scanner = new Scanner(System.in);

    public void placeOrder() {
        System.out.print("Enter your name: ");
        customerNames.add(scanner.nextLine());
        
        Vector<String> order = new Vector<>();
        double totalCost = 0.0;

        System.out.println("Available pizzas:");
        menu.viewMenu();

        while (true) {
            System.out.print("Enter the pizza name to order (or type 'done' to finish): ");
            String pizzaName = scanner.nextLine();

            if (pizzaName.equalsIgnoreCase("done")) {
                break;
            }

            int index = menu.getPizzas().indexOf(pizzaName);
            if (index != -1) {
                order.add(pizzaName);
                totalCost += menu.getPrices().get(index);
            } else {
                System.out.println("Pizza not found in the menu. Please try again.");
            }
        }

        orderedPizzas.add(order);
        totalCosts.add(totalCost);
        System.out.println("Order placed successfully! Total cost: $" + totalCost);
    }

    public void viewOrders() {
        if (customerNames.isEmpty()) {
            System.out.println("No orders placed yet.");
            return;
        }

        for (int i = 0; i < customerNames.size(); i++) {
            System.out.println("\nOrder #" + (i + 1) + " by " + customerNames.get(i));
            System.out.print("Pizzas: ");
            for (String pizza : orderedPizzas.get(i)) {
                System.out.print(pizza + ", ");
            }
            System.out.println("\nTotal Cost: $" + totalCosts.get(i));
            System.out.println("-------------------------");
        }
    }
}

class PizzaShop18029V2 {
    private static Scanner scanner = new Scanner(System.in);
    private static Menu menu = new Menu();
    private static Order order = new Order();

    public static void main(String[] args) {
        System.out.println("Welcome to the Pizza Shop!");
        
        while (true) {
            System.out.println("\n1. Login as Owner");
            System.out.println("2. Login as Customer");
            System.out.println("3. Exit");
            System.out.print("Choose your role (1/2/3): ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            switch (choice) {
                case 1:
                    ownerMenu();
                    break;
                case 2:
                    customerMenu();
                    break;
                case 3:
                    System.out.println("Thank you for visiting the Pizza Shop!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void ownerMenu() {
        while (true) {
            System.out.println("\n=== Owner Menu ===");
            System.out.println("1. Add Pizza");
            System.out.println("2. Remove Pizza");
            System.out.println("3. View Menu");
            System.out.println("4. View Orders");
            System.out.println("5. Back to Main Menu");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    menu.addPizza();
                    break;
                case 2:
                    menu.removePizza();
                    break;
                case 3:
                    menu.viewMenu();
                    break;
                case 4:
                    order.viewOrders();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void customerMenu() {
        while (true) {
            System.out.println("\n=== Customer Menu ===");
            System.out.println("1. Place Order");
            System.out.println("2. View Menu");
            System.out.println("3. Back to Main Menu");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    order.placeOrder();
                    break;
                case 2:
                    menu.viewMenu();
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}