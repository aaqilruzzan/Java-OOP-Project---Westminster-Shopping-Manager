import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Random;

public class User implements java.io.Serializable {

    private String id;
    private String username;
    private String password;

    private int purchase_frequency = 0;
    private static final long serialVersionUID = 1L;
    static ArrayList<User> users = new ArrayList<>();
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.id=generateRandomUserId();
    }

    private String generateRandomUserId() {
        Random random = new Random();
        int minId = 1000; // Set a minimum value for the user ID
        int maxId = 9999; // Set a maximum value for the user ID

        // Generating a random number within the specified range
        int randomId = random.nextInt(maxId - minId + 1) + minId;

        return Integer.toString(randomId);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static void loadUsers(){
        try {
            FileInputStream fin = new FileInputStream("user.txt");
            ObjectInputStream objin = new ObjectInputStream(fin);
            while (true) {
                try {
                    User user = (User) objin.readObject();
                    users.add(user);
                } catch (EOFException e) {
                    break;
                }
            }
            System.out.println("Users loaded successfully");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No users found");
        }
    }

    public int getPurchase_frequency() {
        loadUsers();
        for (User user:users){
            if (user.getUsername().equals(this.username)){
                purchase_frequency= user.purchase_frequency;
            }
        }
        return purchase_frequency;
    }

    public void setPurchase_frequency(int purchase_frequency) {
        this.purchase_frequency = purchase_frequency;
    }

    public String getId() {
        return id;
    }
}
