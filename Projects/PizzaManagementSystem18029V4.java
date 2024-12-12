import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
// import java.util.*;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class PizzaManagementSystem18029V4 {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginPage::new); 
    }
}


class LoginPage extends JFrame {
    final private Font mainFont = new Font("Segoe Print", Font.BOLD, 18);
    JTextField tfUser ;
    JPasswordField pfPassword;

    public LoginPage() {
        initialize(); // Initialize the UI components
    }

    public void initialize() {
        // Title
        JLabel lbLoginForm = new JLabel("Pizza Shop", SwingConstants.CENTER);
        lbLoginForm.setFont(mainFont);

        // Username Label and Field
        JLabel lbUser  = new JLabel("Username");
        lbUser .setFont(mainFont);

        tfUser  = new JTextField();
        tfUser .setFont(mainFont);
        tfUser .setBorder(BorderFactory.createCompoundBorder(
                tfUser .getBorder(), BorderFactory.createEmptyBorder(5, 10, 5, 10))); // Add padding

        // Password Label and Field
        JLabel lbPassword = new JLabel("Password");
        lbPassword.setFont(mainFont);

        pfPassword = new JPasswordField();
        pfPassword.setFont(mainFont);
        pfPassword.setBorder(BorderFactory.createCompoundBorder(
                pfPassword.getBorder(), BorderFactory.createEmptyBorder(5, 10, 5, 10))); // Add padding

        // Form Panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(0, 1, 10, 10));
        formPanel.add(lbLoginForm);
        formPanel.add(lbUser );
        formPanel.add(tfUser );
        formPanel.add(lbPassword);
        formPanel.add(pfPassword);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Margin

        // Buttons
        JButton btnLogin = new JButton("Login");
        btnLogin.setFont(mainFont);
        btnLogin.setBackground(new Color(0, 102, 204));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Padding

        JButton btnCancel = new JButton("Cancel");
        btnCancel.setFont(mainFont);
        btnCancel.setBackground(new Color(204, 0, 0));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFocusPainted(false);
        btnCancel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Padding

        // Button Actions
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = tfUser .getText();
                String password = String.valueOf(pfPassword.getPassword());

                if ("Owner".equals(username) && "PizzaShop".equals(password)) {
                    new MainFrame(); // Open the main frame
                    dispose(); // Close the login frame
                } else {
                    JOptionPane.showMessageDialog(LoginPage.this, "Invalid Username or Password", "Try Again", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the application
            }
        });

        // Button Panel
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(1, 2, 10, 0));
        buttonsPanel.add(btnLogin);
        buttonsPanel.add(btnCancel);
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20)); // Margin

        // Add panels to frame
        add(formPanel, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.SOUTH);

        setTitle("Login Page");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(400, 500);
        setMinimumSize(new Dimension(350, 450));
        setLocationRelativeTo(null);
        setVisible(true);
    }
}

class DBHelper 
{
    private static final String DB_URL = "jdbc:mysql://sql12.freesqldatabase.com:3306/sql12751584";
    private static final String DB_USER = "sql12751584";
    private static final String DB_PASSWORD = "zJRZJWJ2Xb";

    public static Connection getConnection() throws SQLException
    {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}

class MainFrame extends JFrame 
{
    private JPanel contentPanel;

    MainFrame() 
    {
        setTitle("Pizza Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLayout(new BorderLayout());

        // Sidebar
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new GridLayout(3, 1));
        sidebar.setPreferredSize(new Dimension(200, 600));

        JButton menuButton = new JButton("Menu");
        menuButton.setBackground(Color.RED);
        menuButton.setForeground(Color.WHITE);
        menuButton.addActionListener(e -> showMenuPage());
        sidebar.add(menuButton);

        JButton editPizzaButton = new JButton("Edit Pizza");
        editPizzaButton.setBackground(Color.RED);
        editPizzaButton.setForeground(Color.WHITE);
        editPizzaButton.addActionListener(e -> showEditPizzaPage());
        sidebar.add(editPizzaButton);

        JButton orderButton = new JButton("Order");
        orderButton.setBackground(Color.RED);
        orderButton.setForeground(Color.WHITE);
        orderButton.addActionListener(e -> showOrderPage());
        sidebar.add(orderButton);

        add(sidebar, BorderLayout.WEST);

        // Content Panel
        contentPanel = new JPanel();
        contentPanel.setLayout(new CardLayout());
        add(contentPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private void showMenuPage() 
    {
        contentPanel.removeAll();
        JPanel menuPanel = new JPanel(new BorderLayout());

        JTable menuTable = new JTable();
        DefaultTableModel menuTableModel = new DefaultTableModel(new String[]{"Pizza ID", "Pizza Name", "Pizza Cost"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make cells non-editable
            }
        };
        menuTable.setModel(menuTableModel);
        menuTable.getColumnModel().getColumn(0).setPreferredWidth(80); 
        menuTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        menuTable.getColumnModel().getColumn(2).setPreferredWidth(150);

        menuTable.setRowHeight(30);
        menuTable.setFont(new Font("Arial", Font.PLAIN, 14)); 


        JScrollPane scrollPane = new JScrollPane(menuTable);
        menuPanel.add(scrollPane, BorderLayout.CENTER);

        // Fetch data from database
        try (Connection connection = DBHelper.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM pizza")) 
             {

            while (rs.next()) 
            {
                menuTableModel.addRow(new Object[]{rs.getInt("pizza_id"), rs.getString("pizza_name"), rs.getString("pizza_price")});
            }
        } catch (SQLException e) 
        {
            JOptionPane.showMessageDialog(this, "Error fetching menu data", "Database Error", JOptionPane.ERROR_MESSAGE);
        }

        contentPanel.add(menuPanel);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showEditPizzaPage() {
        contentPanel.removeAll();
        JPanel editPizzaPanel = new JPanel(null);
    
        JLabel idLabel = new JLabel("Pizza ID:");
        idLabel.setBounds(50, 50, 100, 30);
        editPizzaPanel.add(idLabel);
    
        JTextField idField = new JTextField();
        idField.setBounds(150, 50, 200, 30);
        editPizzaPanel.add(idField);
    
        JLabel nameLabel = new JLabel("Pizza Name:");
        nameLabel.setBounds(50, 100, 100, 30);
        editPizzaPanel.add(nameLabel);
    
        JTextField nameField = new JTextField();
        nameField.setBounds(150, 100, 200, 30);
        editPizzaPanel.add(nameField);
    
        JLabel costLabel = new JLabel("Pizza Cost:");
        costLabel.setBounds(50, 150, 100, 30);
        editPizzaPanel.add(costLabel);
    
        JTextField costField = new JTextField();
        costField.setBounds(150, 150, 200, 30);
        editPizzaPanel.add(costField);
    
        JButton addButton = new JButton("Add Pizza");
        addButton.setBackground(Color.RED);
        addButton.setForeground(Color.WHITE);
        addButton.setBounds(50, 200, 120, 30);
    
        addButton.addActionListener(e -> {
            String id = idField.getText();
            String name = nameField.getText();
            String cost = costField.getText();
    
            try {
                // Validate Pizza ID
                int pizzaId = Integer.parseInt(id.trim());
                if (pizzaId <= 0) {
                    throw new IllegalArgumentException("Pizza ID must be a positive number.");
                }
    
                // Validate Pizza Name
                if (name == null || name.trim().isEmpty()) {
                    throw new IllegalArgumentException("Pizza name cannot be empty.");
                }
                if (!name.matches("^[a-zA-Z ]+$")) {
                    throw new IllegalArgumentException("Pizza name must contain only letters");
                }
                
                if (name.matches(".*(.)\\1{2,}.*")) {
                    throw new IllegalArgumentException("Pizza name cannot contain more than 3 consecutive identical letters.");
                }
    
                // Validate Pizza Cost
                int pizzaCost = Integer.parseInt(cost.trim());
                if (pizzaCost <= 0) {
                    throw new IllegalArgumentException("Pizza cost must be a positive number.");
                }
    
                // Database Insertion
                try (Connection connection = DBHelper.getConnection();
                     PreparedStatement stmt = connection.prepareStatement("INSERT INTO pizza (pizza_id, pizza_name, pizza_price) VALUES (?, ?, ?)")) {
                    stmt.setInt(1, pizzaId);
                    stmt.setString(2, name.trim());
                    stmt.setInt(3, pizzaCost);
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(editPizzaPanel, "Pizza added successfully!");

                    idField.setText("");
                    nameField.setText("");
                    costField.setText("");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(editPizzaPanel, "Error adding pizza: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                }
    
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(editPizzaPanel, "Pizza ID and Cost must be valid numbers.", "Input Error", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(editPizzaPanel, ex.getMessage(), "Input Validation Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    
        editPizzaPanel.add(addButton);
        
    
    

        JButton removeButton = new JButton("Remove Pizza");
        removeButton.setBackground(Color.RED);
        removeButton.setForeground(Color.WHITE);
        removeButton.setBounds(200, 200, 150, 30);
        removeButton.addActionListener(e -> 
        {
            String id = idField.getText();

            try (Connection connection = DBHelper.getConnection();
                 PreparedStatement stmt = connection.prepareStatement("DELETE FROM pizza WHERE pizza_id = ?")) 
            {
                stmt.setInt(1, Integer.parseInt(id));
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Pizza removed successfully");
                idField.setText("");
                nameField.setText("");
                costField.setText("");
            } catch (SQLException ex) 
            {
                JOptionPane.showMessageDialog(this, "Error removing pizza", "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        editPizzaPanel.add(removeButton);

        JButton backButton = new JButton("Back");
        backButton.setBackground(Color.RED);
        backButton.setForeground(Color.WHITE);
        backButton.setBounds(200, 250, 120, 30);
        backButton.addActionListener(e -> showMenuPage());
        editPizzaPanel.add(backButton);

        contentPanel.add(editPizzaPanel);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showOrderPage() {
        contentPanel.removeAll();
        JPanel orderPanel = new JPanel(new BorderLayout());
    
        // Create table to display orders
        JTable orderTable = new JTable();
        DefaultTableModel orderTableModel = new DefaultTableModel(
                new String[]{"Customer Name", "Pizza Name", "Pizza Cost", "Quantity"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table cells non-editable
            }
        };
        orderTable.setModel(orderTableModel);
        orderTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        orderTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        orderTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        orderTable.setRowHeight(30);
        orderTable.setFont(new Font("Arial", Font.PLAIN, 14));
    
        JScrollPane scrollPane = new JScrollPane(orderTable);
        orderPanel.add(scrollPane, BorderLayout.CENTER);
    
        // Load existing orders from the database
        try (Connection connection = DBHelper.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM orders")) {
            while (rs.next()) {
                String customerName = rs.getString("customer_name");
                String pizzaName = rs.getString("pizza_name");
                double pizzaCost = rs.getDouble("pizza_cost");
                int quantity = rs.getInt("quantity");
                orderTableModel.addRow(new Object[]{customerName, pizzaName, pizzaCost, quantity});
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Print stack trace for debugging
            JOptionPane.showMessageDialog(this, "Error loading previous orders: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    
        // Create button panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
    
        // Place Order button
        JButton placeOrderButton = new JButton("Place Order");
        placeOrderButton.setBackground(Color.RED);
        placeOrderButton.setForeground(Color.WHITE);
        placeOrderButton.addActionListener(e -> {
            try {
                String customerName = JOptionPane.showInputDialog("Enter Customer Name:");
                if (customerName == null || customerName.trim().isEmpty()) {
                    throw new IllegalArgumentException("Customer Name is required.");
                }
                customerName = customerName.trim();
                if (!customerName.matches("[a-zA-Z]+")) {
                    throw new IllegalArgumentException("Customer Name must contain only letters.");
                }
        
                // Check for duplicate letters not exceeding 5 occurrences
                Map<Character, Long> charCountMap = customerName.chars()
                        .mapToObj(c -> (char) c)
                        .collect(Collectors.groupingBy(c -> c, Collectors.counting()));
                for (Map.Entry<Character, Long> entry : charCountMap.entrySet()) {
                    if (entry.getValue() > 5) {
                        throw new IllegalArgumentException("Customer Name must not have any letter repeated more than 5 times.");
                    }
                }
        
                // Pizza Name ComboBox
                JComboBox<String> pizzaNameComboBox = new JComboBox<>();
        double pizzaCost = 0.0;
        try (Connection connection = DBHelper.getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT pizza_name, pizza_price FROM pizza");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                pizzaNameComboBox.addItem(rs.getString("pizza_name"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error fetching pizza names: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int pizzaSelectionResult = JOptionPane.showConfirmDialog(this, pizzaNameComboBox, "Select Pizza Name", JOptionPane.OK_CANCEL_OPTION);
        if (pizzaSelectionResult != JOptionPane.OK_OPTION) {
            return;
        }
        String selectedPizzaName = (String) pizzaNameComboBox.getSelectedItem();

        // Fetch cost for the selected pizza
        try (Connection connection = DBHelper.getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT pizza_price FROM pizza WHERE pizza_name = ?")) {
            stmt.setString(1, selectedPizzaName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    pizzaCost = rs.getDouble("pizza_price");
                } else {
                    throw new SQLException("Pizza cost not found.");
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error fetching pizza cost: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Step 3: Select Quantity
        JComboBox<Integer> quantityComboBox = new JComboBox<>();
        for (int i = 1; i <= 10; i++) { // Allow quantity from 1 to 10
            quantityComboBox.addItem(i);
        }

        int quantitySelectionResult = JOptionPane.showConfirmDialog(this, quantityComboBox, "Select Quantity", JOptionPane.OK_CANCEL_OPTION);
        if (quantitySelectionResult != JOptionPane.OK_OPTION) {
            return;
        }
        int selectedQuantity = (int) quantityComboBox.getSelectedItem();
        double totalCost = pizzaCost * selectedQuantity;

        // Step 4: Place Order
        try (Connection connection = DBHelper.getConnection();
             PreparedStatement stmt = connection.prepareStatement("INSERT INTO orders (customer_name, pizza_name, pizza_cost, quantity) VALUES (?, ?, ?, ?)")) {
            stmt.setString(1, customerName);
            stmt.setString(2, selectedPizzaName);
            stmt.setDouble(3, totalCost);
            stmt.setInt(4, selectedQuantity);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Order placed successfully.\n" +
                    "Customer: " + customerName + "\n" +
                    "Pizza: " + selectedPizzaName + "\n" +
                    "Quantity: " + selectedQuantity + "\n" +
                    "Total Cost: " + totalCost);

            // Update table with the new order
            orderTableModel.addRow(new Object[]{customerName, selectedPizzaName, totalCost, selectedQuantity});
        }
    } catch (IllegalArgumentException ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Error placing order: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
    }
});
        
    
        // Remove Order button
        JButton removeOrderButton = new JButton("Remove Order");
        removeOrderButton.setBackground(Color.RED);
        removeOrderButton.setForeground(Color.WHITE);
        removeOrderButton.addActionListener(e -> {
            try {
                // Input for Customer Name
                String customerName = JOptionPane.showInputDialog("Enter Customer Name:");
                if (customerName == null || customerName.trim().isEmpty()) {
                    throw new IllegalArgumentException("Customer Name is required to remove an order.");
                }
                customerName = customerName.trim();
                if (!customerName.matches("[a-zA-Z]+")) {
                    throw new IllegalArgumentException("Customer Name must contain only letters.");
                }
    
                // Check for duplicate letters not exceeding 5 occurrences
                Map<Character, Long> charCountMap = customerName.chars()
                        .mapToObj(c -> (char) c)
                        .collect(Collectors.groupingBy(c -> c, Collectors.counting()));
                for (Map.Entry<Character, Long> entry : charCountMap.entrySet()) {
                    if (entry.getValue() > 5) {
                        throw new IllegalArgumentException("Customer Name must not have any letter repeated more than 5 times.");
                    }
                }
    
                // Remove the order from the database
                try (Connection connection = DBHelper.getConnection();
                     PreparedStatement stmt = connection.prepareStatement("DELETE FROM orders WHERE customer_name = ?")) {
                    stmt.setString(1, customerName);
                    int rowsAffected = stmt.executeUpdate();
    
                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(this, "Order removed successfully.");
    
                        // Refresh table by clearing it and re-fetching the data
                        orderTableModel.setRowCount(0);
                        try (Statement stmtRefresh = connection.createStatement();
                             ResultSet rs = stmtRefresh.executeQuery("SELECT * FROM orders")) {
                            while (rs.next()) {
                                orderTableModel.addRow(new Object[]{
                                        rs.getString("customer_name"),
                                        rs.getString("pizza_name"),
                                        rs.getDouble("pizza_cost"),
                                        rs.getInt("quantity")
                                });
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "No order found for the given customer name.", "Order Not Found", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error removing order: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    
        // Add buttons to the button panel
        buttonPanel.add(placeOrderButton);
        buttonPanel.add(removeOrderButton);
        orderPanel.add(buttonPanel, BorderLayout.SOUTH);
    
        contentPanel.add(orderPanel);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
}    
