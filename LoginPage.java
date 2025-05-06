import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class LoginPage extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginPage() {
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 2, 10, 10));  // Added spacing between components

       
        setPreferredSize(new Dimension(300, 150));
        pack();  
        setLocationRelativeTo(null);  

        // Remove non-resizable if you want the option to maximize (optional)
        setResizable(true);  // Make the window resizable (including maximize)

        // Create components for username and password fields
        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();
        
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();

        // Create login button and add action listener
        loginButton = new JButton("Login");
        loginButton.addActionListener(new LoginListener());

        // Add components to the frame
        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(new JLabel());  // Empty label for spacing
        add(loginButton);

        // Make the window visible
        setVisible(true);
    }

    private class LoginListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText();
            char[] passwordArray = passwordField.getPassword();
            String password = new String(passwordArray);

            // Authenticate credentials (hardcoded for demo purposes)
            if (authenticate(username, password)) {
                // Hide login page
                setVisible(false);

                // Display success message with green checkmark icon
                showSuccessMessage();

                // Ensure BillingSystemGUI2 runs on the Event Dispatch Thread (EDT)
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        new BillingSystemGUI2();  // Show the billing system GUI after successful login
                    }
                });
            } else {
                JOptionPane.showMessageDialog(null, "Invalid credentials. Try again.");
            }
        }
    }

    private boolean authenticate(String username, String password) {
        // Simple authentication logic (hardcoded username and password)
        return username.equals("admin") && password.equals("12345");
    }

    private void showSuccessMessage() {
        // Create a success message panel
        JPanel successPanel = new JPanel();
        successPanel.setLayout(new BoxLayout(successPanel, BoxLayout.Y_AXIS));
        successPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create a success message with a green checkmark
        JLabel successLabel = new JLabel("Login Successful!");
        successLabel.setFont(new Font("Arial", Font.BOLD, 18));
        successLabel.setForeground(Color.GREEN);

        // Adding green checkmark icon (you can replace this with any image you prefer)
        JLabel checkmarkLabel = new JLabel("\u2714"); // Unicode for checkmark
        checkmarkLabel.setFont(new Font("Arial", Font.PLAIN, 30));
        checkmarkLabel.setForeground(Color.GREEN);

        // Add components to the success panel
        successPanel.add(successLabel);
        successPanel.add(checkmarkLabel);

        // Create a custom dialog to show the success message
        JOptionPane.showMessageDialog(this, successPanel, "Success", JOptionPane.PLAIN_MESSAGE);
    }

    public static void main(String[] args) {
        // Launch the LoginPage when the application starts
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new LoginPage();
            }
        });
    }
}
