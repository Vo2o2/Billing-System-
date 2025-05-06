import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BillingSystemGUI2 {
    private BillingSystemBackend2 backend;
    private JTextField barcodeField;
    private JTextArea billArea;
    private JLabel totalLabel;
    private JTextField discountField;
    private JTextField amountGivenField;  // New field for amount given by customer
    private Map<String, Integer> billItems;
    private double discountApplied = 0.0;
    private double totalAmount = 0.0;

    public BillingSystemGUI2() {
        backend = new BillingSystemBackend2();  // Ensure backend is initialized
        billItems = new HashMap<>();

        JFrame frame = new JFrame("Billing System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 700); // Increased size for better UI
        frame.setLayout(new BorderLayout(10, 10));  // Set margins for layout

        // Set background color
        frame.getContentPane().setBackground(new Color(240, 240, 240));

        // Set up the header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        JLabel titleLabel = new JLabel("Billing System", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(50, 50, 50));
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        headerPanel.setBackground(new Color(100, 149, 237));  // Cornflower Blue
        frame.add(headerPanel, BorderLayout.NORTH);

        // Panel for the barcode and discount inputs + buttons at the top-left
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(2, 6, 10, 10)); // Grid for inputs and buttons
        inputPanel.setBackground(new Color(240, 240, 240));

        // Total label (moved to the top)
        totalLabel = new JLabel("Total: $0.00");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalLabel.setForeground(new Color(50, 50, 50));
        inputPanel.add(totalLabel);

        JLabel barcodeLabel = new JLabel("Enter Barcode: ");
        barcodeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        barcodeLabel.setForeground(Color.BLACK);
        barcodeField = new JTextField(15);
        barcodeField.setFont(new Font("Arial", Font.PLAIN, 14));
        barcodeField.setBackground(Color.WHITE);
        barcodeField.setForeground(Color.BLACK);
        barcodeField.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        JLabel discountLabel = new JLabel("Discount (%): ");
        discountLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        discountLabel.setForeground(Color.BLACK);
        discountField = new JTextField(5);
        discountField.setFont(new Font("Arial", Font.PLAIN, 14));
        discountField.setBackground(Color.WHITE);
        discountField.setForeground(Color.BLACK);
        discountField.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        JLabel amountGivenLabel = new JLabel("Amount Given: ");
        amountGivenLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        amountGivenLabel.setForeground(Color.BLACK);
        amountGivenField = new JTextField(10);
        amountGivenField.setFont(new Font("Arial", Font.PLAIN, 14));
        amountGivenField.setBackground(Color.WHITE);
        amountGivenField.setForeground(Color.BLACK);
        amountGivenField.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        JButton addButton = new JButton("Add Product");
        addButton.setFont(new Font("Arial", Font.BOLD, 14));
        addButton.setBackground(new Color(100, 149, 237));
        addButton.setForeground(Color.WHITE);
        addButton.setPreferredSize(new Dimension(150, 40));

        JButton calculateButton = new JButton("Calculate Total");
        calculateButton.setFont(new Font("Arial", Font.BOLD, 14));
        calculateButton.setBackground(new Color(100, 149, 237));
        calculateButton.setForeground(Color.WHITE);
        calculateButton.setPreferredSize(new Dimension(150, 40));

        JButton saveButton = new JButton("Save Bill");
        saveButton.setFont(new Font("Arial", Font.BOLD, 14));
        saveButton.setBackground(new Color(100, 149, 237));
        saveButton.setForeground(Color.WHITE);
        saveButton.setPreferredSize(new Dimension(150, 40));

        JButton clearButton = new JButton("Clear Bill");
        clearButton.setFont(new Font("Arial", Font.BOLD, 14));
        clearButton.setBackground(new Color(100, 149, 237));
        clearButton.setForeground(Color.WHITE);
        clearButton.setPreferredSize(new Dimension(150, 40));

        // Online Payment Button
// Online Payment Button
JButton onlinePaymentButton = new JButton("Online Payment");
onlinePaymentButton.setFont(new Font("Arial", Font.BOLD, 14));
onlinePaymentButton.setBackground(new Color(100, 149, 237));
onlinePaymentButton.setForeground(Color.WHITE);
onlinePaymentButton.setPreferredSize(new Dimension(150, 40));

inputPanel.add(onlinePaymentButton);

// Action listener for the Online Payment Button
onlinePaymentButton.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        // Load the image
        ImageIcon imageIcon = new ImageIcon("/root/Desktop/project2/bank.jpeg");

        // Display the image in a JOptionPane in a default size
        JOptionPane.showMessageDialog(frame, 
                                      new JLabel(imageIcon), 
                                      "Online Payment QR", 
                                      JOptionPane.PLAIN_MESSAGE);
    }
});


        
        inputPanel.add(barcodeLabel);
        inputPanel.add(barcodeField);
        inputPanel.add(discountLabel);
        inputPanel.add(discountField);
        inputPanel.add(amountGivenLabel);
        inputPanel.add(amountGivenField);
        inputPanel.add(addButton);
        inputPanel.add(calculateButton);
        inputPanel.add(saveButton);
        inputPanel.add(clearButton);

        frame.add(inputPanel, BorderLayout.NORTH);

        // Bill area (this is where the bill will appear in the middle of the layout)
        JPanel billPanel = new JPanel();
        billPanel.setLayout(new BorderLayout());
        billArea = new JTextArea();
        billArea.setFont(new Font("Courier New", Font.PLAIN, 14));
        billArea.setBackground(new Color(255, 255, 255));  // White background
        billArea.setForeground(new Color(50, 50, 50));      // Dark text color
        billArea.setEditable(false);
        billPanel.add(new JScrollPane(billArea), BorderLayout.CENTER);
        frame.add(billPanel, BorderLayout.CENTER);

        // KeyListener for barcodeField to add product when Enter is pressed
        barcodeField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String barcodeText = barcodeField.getText().trim();

                    if (barcodeText.isEmpty()) {
                        JOptionPane.showMessageDialog(frame, "Please enter a barcode.");
                        return;
                    }

                    try {
                        int barcode = Integer.parseInt(barcodeText);

                        // Fetch the product by barcode from backend
                        Product product = backend.getProductByBarcode(barcode);
                        if (product != null) {
                            String productName = product.getProductName();
                            int quantity = 1;  // Default to 1 quantity
                            billItems.put(productName, billItems.getOrDefault(productName, 0) + quantity);
                            billArea.append(productName + " - Quantity: " + quantity + "\n");

                            // Update the total
                            totalAmount = backend.calculateTotal(billItems);
                            totalLabel.setText("Total: $" + totalAmount);
                        } else {
                            JOptionPane.showMessageDialog(frame, "Product not found.");
                        }

                        barcodeField.setText("");  // Clear barcode input

                        // Remove the "Add Product" button
                        addButton.setVisible(false);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(frame, "Invalid barcode.");
                    }
                }
            }
        });

        // KeyListener for discountField to apply discount when Enter is pressed
        discountField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String discountText = discountField.getText().trim();
                    try {
                        discountApplied = Double.parseDouble(discountText);
                        if (discountApplied >= 0 && discountApplied <= 100) {
                            totalAmount = backend.calculateTotal(billItems);
                            totalAmount = backend.applyDiscount(totalAmount, discountApplied);  // Apply the discount
                            totalLabel.setText("Total: $" + totalAmount);
                        } else {
                            JOptionPane.showMessageDialog(frame, "Discount must be between 0 and 100.");
                        }

                        // Remove the "Apply Discount" button
                        // Note: There is no button for applying the discount in this code.
                        // But if there was a button, you could hide it as follows:
                        // applyDiscountButton.setVisible(false);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(frame, "Invalid discount.");
                    }
                }
            }
        });

// Action listeners for other buttons
calculateButton.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        // Get the total after discount
        double total = totalAmount;

        // Get the amount given by the customer
        String amountGivenText = amountGivenField.getText().trim();

        try {
            // Try to parse the amount given
            double amountGiven = Double.parseDouble(amountGivenText);

            // Calculate the amount to return
            double amountToReturn = amountGiven - total;

            // Create the custom dialog panel
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            // Customize the font for a more noticeable appearance
            JLabel totalLabel = new JLabel("Total: $" + total);
            totalLabel.setFont(new Font("Arial", Font.BOLD, 20));
            totalLabel.setForeground(Color.BLACK);

            JLabel amountGivenLabel = new JLabel("Amount Given: $" + amountGiven);
            amountGivenLabel.setFont(new Font("Arial", Font.BOLD, 20));
            amountGivenLabel.setForeground(Color.BLACK);

            JLabel amountToReturnLabel = new JLabel("Amount to Return: $" + amountToReturn);
            amountToReturnLabel.setFont(new Font("Arial", Font.BOLD, 20));
            amountToReturnLabel.setForeground(Color.RED);  // Make it more noticeable by using red

            // Add the labels to the panel
            panel.add(totalLabel);
            panel.add(Box.createVerticalStrut(10));
            panel.add(amountGivenLabel);
            panel.add(Box.createVerticalStrut(10));
            panel.add(amountToReturnLabel);

            // Create the "OK" button
            JButton okButton = new JButton("OK");
            okButton.setFont(new Font("Arial", Font.BOLD, 14));
            okButton.setBackground(new Color(100, 149, 237));
            okButton.setForeground(Color.WHITE);
            okButton.setPreferredSize(new Dimension(100, 40));

            // Create the JDialog to show the panel
            JDialog dialog = new JDialog(frame, "Amount to Return", true);
            dialog.setContentPane(panel);
            dialog.setSize(400, 300); // Set a larger size for the dialog
            dialog.setLocationRelativeTo(frame); // Center the dialog on the main frame

            // Add an action listener to the "OK" button to close the dialog
            okButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    dialog.dispose();  // Close the dialog when "OK" is clicked
                }
            });

            // Add the "OK" button to the panel
            panel.add(Box.createVerticalStrut(20));  // Add space before the button
            panel.add(okButton);  // Add the OK button to the dialog

            dialog.setVisible(true); // Show the dialog

        } catch (NumberFormatException ex) {
            // If the amount given is invalid, show a message
            JOptionPane.showMessageDialog(frame, "Please enter a valid amount given.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }
});

        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String amountGivenText = amountGivenField.getText().trim();
                try {
                    double amountGiven = Double.parseDouble(amountGivenText);

                    // Calculate amount to return
                    double amountToReturn = amountGiven - totalAmount;

                    // Get current date and time
                    String currentDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

                    // Build the full bill information
                    String billContent = billArea.getText();
                    billContent += "\nDiscount: " + discountApplied + "%";
                    billContent += "\nAmount Given: $" + amountGiven;
                    billContent += "\nAmount to Give Back: $" + amountToReturn;
                    billContent += "\nDate and Time: " + currentDateTime;

                    // Show the complete bill in the text area
                    billArea.setText(billContent);

                    // Save bill to file
                    int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to save the bill?", "Save Bill", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        try {
                            BufferedWriter writer = new BufferedWriter(new FileWriter("/root/Desktop/savedbill/bill.txt"));
                            writer.write(billArea.getText());
                            writer.close();
                            JOptionPane.showMessageDialog(frame, "Bill saved successfully.");
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(frame, "Error saving bill.");
                        }
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid amount given.");
                }
            }
        });

        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Clear the bill and reset
                billItems.clear();
                billArea.setText("");
                totalLabel.setText("Total: $0.00");
                barcodeField.setText("");
                discountField.setText("");
                amountGivenField.setText("");
                addButton.setVisible(true);  // Show the "Add Product" button again
            }
        });

        // Display the frame
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new BillingSystemGUI2();
            }
        });
    }
}
