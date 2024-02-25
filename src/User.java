import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class User {

    public List<UserData> users = new ArrayList<>();
    public List<OrderData> orders = new ArrayList<>();
    private String filepathUser = "users.json";
    private UserData count = new UserData();
    public int currentUser = count.getUserCount();
    private BufferedReader bufferedReader;
    {
        bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    }
    private void SaveUsers(List<UserData> users) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            FileWriter fileWriter = new FileWriter(filepathUser);
            gson.toJson(users, fileWriter);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void LoadUsers() {
        users.clear();
        Gson gson = new Gson();
        try (BufferedReader reader = new BufferedReader(new FileReader(filepathUser))) {
            List<UserData> loadedUsers = gson.fromJson(reader, new TypeToken<ArrayList<UserData>>() {}.getType());
            users.addAll(loadedUsers);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public char UserMenu() throws IOException, NoSuchAlgorithmException {
        char choice = 0;
        if(!users.isEmpty()) {
            System.out.println(
                    '\n' +
                            "User Manager\n" +
                            "Choose what you want to do\n" +
                            "Your current user is: " + users.get(currentUser).getUsername() + '\n' +
                            "1 - Create User\n" +
                            "2 - Delete User\n" +
                            "3 - User Info\n" +
                            "4 - Purchase History\n" +
                            "5 - User Modification\n" +
                            "6 - Change Current User\n" +
                            "7 - Exit\n"
            );
            System.out.print("Choice: ");
            try {
                choice = bufferedReader.readLine().charAt(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            UserCreation();
        }
            return choice;
    }

    public void UserCreation() throws IOException, NoSuchAlgorithmException {
        PasswordManager passwordManager = PasswordManager.getInstance();
        UserData newUser = new UserData();
        Date date = new Date(System.currentTimeMillis());
        System.out.println("UserCreation");
        System.out.print('\n' + "Enter username: ");
        String username = bufferedReader.readLine();
        newUser.setUsername(username);
        System.out.print("Enter password: ");
        String plainPassword = bufferedReader.readLine();
        String salt = passwordManager.generateSalt();
        newUser.setSalt(salt);
        String hashedPassword = passwordManager.hashPassword(plainPassword, salt);
        newUser.setHashedPassword(hashedPassword);
        newUser.setDateOfCreation(date);
        newUser.setDateOfModification(date);

        newUser.setId(UserData.getUserCount());

        UserData.incrementUserCount();
        users.add(newUser);
        SaveUsers(users);
    }

    public boolean UserLogin() throws IOException, NoSuchAlgorithmException {
        Shop shop = new Shop();

        PasswordManager passwordManager = PasswordManager.getInstance();
        System.out.println("UserLogin");
        System.out.print("\nEnter your username: ");
        String username = bufferedReader.readLine();
        System.out.print("Enter your password: ");
        String password = bufferedReader.readLine();

        UserData user = users.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);

        if (user != null) {
            String hashedPassword = passwordManager.hashPassword(password, user.getSalt());
            if (passwordManager.verifyPassword(password, user.getHashedPassword(), user.getSalt())) {
                currentUser = users.indexOf(user);
                System.out.println("Login successful");
                return true;
            }
        }
        System.out.println("Invalid username or password");
        shop.ShopMenu();
        return false;
    }

    public void UserChanger() throws IOException, NoSuchAlgorithmException {
        if(users.size() > 1) {
            UserLogin();
        } else if (users.size() == 1) {
            System.out.println("\nThere is only one user available at this moment");
        }else {
            System.out.println("\nThere are no users");
        }
    }

    public void UserDel() throws IOException {
        char delChoice;
        if(users.size() >= 2){
            System.out.print("\nAre you sure you want to delete user " + users.get(currentUser).getUsername() + "?(Y/N)");
            delChoice = bufferedReader.readLine().toLowerCase().charAt(0);
            if(delChoice == 'y'){
                users.remove(currentUser);
                SaveUsers(users);
                System.out.println("User has been deleted");
                currentUser -= 1;
            }else{
                return;
            }
        } else if (users.size() == 1) {
            System.out.println("\nYou need to have at least one user, you cant delete this one");
        } else {
            System.out.println("\nThere are no current users");
        }
        users.clear();
    }
    public void UserInfo() {
        if (users.size() > 0) {
            System.out.println("\n" +
                    "User Information:\n" +
                    "Username: " + users.get(currentUser).getUsername() + "\n" +
                    "Date of Creation: " + users.get(currentUser).getDateOfCreation() + "\n" +
                    "Date of Modification: " + users.get(currentUser).getDateOfModification());
        }else{
            System.out.println("There are no users");
        }
    }

    public void UserModification() throws IOException, NoSuchAlgorithmException {
        char userModificationChoice;
        do {
            System.out.println(
                    "UserModification\n" +
                    "1 - Change your Username\n" +
                    "2 - Change your Password\n"
            );
            userModificationChoice = bufferedReader.readLine().charAt(0);
            Date date = new Date(System.currentTimeMillis());
            switch (userModificationChoice){
                case '1':
                    System.out.print("Enter new username: ");
                    String newUsername = bufferedReader.readLine();
                    users.get(currentUser).setUsername(newUsername);
                    users.get(currentUser).setDateOfModification(date);
                    SaveUsers(users);
                    break;
                case '2':
                    System.out.print("Enter new password: ");
                    String newPlainPassword = bufferedReader.readLine();
                    String salt = PasswordManager.getInstance().generateSalt();
                    users.get(currentUser).setSalt(salt);
                    String hashedPassword = PasswordManager.getInstance().hashPassword(newPlainPassword, salt);
                    users.get(currentUser).setHashedPassword(hashedPassword);
                    users.get(currentUser).setDateOfModification(date);
                    SaveUsers(users);
                    break;
                default:
                    System.out.println("Invalid input");
                    break;
            }
        }while(userModificationChoice != '1' && userModificationChoice != '2');
    }

    public void UserAddToPurchaseHistory(OrderData order){
        users.get(currentUser).getOrders().add(order);
        SaveUsers(users);
    }

    public void UserShowPurchaseHistory(){
        List<OrderData> userOrders = users.get(currentUser).getOrders();
        if(!userOrders.isEmpty()){
            for(OrderData order : userOrders){
                System.out.println(
                        "Order ID: " + order.getOrderId() +
                        "\nProduct Name: " + order.getProduct() +
                        "\nQuantity: " + order.getQuantity() +
                        "\nPrice: " + order.getSubtotal() +
                        "\nAddress: " + order.getAddress() +
                        "\nOrder Date: " + order.getOrderDate() +
                        "\nOrder Status: " + order.getStatus() + '\n');

            }
        }else {
            System.out.println("You have no orders");
        }
    }
}
