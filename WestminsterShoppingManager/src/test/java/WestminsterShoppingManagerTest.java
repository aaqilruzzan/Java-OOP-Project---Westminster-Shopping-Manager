import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

import static org.junit.Assert.*;

public class WestminsterShoppingManagerTest {

    @Test
    public void addProduct() {
        // Setting up input for the test (simulate user input)
        String simulatedUserInput = "1\nProduct Name\n5\n10.0\nBrand\nWarranty\n";
        InputStream originalSystemIn = System.in;
        System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));

        // Setting up output capture
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        // Execute the method to be tested
        WestminsterShoppingManager.addProduct(new Scanner(System.in));

        // Restore original System.in and System.out
        System.setIn(originalSystemIn);
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));

        // validate/ perform assertions
        assertTrue(outputStreamCaptor.toString().contains("Product added successfully"));

    }

    @Test
    public void deleteProduct() {
        // Setting up initial product for testing
        WestminsterShoppingManager.products.clear();
        WestminsterShoppingManager.products.add(new Electronics("A1", "TestProduct", 10, 20.0, "TestBrand", "TestWarranty"));

        // Setting up input for the test (simulate user input)
        String simulatedUserInput = "A1\n";
        InputStream originalSystemIn = System.in;
        System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));

        // Setting up output capture
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        // Execute the method to be tested
        WestminsterShoppingManager.deleteProduct();

        // Restoring original System.in and System.out
        System.setIn(originalSystemIn);
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));

        // Validate / perform assertions
        assertTrue(outputStreamCaptor.toString().contains("Electronics Product deleted successfully"));
    }

    @Test
    public void printListOfProducts() {
        // Setting up initial products for testing
        WestminsterShoppingManager.products.clear();
        WestminsterShoppingManager.products.add(new Electronics("A1", "TestProductE", 10, 20.0, "TestBrand", "TestWarranty"));
        WestminsterShoppingManager.products.add(new Clothing("B1", "TestProductC", 15, 25.0, "TestColor", "TestSize"));

        // Setting up input for the test (simulate user input)
        String simulatedUserInput = "";
        InputStream originalSystemIn = System.in;
        System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));

        // Setting up output capture
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        // Executing the method to be tested
        WestminsterShoppingManager.printListOfProducts();

        // Restoring original System.in and System.out
        System.setIn(originalSystemIn);
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));

        // Validate / perform assertions
        assertTrue(outputStreamCaptor.toString().contains("Product Type: Electronics"));
        assertTrue(outputStreamCaptor.toString().contains("Product Type: Clothing"));

    }

    @Test
    public void saveProductsTest() {
        // Setting up initial products for testing
        WestminsterShoppingManager.products.clear();
        WestminsterShoppingManager.products.add(new Electronics("A1", "TestProductE", 10, 20.0, "TestBrand", "TestWarranty"));
        WestminsterShoppingManager.products.add(new Clothing("B1", "TestProductC", 15, 25.0, "TestColor", "TestSize"));

        // Setting up output capture
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        // Executing the method to be tested
        try {
            WestminsterShoppingManager.saveProducts();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Validating the output or perform assertions
        assertTrue(outputStreamCaptor.toString().contains("Products saved successfully"));

        // Optionally, checking the content of the saved file
        try {
            FileInputStream fin = new FileInputStream("products.txt");
            ObjectInputStream objin = new ObjectInputStream(fin);
            ArrayList<Product> loadedProducts = new ArrayList<>();
            while (true) {
                try {
                    Product product = (Product) objin.readObject();
                    loadedProducts.add(product);
                } catch (EOFException e) {
                    break;
                }
            }
            assertEquals(WestminsterShoppingManager.products.size(), loadedProducts.size());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }



}