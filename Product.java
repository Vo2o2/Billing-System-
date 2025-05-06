public class Product {
    private int barcode;
    private String productName;
    private double price;
    private int quantity;

    // Constructor
    public Product(int barcode, String productName, double price, int quantity) {
        this.barcode = barcode;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
    }

    // Getters and Setters
    public int getBarcode() {
        return barcode;
    }

    public void setBarcode(int barcode) {
        this.barcode = barcode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Product{" +
               "barcode=" + barcode +
               ", productName='" + productName + '\'' +
               ", price=" + price +
               ", quantity=" + quantity +
               '}';
    }
}
