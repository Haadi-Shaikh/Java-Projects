import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

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
    private static final String DB_URL = "jdbc:mysql://localhost:3306/pizza_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

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
        DefaultTableModel menuTableModel = new DefaultTableModel(new String[]{"Pizza ID", "Pizza Name", "Pizza Cost"}, 0);
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

    private void showEditPizzaPage() 
    {
        contentPanel.removeAll();
        JPanel editPizzaPanel = new JPanel(null);
        // editPizzaPanel.setBackground(Color.RED);

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

            try (Connection connection = DBHelper.getConnection();
                 PreparedStatement stmt = connection.prepareStatement("INSERT INTO pizza (pizza_id, pizza_name, pizza_price) VALUES (?, ?, ?)")) 
            {
                stmt.setInt(1, Integer.parseInt(id));
                stmt.setString(2, name);
                stmt.setString(3, cost);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Pizza added successfully ");
            } catch (SQLException ex) 
            {
                JOptionPane.showMessageDialog(this, "Error adding pizza", "Database Error", JOptionPane.ERROR_MESSAGE);
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

    JTable orderTable = new JTable();
    DefaultTableModel orderTableModel = new DefaultTableModel(new String[]{"Customer Name", "Pizza Name", "Pizza Cost", "Quantity"}, 0);
    orderTable.setModel(orderTableModel);
    orderTable.getColumnModel().getColumn(0).setPreferredWidth(80); 
    orderTable.getColumnModel().getColumn(1).setPreferredWidth(150);
    orderTable.getColumnModel().getColumn(2).setPreferredWidth(150);

    orderTable.setRowHeight(30);
    orderTable.setFont(new Font("Arial", Font.PLAIN, 14)); 
    JScrollPane scrollPane = new JScrollPane(orderTable);
    orderPanel.add(scrollPane, BorderLayout.CENTER);

    // Fetch and display previous orders from the database
    try (Connection connection = DBHelper.getConnection();
         Statement stmt = connection.createStatement();
         ResultSet rs = stmt.executeQuery("SELECT * FROM orders")) {
        while (rs.next()) {
            String customerName = rs.getString("customer_name");
            String pizzaName = rs.getString("pizza_name");
            double pizzaCost = rs.getDouble("pizza_cost");
            int quantity = rs.getInt("quantity");
            // Add each order to the table model
            orderTableModel.addRow(new Object[]{customerName, pizzaName, pizzaCost, quantity});
        }
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Error loading previous orders", "Database Error", JOptionPane.ERROR_MESSAGE);
    }

    JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
    JButton placeOrderButton = new JButton("Place Order");
    placeOrderButton.setBackground(Color.RED);
    placeOrderButton.setForeground(Color.WHITE);
    placeOrderButton.addActionListener(e -> {
        String customerName = JOptionPane.showInputDialog("Enter Customer Name:");
        String pizzaName = JOptionPane.showInputDialog("Enter Pizza Name:");
        String cost = JOptionPane.showInputDialog("Enter Pizza Cost:");
        String quantity = JOptionPane.showInputDialog("Enter Quantity:");

        try (Connection connection = DBHelper.getConnection();
             PreparedStatement stmt = connection.prepareStatement("INSERT INTO orders (customer_name, pizza_name, pizza_cost, quantity) VALUES (?, ?, ?, ?)")) {
            stmt.setString(1, customerName);
            stmt.setString(2, pizzaName);
            stmt.setDouble(3, Double.parseDouble(cost));
            stmt.setInt(4, Integer.parseInt(quantity));
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Order placed successfully");

            // Update table with new order
            orderTableModel.addRow(new Object[]{customerName, pizzaName, Double.parseDouble(cost), Integer.parseInt(quantity)});
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error placing order", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    });
    buttonPanel.add(placeOrderButton);

    JButton removeOrderButton = new JButton("Remove Order");
    removeOrderButton.setBackground(Color.RED);
    removeOrderButton.setForeground(Color.WHITE);
    removeOrderButton.addActionListener(e -> {
        String customerName = JOptionPane.showInputDialog("Enter Customer Name:");

        try (Connection connection = DBHelper.getConnection();
             PreparedStatement stmt = connection.prepareStatement("DELETE FROM orders WHERE customer_name = ?")) {
            stmt.setString(1, customerName);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Order removed successfully");

            // Refresh table by clearing it and re-fetching the data
            orderTableModel.setRowCount(0);
            try (Connection conn = DBHelper.getConnection();
                 Statement stmtRefresh = conn.createStatement();
                 ResultSet rs = stmtRefresh.executeQuery("SELECT * FROM orders")) {
                while (rs.next()) {
                    orderTableModel.addRow(new Object[]{rs.getString("customer_name"), rs.getString("pizza_name"), rs.getDouble("pizza_cost"), rs.getInt("quantity")});
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error removing order", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    });
    buttonPanel.add(removeOrderButton);

    orderPanel.add(buttonPanel, BorderLayout.SOUTH);
    contentPanel.add(orderPanel);
    contentPanel.revalidate();
    contentPanel.repaint();
    }
}
