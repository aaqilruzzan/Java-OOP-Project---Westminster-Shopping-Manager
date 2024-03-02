import java.io.Serializable;

public class Clothing extends Product implements Serializable {
    private String color;
    private String size;

    public Clothing(String product_ID, String product_name, int no_of_available_items, double price, String color, String size) {
        super(product_ID, product_name, no_of_available_items, price);
        this.color = color;
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @Override
    public String toString() {

            return "Clothing{" +
                    "color='" + color + '\'' +
                    ", size='" + size + '\'' +
                    '}';
    }
}
