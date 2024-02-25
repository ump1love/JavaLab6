import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.NoSuchAlgorithmException;

class Simulation {
    private BufferedReader bufferedReader;
    {
        bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    }
    public void ShopSimulation() throws IOException, NoSuchAlgorithmException {

        User user = new User() ;
        Shop shop = new Shop();
        user.LoadUsers();

        String shopMenuChoice;

        while(true){
            shopMenuChoice = shop.ShopMenu();
            switch (shopMenuChoice){
                case "help":
                    shop.ShopHelp();
                    break;
                case "user":
                    shop.ShopUserEnterChoice();
                    break;
                case "products":
                    shop.ShopAllProducts();
                    break;
                case "search":
                    shop.ShopSearch();
                    break;
                case "exit":
                    System.out.println("Exiting...");
                    return;
                case "updateproducts":
                    System.out.println("Products have been updated");
                    ProductsAndOrders.getInstance().AddProducts();
                    break;
                default:
                    System.out.println("Invalid input");
                    break;

            }
        }

    }

}
