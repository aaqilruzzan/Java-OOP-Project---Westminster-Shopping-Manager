import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Scanner;
import javax.swing.*;

public class WestminsterShoppingManager implements ShoppingManager{
    static ArrayList<Product> products = new ArrayList<>();
    static int electronic_pid;
    static int clothing_pid;

    private static String getNonEmptyStringInput(Scanner scanner, String prompt, boolean lettersRequired) {
        String input;
        do {
            System.out.println(prompt);

            input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("Invalid input. Please enter a non-empty string.");
            } else if (lettersRequired && !input.matches(".*[a-zA-Z]+.*")) {
                System.out.println("Invalid input. Letters are required. Please enter a valid string.");
                input = ""; // Setting to a value that will force the loop to continue
            }
        } while (input.isEmpty());
        return input;
    }


    private static int getPositiveIntInput(Scanner scanner, String prompt) {
        int input;
        do {
            System.out.println(prompt);
            try {
                input = Integer.parseInt(scanner.nextLine());
                if (input <= 0) {
                    System.out.println("Invalid input. Please enter a positive integer.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
                input = -1; // Setting to a value that will force the loop to continue
            }
        } while (input <= 0);
        return input;
    }

    private static double getPositivedoubleInput(Scanner scanner, String prompt) {
        double input;
        do {
            System.out.print(prompt);
            try {
                input = Double.parseDouble(scanner.nextLine());
                if (input <= 0) {
                    System.out.println("Invalid input. Please enter a positive double value.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid double value.");
                input = -1; // Setting to a value that will force the loop to continue
            }
        } while (input <= 0);
        return input;
    }

    public static void addProduct(Scanner scanner){
        if(products.size()>50){
            System.out.println("Maximum number of products reached");
            return;
        }
        Scanner input = new Scanner(System.in);
        System.out.println("What is the type of product you want to add? Enter the relevant number" +
                "\n1. Electronics" + "\n2. Clothing");
        int choice = getPositiveIntInput(input, "Enter your choice: ");
        // Finding the maximum values of electronic_pid and clothing_pid in order to determine the next one
        int maxElectronicPid = products.stream()
                .filter(product -> product instanceof Electronics)
                .map(product -> Integer.parseInt(product.getProduct_ID().substring(1)))
                .max(Integer::compareTo)
                .orElse(0);

        int maxClothingPid = products.stream()
                .filter(product -> product instanceof Clothing)
                .map(product -> Integer.parseInt(product.getProduct_ID().substring(1)))
                .max(Integer::compareTo)
                .orElse(0);

        switch (choice) {
            case 1:
                String productId = "A" + (++maxElectronicPid);
                String productName = getNonEmptyStringInput(input, "Enter the product name: ",true);;
                int noOfAvailableItems = getPositiveIntInput(input, "Enter the number of available items: ");
                double price = getPositivedoubleInput(input, "Enter the price: ");
                String brand = getNonEmptyStringInput(input, "Enter the brand: ",true);
                String warrantyPeriod = getNonEmptyStringInput(input, "Enter the warranty period: ",true);
                Electronics electronics_product = new Electronics(productId, productName, noOfAvailableItems, price, brand, warrantyPeriod);
                products.add(electronics_product);
                System.out.println("Product added successfully");
                break;
            case 2:
                productId = "B" + (++maxClothingPid);
                productName = getNonEmptyStringInput(input, "Enter the product name: ",true);
                noOfAvailableItems = getPositiveIntInput(input, "Enter the number of available items: ");
                price = getPositivedoubleInput(input, "Enter the price: ");
                String color = getNonEmptyStringInput(input, "Enter the color: ",true);
                String size = getNonEmptyStringInput(input, "Enter the size: ",false);
                Clothing clothing_product = new Clothing(productId, productName, noOfAvailableItems, price, color, size);
                products.add(clothing_product);
                System.out.println("Product added successfully");
                break;
            default:
                System.out.println("Invalid input");
        }
        System.out.println(products);
    }

    public static void deleteProduct() {
        Scanner input = new Scanner(System.in);
        String productId = getNonEmptyStringInput(input, "Enter the product ID: ",false);
        String product_type;
        boolean productFound = false;
        for (Product product : products) {
            if (product.getProduct_ID().equals(productId)) {
                productFound = true;
                product_type=product instanceof Electronics ? "Electronics" : "Clothing";
                products.remove(product);
                System.out.println(product_type + " Product deleted successfully");
                System.out.println(products.size()+" products remaining");
                break;
            }
        }
        if (!productFound) {
            System.out.println("Product not found");
        }
    }

    public static void printListOfProducts(){
        // sort the products by product id alphabetically
        products.sort(Comparator.comparing(Product::getProduct_ID));
        for(Product product: products){

            if(product instanceof Electronics){
                Electronics electronics_product = (Electronics) product;
                System.out.println("\nProduct Type: Electronics" +
                        "\nProduct ID: " + electronics_product.getProduct_ID() +
                        "\nProduct Name: " + electronics_product.getProduct_name() +
                        "\nAvailable Items: " + electronics_product.getNo_of_available_items() +
                        "\nPrice: " + electronics_product.getPrice() +
                        "\nBrand: " + electronics_product.getBrand() +
                        "\nWarranty Period: " + electronics_product.getWarranty_period());
            }
            else{
                Clothing clothing_product = (Clothing) product;
                System.out.println("\nProduct Type: Clothing" +
                        "\nProduct ID: " + clothing_product.getProduct_ID() +
                        "\nProduct Name: " + clothing_product.getProduct_name() +
                        "\nAvailable Items: " + clothing_product.getNo_of_available_items() +
                        "\nPrice: " + clothing_product.getPrice() +
                        "\nColor: " + clothing_product.getColor() +
                        "\nSize: " + clothing_product.getSize());
            }
        }
    }

    public static void saveProducts() throws IOException {
       File file= new File("products.txt");
       FileOutputStream fout= new FileOutputStream(file);
       ObjectOutputStream objout = new ObjectOutputStream(fout);

        Iterator itr=products.iterator();
        while(itr.hasNext()){
            Product product=(Product) itr.next();
            objout.writeObject(product);
        }
        System.out.println("Products saved successfully");
    }

    public static void loadProducts(){
        try {
            FileInputStream fin = new FileInputStream("products.txt");
            ObjectInputStream objin = new ObjectInputStream(fin);
            while (true) {
                try {
                    Product product = (Product) objin.readObject();
                    products.add(product);
                } catch (EOFException e) {
                    break;
                }
            }
            System.out.println("Products loaded successfully");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No products found");
        }
    }

    public static void opengui(User user){
        SwingUtilities.invokeLater(() -> {
            GUI gui = new GUI(user);
            gui.createAndShowGUI();
        });
    }

    public static User getUserInfo() {
        Scanner scanner = new Scanner(System.in);
        String userName = getNonEmptyStringInput(scanner, "Enter your username:",false);
        String password = getNonEmptyStringInput(scanner, "Enter your password:",true);

        return new User(userName,password);
    }


        public static void main(String[] args) throws IOException {

            Scanner input = new Scanner(System.in);
            loadProducts();

            System.out.println("Welcome to Westminster Shopping Manager");
            while (true) {
                System.out.println("\nWhat do you want to do? Enter the relevant number" +
                        "\n1. Add a product" + "\n2. Delete a product" + "\n3. Print the list of products" +
                        "\n4. Save products" + "\n5. Exit" + "\n6. Customer GUI");

                int choice = getPositiveIntInput(input, "Enter your choice: ");
                switch (choice) {
                    case 1:
                        addProduct(input);
                        break;
                    case 2:
                        deleteProduct();
                        break;
                    case 3:
                        printListOfProducts();
                        break;
                    case 4:
                        saveProducts();
                        break;
                    case 5:
                        System.out.println("Thank you for using Westminster Shopping Manager");
                        System.exit(0);
                    case 6:
                        User user = getUserInfo();
                        opengui(user);
                        break;
                    default:
                        System.out.println("Invalid input");
                }
            }
        }

    }
