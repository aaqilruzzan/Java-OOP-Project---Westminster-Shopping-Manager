abstract public class Product implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    protected String product_ID;
    protected String product_name;
    protected int no_of_available_items;
    protected double price;
    protected int selected_quantity;

    public Product(String productId, String productName, int noOfAvailableItems, double price) {
        this.product_ID = productId;
        this.product_name = productName;
        this.no_of_available_items = noOfAvailableItems;
        this.price = price;
    }

    public String getProduct_ID() {
        return product_ID;
    }

    public void setProduct_ID(String product_ID) {
        this.product_ID = product_ID;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public int getNo_of_available_items() {
        return no_of_available_items;
    }

    public void setNo_of_available_items(int no_of_available_items) {
        this.no_of_available_items = no_of_available_items;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getSelected_quantity() {
        return selected_quantity;
    }

    public void setSelected_quantity(int selected_quantity) {
        this.selected_quantity = selected_quantity;
    }
}
