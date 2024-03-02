import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {
    public static List<Product> products;

    public ShoppingCart() {
        this.products = new ArrayList<>();
    }

    // Method to add a product to the cart
    public void addProduct(Product product) {
        products.add(product);
    }

    // Method to remove a product from the cart
    public void removeProduct(Product product) {
        products.remove(product);
    }

    // Method to calculate the total cost of all products in the cart
    public double calculateTotalCost() {
        double totalCost = 0.0;
        for (Product product : products) {
            totalCost += product.price*product.selected_quantity;
        }
        return totalCost;
    }

}
