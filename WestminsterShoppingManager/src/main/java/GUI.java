import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class GUI {
    private JFrame frame;
    private JComboBox<String> productTypeComboBox;
    private JTable productTable;
    private JPanel productDetailsPanel;
    private JButton addToCartButton;
    private JButton viewCartButton;

    private JTextArea productDetailsText;
    private Product selectedProduct;
    private ArrayList<Product> selectedProductsList = new ArrayList<>(); // Add this variable to store selected products

    private User user;
    private double firstPurchaseDiscount = 0.0;
    private double threeItemsDiscount = 0.0;
    ShoppingCart shoppingCart;

    public GUI(User user) {
        this.user = user;
        shoppingCart = new ShoppingCart();
        firstPurchaseDiscount = (user.getPurchase_frequency() == 1) ? 0.1 : 0.0;
        // Initializing components and setting up layout
        productTypeComboBox = new JComboBox<>(new String[]{"All", "Electronics", "Clothing"});

        productTable = new JTable();
        productDetailsPanel = new JPanel();
        addToCartButton = new JButton("Add to Shopping Cart");
        viewCartButton = new JButton("Shopping Cart");

        // Add action listeners for components
        addToCartButton.addActionListener(e -> addToCart());
        viewCartButton.addActionListener(e -> viewCart());
        productTypeComboBox.addActionListener(e -> updateTable()); // Add listener to update table when selection changes

        productTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                // Check if the row selection is not adjusting (to avoid duplicate events)
                if (!e.getValueIsAdjusting()) {
                    // Get the selected row
                    int selectedRow = productTable.getSelectedRow();

                    // Update productDetailsText based on the selected row
                    if (selectedRow != -1) {
                        String productId = (String) productTable.getValueAt(selectedRow, 0);

                        // Find the corresponding product in the products array
                        Product selectedProduct = findProductById(productId);

                        // Update productDetailsText based on the selected product
                        if (selectedProduct != null) {
                            String productName = selectedProduct.getProduct_name();
                            String category = (selectedProduct instanceof Electronics) ? "Electronics" : "Clothing";
                            double price = selectedProduct.getPrice();
                            String info = (selectedProduct instanceof Electronics)
                                    ? ((Electronics) selectedProduct).getBrand() + ", " + ((Electronics) selectedProduct).getWarranty_period()
                                    : ((Clothing) selectedProduct).getSize() + ", " + ((Clothing) selectedProduct).getColor();
                            int availableItems = selectedProduct.getNo_of_available_items();

                            String productDetails = String.format(
                                    "Selected Product - Details\n\n" +
                                            "Product Id: %s\n\n" +
                                            "Category: %s\n\n" +
                                            "Name: %s\n\n" +
                                            "Price: £%.2f\n\n" +
                                            "Info: %s\n\n" +
                                            "Available Items: %d",
                                    productId, category, productName, price, info, availableItems
                            );

                            productDetailsText.setText(productDetails);
                        }
                    }
                }
            }
        });

// ...


    }

//    private static void saveUser(User newUser) {
//        try (ObjectInputStream objin = new ObjectInputStream(new FileInputStream("user.txt"));
//             ObjectOutputStream objout = new ObjectOutputStream(new FileOutputStream("temp_users.txt"))) {
//
//
//            // Find the index of the user with the same ID (assuming getId() is a method in your User class)
//            int index = -1;
//            for (int i = 0; i < User.users.size(); i++) {
//                if (User.users.get(i).getId().equals(newUser.getId())) {
//                    index = i;
//                    break;
//                }
//            }
//
//            // If the user exists, replace it; otherwise, add the new user
//            if (index != -1) {
//                User.users.set(index, newUser);
//            } else {
//                User.users.add(newUser);
//            }
//
//           for (User user : User.users) {
//                objout.writeObject(user);
//            }
//
//            // Rename the temporary file to the original file
//            File tempFile = new File("temp_users.txt");
//            File originalFile = new File("user.txt");
//            if (tempFile.renameTo(originalFile)) {
//                System.out.println("User information saved successfully");
//            } else {
//                System.out.println("Error saving user information");
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }


    private void addToCart() {
        // Get the selected row

        int selectedRow = productTable.getSelectedRow();

        // Check if a row is selected
        if (selectedRow != -1) {
            user.setPurchase_frequency(user.getPurchase_frequency() + 1);
//            saveUser(user);
            String productId = (String) productTable.getValueAt(selectedRow, 0);


            // Find the corresponding product in the products array
            selectedProduct = findProductById(productId);
            selectedProduct.setNo_of_available_items(selectedProduct.getNo_of_available_items() - 1);
            selectedProduct.setSelected_quantity(selectedProduct.getSelected_quantity() + 1);

            if (selectedProduct != null) {
                System.out.println("Added to Shopping Cart: " + selectedProduct.getProduct_name());
            }

            if (!shoppingCart.products.contains(selectedProduct)) {
                shoppingCart.addProduct(selectedProduct);
            }

        }

    }

    private Product findProductById(String productId) {
        for (Product product : WestminsterShoppingManager.products) {
            if (product.getProduct_ID().equals(productId)) {
                return product;
            }
        }
        return null; // Product not found
    }

    private void updateTable() {
        // Get the selected category from the combo box
        String selectedCategory = (String) productTypeComboBox.getSelectedItem();

        // Create a table model with data
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Product ID");
        tableModel.addColumn("Name");
        tableModel.addColumn("Category");
        tableModel.addColumn("Price (£)");
        tableModel.addColumn("Info");
        tableModel.addColumn("Available Items");

        // Add data from the products ArrayList to the table model based on the selected category
        for (Product product : WestminsterShoppingManager.products) {
            String category = (product instanceof Electronics) ? "Electronics" : "Clothing";

            // Only adding the product to the table if the category matches the selected category or "All" is selected
            if (selectedCategory.equals("All") || selectedCategory.equals(category)) {
                String productId = product.getProduct_ID();
                String productName = product.getProduct_name();
                double price = product.getPrice();
                int availableItems = product.getNo_of_available_items();
                String info = (product instanceof Electronics) ? ((Electronics) product).getBrand() + ", " + ((Electronics) product).getWarranty_period()
                        : ((Clothing) product).getSize() + ", " + ((Clothing) product).getColor();

                tableModel.addRow(new Object[]{productId, productName, category, price, info, availableItems});
            }
        }

        // Setting the table model to the JTable
        productTable.setModel(tableModel);
    }


    private void viewCart() {
        if (!shoppingCart.products.isEmpty()) {
            // Creating a new frame for the shopping cart view
            JFrame cartFrame = new JFrame("Shopping Cart");
            cartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Dispose the frame on close
            cartFrame.setSize(700, 500);

            // Creating a table model with data for the shopping cart
            DefaultTableModel cartTableModel = new DefaultTableModel();
            cartTableModel.addColumn("Product");
            cartTableModel.addColumn("Quantity");
            cartTableModel.addColumn("Price");

            for (Product selectedProduct : shoppingCart.products) {
                Object[] cartData = {selectedProduct.getProduct_name(), selectedProduct.getSelected_quantity(), selectedProduct.getPrice()};
                cartTableModel.addRow(cartData);
            }


            // Set the table model to the JTable
            JTable cartTable = new JTable(cartTableModel);

            // Add the cart table to the frame with a top margin
            JScrollPane cartScrollPane = new JScrollPane(cartTable);
            cartScrollPane.setBorder(new EmptyBorder(30, 20, 30, 20)); // Set top margin
            cartFrame.add(cartScrollPane);

            // Create a panel for additional information
            JPanel infoPanel = new JPanel(new GridBagLayout());

            double totalAmount = shoppingCart.calculateTotalCost();
            double firstPurchaseDiscount = (user.getPurchase_frequency() == 1) ? totalAmount * 0.1 : 0.0;
            for (Product selectedProduct : shoppingCart.products) {
                if (selectedProduct instanceof Electronics) {
                    if (selectedProduct.getSelected_quantity() >= 3) {
                        threeItemsDiscount += totalAmount * 0.2;
                    }
                } else if (selectedProduct instanceof Clothing) {
                    if (selectedProduct.getSelected_quantity() >= 3) {
                        threeItemsDiscount += totalAmount * 0.2;
                    }

                }
            }

            JLabel infoText = new JLabel(
                    String.format("<html><div style='text-align: right;'>" +
                            "Total&nbsp &nbsp &nbsp &nbsp %.2f$<br> <br>" +
                            "First Purchase Discount (10%%)&nbsp &nbsp &nbsp &nbsp -%.2f $<br> <br>" +
                            "Three items in the same Category Discount (20%%)&nbsp &nbsp &nbsp &nbsp -%.2f $<br> <br>" +
                            "Final Total&nbsp &nbsp &nbsp &nbsp %.2f $" +
                            "</div></html>", totalAmount, firstPurchaseDiscount, threeItemsDiscount, totalAmount - firstPurchaseDiscount - threeItemsDiscount)
            );


            infoText.setBorder(new EmptyBorder(0, 0, 30, 50)); // Set bottom margin

            // Setting GridBagConstraints to position the label at the bottom-right
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.anchor = GridBagConstraints.SOUTHEAST; // Align to the bottom-right
            gbc.weightx = 1.0; // Expand horizontally
            gbc.weighty = 1.0; // Expand vertically
            infoPanel.add(infoText, gbc);

            // Adding the infoPanel to the bottom of the frame
            cartFrame.add(infoPanel, BorderLayout.SOUTH);

            // Making the frame visible
            cartFrame.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(frame, "Shopping Cart is empty. Add products before viewing the cart.", "Empty Cart", JOptionPane.INFORMATION_MESSAGE);
        }

    }


    public void createAndShowGUI() {
        JFrame frame = new JFrame("Westminster Shopping Center");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);
        frame.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());

        JLabel additionalTextLabel = new JLabel("Select Product Category");
        additionalTextLabel.setBorder(new EmptyBorder(0, 0, 0, 20)); // Add right margin
        // Calculating the width for the JComboBox based on 20% of the frame size
        int comboBoxWidth = (int) (frame.getWidth() * 0.2);

        // Setting the preferred size for the JComboBox
        productTypeComboBox.setPreferredSize(new Dimension(comboBoxWidth, productTypeComboBox.getPreferredSize().height));

        // Creating a JPanel for the productTypeComboBox with a margin at the top
        JPanel comboBoxPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        comboBoxPanel.setBorder(new EmptyBorder(40, 0, 0, 0)); // Add top margin
        comboBoxPanel.add(additionalTextLabel);
        comboBoxPanel.add(productTypeComboBox);

        // borderlayout.center
        topPanel.add(comboBoxPanel, BorderLayout.CENTER);

        JPanel cartButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        cartButtonPanel.setBorder(new EmptyBorder(10, 0, 0, 10)); // Add top and right margin
        cartButtonPanel.add(viewCartButton);


        topPanel.add(cartButtonPanel, BorderLayout.EAST);

        frame.add(topPanel, BorderLayout.NORTH);

        DefaultTableModel tableModel = new DefaultTableModel();


        tableModel.addColumn("Product ID");
        tableModel.addColumn("Name");
        tableModel.addColumn("Category");
        tableModel.addColumn("Price (£)");
        tableModel.addColumn("Info");
        tableModel.addColumn("Available Items");

        // Adding data from the products ArrayList to the table model
        for (Product product : WestminsterShoppingManager.products) {
            String productId = product.getProduct_ID();
            String productName = product.getProduct_name();
            int availableItems = product.getNo_of_available_items();
            String category = (product instanceof Electronics) ? "Electronics" : "Clothing";
            double price = product.getPrice();
            String info = (product instanceof Electronics) ? ((Electronics) product).getBrand() + ", " + ((Electronics) product).getWarranty_period()
                    : ((Clothing) product).getSize() + ", " + ((Clothing) product).getColor();

            tableModel.addRow(new Object[]{productId, productName, category, price, info, availableItems});
        }

        productTable.setModel(tableModel);

        productTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                // Check the value of "Available Items" in the current row
                Object availableItemsObject = table.getModel().getValueAt(row, 5);

                // If "Available Items" is less than 3, setting the background color to red
                // Checking if the value is not null before attempting to convert it to int
                if (availableItemsObject != null) {
                    int availableItems = (int) availableItemsObject;

                    // If "Available Items" is less than 3, set the background color to red
                    if (availableItems < 3) {
                        c.setBackground(Color.RED);
                    } else {
                        // Resetting the background color for other rows
                        c.setBackground(table.getBackground());
                    }
                } else {
                    // Handle the case where the value is null
                    c.setBackground(table.getBackground()); // Set default background color
                }

                return c;
            }
        });

        // Adding the table to the frame with margins
        JScrollPane tableScrollPane = new JScrollPane(productTable);
        tableScrollPane.setBorder(new EmptyBorder(60, 20, 20, 20)); // Set margins
        frame.add(tableScrollPane, BorderLayout.CENTER);

        // Creating a panel for product details and "Add to Shopping Cart" button
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Color.WHITE);


        // Adding product details to the details panel
        productDetailsText = new JTextArea(
                "Selected Product - Details\n\n" +
                        "Product Id: \n\n" +
                        "Category: \n\n" +
                        "Name: \n\n" +
                        "Size: \n\n" +
                        "Colour: \n\n" +
                        "Items Available: "
        );
        productDetailsText.setEditable(false);
        productDetailsText.setMargin(new Insets(20, 40, 20, 40)); // Set margin

        productDetailsPanel.setBackground(Color.WHITE);
        productDetailsPanel.add(productDetailsText);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);

        buttonPanel.add(addToCartButton);

        bottomPanel.add(productDetailsPanel, BorderLayout.WEST);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        // Initializing other components and add them to the frame

        frame.setVisible(true);
    }
}
