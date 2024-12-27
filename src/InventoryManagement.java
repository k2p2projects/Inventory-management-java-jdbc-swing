import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class InventoryManagement extends JFrame {
    private JTextField itemNameField, itemQuantityField, itemCostField;
    private JTextField sellItemQuantityField;
    private JButton addButton, clearButton, sellButton, clearAllButton;
    private JComboBox<String> itemDropdown;
    private JTable table;
    private DefaultTableModel tableModel;
    private Connection connection;

    public InventoryManagement() {
        // Database connection setup
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/inventory_management?", "root", "root");
            JOptionPane.showMessageDialog(this, "Database connection established successfully!", "Connection Status", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database connection failed: " + e.getMessage(), "Connection Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            System.exit(1);
        }

        // Frame setup
        setTitle("Inventory Management System");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Tabbed pane setup
        JTabbedPane tabbedPane = new JTabbedPane();

        // Add Items panel
        JPanel addItemPanel = new JPanel(new GridLayout(5, 2));
        itemNameField = new JTextField();
        itemQuantityField = new JTextField();
        itemCostField = new JTextField();
        addButton = new JButton("Add");
        clearButton = new JButton("Clear");

        addItemPanel.add(new JLabel("Item Name:"));
        addItemPanel.add(itemNameField);
        addItemPanel.add(new JLabel("Item Quantity:"));
        addItemPanel.add(itemQuantityField);
        addItemPanel.add(new JLabel("Item Cost:"));
        addItemPanel.add(itemCostField);
        addItemPanel.add(addButton);
        addItemPanel.add(clearButton);
        tabbedPane.add("Add Items", addItemPanel);

        // Action listeners for add and clear buttons
        addButton.addActionListener(new AddButtonListener());
        clearButton.addActionListener(e -> clearFields());

        // Update Items panel
        JPanel updateItemPanel = new JPanel(new GridLayout(4, 2));
        itemDropdown = new JComboBox<>();
        sellItemQuantityField = new JTextField();
        sellButton = new JButton("Update");

        populateItemDropdown(itemDropdown);

        updateItemPanel.add(new JLabel("Item Name:"));
        updateItemPanel.add(itemDropdown);
        updateItemPanel.add(new JLabel("Quantity:"));
        updateItemPanel.add(sellItemQuantityField);
        updateItemPanel.add(new JLabel());
        updateItemPanel.add(sellButton);
        tabbedPane.add("Update Items", updateItemPanel);

        // Action listener for update button
        sellButton.addActionListener(new UpdateButtonListener());

        // Inventory Table panel
        JPanel tableViewPanel = new JPanel(new BorderLayout());
        tableModel = new DefaultTableModel(new String[]{"ID", "Item Name", "Quantity", "Cost"}, 0);
        table = new JTable(tableModel);
        clearAllButton = new JButton("Clear All Items");

        clearAllButton.addActionListener(e -> clearAllItems());

        tableViewPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        tableViewPanel.add(clearAllButton, BorderLayout.SOUTH);
        tabbedPane.add("Inventory Table", tableViewPanel);

        add(tabbedPane, BorderLayout.CENTER);
        setVisible(true);
        loadInventoryData();
    }

    private void clearFields() {
        itemNameField.setText("");
        itemQuantityField.setText("");
        itemCostField.setText("");
        sellItemQuantityField.setText("");
    }

    private void loadInventoryData() {
        tableModel.setRowCount(0);
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM inventory")) {
            while (rs.next()) {
                tableModel.addRow(new Object[]{rs.getInt("id"), rs.getString("item_name"), rs.getInt("item_quantity"), rs.getDouble("item_cost")});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void populateItemDropdown(JComboBox<String> dropdown) {
        dropdown.removeAllItems();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT item_name FROM inventory")) {
            while (rs.next()) {
                dropdown.addItem(rs.getString("item_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void clearAllItems() {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to clear all items?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Statement stmt = connection.createStatement()) {
                stmt.executeUpdate("DELETE FROM inventory");
                JOptionPane.showMessageDialog(this, "All items cleared successfully.");
                loadInventoryData();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private class AddButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = itemNameField.getText();
            int quantity = Integer.parseInt(itemQuantityField.getText());
            double cost = Double.parseDouble(itemCostField.getText());

            try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO inventory (item_name, item_quantity, item_cost) VALUES (?, ?, ?)")) {
                stmt.setString(1, name);
                stmt.setInt(2, quantity);
                stmt.setDouble(3, cost);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(InventoryManagement.this, "Item added successfully");
                clearFields();
                loadInventoryData();
                populateItemDropdown(itemDropdown);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private class UpdateButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String selectedItem = (String) itemDropdown.getSelectedItem();
            int updatedQuantity = Integer.parseInt(sellItemQuantityField.getText());

            try (PreparedStatement stmt = connection.prepareStatement("UPDATE inventory SET item_quantity = ? WHERE item_name = ?")) {
                stmt.setInt(1, updatedQuantity);
                stmt.setString(2, selectedItem);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(InventoryManagement.this, "Item updated successfully.");
                loadInventoryData();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            clearFields();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(InventoryManagement::new);
    }
}
