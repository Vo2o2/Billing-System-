import java.sql.*;
import java.util.Map;
import java.util.HashMap;

public class BillingSystemBackend2 {
    private Connection conn;
    private String username = "postgres";  
    private String password = "12345";  

    public BillingSystemBackend2() {
        try {
            
            Class.forName("org.postgresql.Driver");

           
            conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/products", "postgres", "12345");

            // Check if the connection is successful
            if (conn != null) {
                System.out.println("Connected to PostgreSQL database!");
            } else {
                System.out.println("Failed to connect to the database.");
            }
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Connection to the database failed.");
            e.printStackTrace();
        }
    }

    // Method to get the product details by barcode (returns a Product object)
    public Product getProductByBarcode(int barcode) {
        Product product = null;
        
        if (conn != null) {  // Check if the connection is not null
            try {
                String sql = "SELECT product_name, price, quantity FROM products WHERE barcode = ?";
                var preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setInt(1, barcode);
                var resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    String productName = resultSet.getString("product_name");
                    double price = resultSet.getDouble("price");
                    int quantity = resultSet.getInt("quantity");
                    product = new Product(barcode, productName, price, quantity);
                    System.out.println("Product found: " + product);  // Debugging log
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Connection is null. Cannot fetch product.");
        }
        return product;
    }

    // Method to calculate the total price for the items in the bill
    public double calculateTotal(Map<String, Integer> billItems) {
        double total = 0.0;

        // Loop through each item in the bill
        for (Map.Entry<String, Integer> entry : billItems.entrySet()) {
            String productName = entry.getKey();
            int quantity = entry.getValue();

            // Assuming you can get the barcode for the product from your database
            int barcode = getBarcodeByProductName(productName);  // This is your helper method to get the barcode
            double price = getProductPriceByBarcode(barcode);  // Corrected to use the method getProductPriceByBarcode

            total += price * quantity;
        }

        return total;
    }

    // Helper method to get the barcode by product name
    public int getBarcodeByProductName(String productName) {
        int barcode = 0;
        
        if (conn != null) {  // Check if the connection is not null
            try {
                String sql = "SELECT barcode FROM products WHERE product_name = ?";
                var preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setString(1, productName);
                var resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    barcode = resultSet.getInt("barcode");
                    System.out.println("Found barcode for product: " + barcode);  // Debugging log
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Connection is null. Cannot fetch barcode.");
        }
        return barcode;
    }

    // Method to get the product price by barcode
    public double getProductPriceByBarcode(int barcode) {
        double price = 0.0;
        
        if (conn != null) {  // Check if the connection is not null
            try {
                String sql = "SELECT price FROM products WHERE barcode = ?";
                var preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setInt(1, barcode);
                var resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    price = resultSet.getDouble("price");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Connection is null. Cannot fetch price.");
        }
        return price;
    }

    // Method to apply discount on the total price
    public double applyDiscount(double total, double discount) {
        double discountAmount = (total * discount) / 100;
        return total - discountAmount;
    }

    public static void main(String[] args) {
        // Testing the database connection and product fetching
        BillingSystemBackend2 backend = new BillingSystemBackend2();
        Product product = backend.getProductByBarcode(1001);  // Example barcode
        if (product != null) {
            System.out.println("Product found: " + product);
        } else {
            System.out.println("Product not found!");
        }
    }
}
