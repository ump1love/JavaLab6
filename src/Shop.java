
import java.io.*;
import java.security.NoSuchAlgorithmException;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class Shop {
    private BufferedReader bufferedReader;
    private boolean loginSuccessful = false;
    {
        bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    }
    User user = new User();
    ProductsAndOrders productsAndOrders = ProductsAndOrders.getInstance();

    public String ShopMenu() {
        String shopMenuChoice;
        if(loginSuccessful){
            System.out.print("\nYour current user is: " + user.users.get(user.currentUser).getUsername());
        }else{
            System.out.print("\nCurrently you are not signed in");
        }

        System.out.println(
                "\nWelcome to our anti-GUI shop!\n" +
                "Our shop uses a terminal as a navigation panel, " +
                "type \"help\" to get all available commands.\n"
        );
        try {
            shopMenuChoice = bufferedReader.readLine().toLowerCase();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return shopMenuChoice;
    }

    public void ShopHelp() {
        System.out.println(
                "\nUser - Opens user manager\n" +
                "Products - List of all available products\n" +
                "Search - search system\n" +
                "Exit - For exit\n"
        );
    }

    public void ShopUserEnterChoice(){
        user.LoadUsers();
        String shopUserEnterChoice;
        if(!user.users.isEmpty()){
            System.out.println("What do you want to do? (Login/Sign up)");
            do {
                try {
                    shopUserEnterChoice = bufferedReader.readLine().toLowerCase();
                    ShopUserEnter(shopUserEnterChoice);
                } catch (IOException | NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }

            }while(!Objects.equals(shopUserEnterChoice, "login") && !Objects.equals(shopUserEnterChoice, "log in")
                    && !Objects.equals(shopUserEnterChoice, "sign up") && !Objects.equals(shopUserEnterChoice, "signup"));
        }
        else{
            System.out.println("You need to have at least one user to enter user menu");
            try {
                ShopUserEnter("sign up");
            } catch (IOException | NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void ShopUserEnter(String choice) throws IOException, NoSuchAlgorithmException {
        switch (choice){
            case "login":
            case "log in":
                loginSuccessful = user.UserLogin();
                ShopUserMenu(loginSuccessful);
                break;
            case "sign up":
            case "signup":
                user.UserCreation();
                break;
        }
    }

    public void ShopUserMenu(boolean loginSuccessful) throws IOException, NoSuchAlgorithmException {
        if (loginSuccessful = true){
            char userMenu;
            do {
                userMenu = user.UserMenu();

                switch (userMenu){
                    case '1':
                        user.UserCreation();
                        break;
                    case '2':
                        user.UserDel();
                        break;
                    case '3':
                        user.UserInfo();
                        break;
                    case '4':
                        user.UserShowPurchaseHistory();
                        break;
                    case '5':
                        user.UserModification();
                        break;
                    case '6':
                        user.UserChanger();
                        break;
                    case '7':
                        break;
                    default:
                        System.out.println("Invalid input");
                        break;
                }
            }while (userMenu != '7');

        }
    }

    public void ShopAllProducts(){
        if(!productsAndOrders.products.isEmpty()){
            System.out.println("There are currently " + productsAndOrders.products.size() + " products");
            for(ProductData product : productsAndOrders.products){
                System.out.println(product.getProductId() + " " + product.getName());
            }
            System.out.println(
                    "\nIf you want to select an item and see its description, price, etc.\n" +
                    "Just type this item name or its ID you can also type \"exit\" for exit");
            String productChoice;
            try {
                productChoice = bufferedReader.readLine().toLowerCase();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Optional<ProductData> selectedProduct = ProductsAndOrders.getInstance().products.stream()
                    .filter(p -> p.getNameShop().equalsIgnoreCase(productChoice) || String.valueOf(p.getProductId()).equals(productChoice))
                    .findFirst();
            if(selectedProduct.isPresent()){
                ShopDisplayProduct(selectedProduct.get());
            } else if (Objects.equals(productChoice, "exit")) {
                return;
            }else {
                System.out.println("Invalid item choice");
                ShopAllProducts();
            }
        }else{
            System.out.println("There are currently no products available");
        }
    }

    public void ShopDisplayProduct(ProductData product){
        System.out.println(
                "Product Name: " + product.getName() +
                "\nProduct Description: " + product.getDescription() +
                "\nProduct Category: " + product.getCategory() +
                "\nProduct Price: " + product.getPrice() +
                "\nProduct Quantity: " + product.getQuantity());
        if(loginSuccessful) {
            if (product.getQuantity() > 0) {
                System.out.println("You can order items from this menu, just type \"order\" to do so or exit to go back.");
                String productOrderChoice;
                try {
                    productOrderChoice = bufferedReader.readLine().toLowerCase();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                if (productOrderChoice.equals("order")) {
                    ShopOrder(product);
                } else if (productOrderChoice.equals("exit")) {
                    ShopAllProducts();
                } else {
                    System.out.println("Invalid input");
                    ShopAllProducts();
                }
            } else {
                System.out.println("Sorry, but we run out of " + product.getName());
                ShopAllProducts();
            }
        }else {
            System.out.println("\nYou need to be logged in to be able to order anything");
        }
    }

    public void ShopOrder(ProductData product){
        OrderData newOrder = new OrderData();
        newOrder.setProduct(product.getName());
        System.out.print(
                "Welcome to order menu.\n" +
                "Product: " + product.getName() +
                "\nEnter how many do you want to order(you can enter \"0\" if you want to exit): ");
        int orderQuantity = 0;
        try{
            orderQuantity = Integer.parseInt(bufferedReader.readLine());
        }catch (Exception i){
            System.out.println("Invalid quantity input");
        }
        if(orderQuantity <= product.getQuantity() && orderQuantity != 0){
            newOrder.setQuantity(orderQuantity);
        } else if (orderQuantity > product.getQuantity()) {
            System.out.println("We don't have enough of " + product.getName() + "(s) currently.");
            return;
        }else {
            System.out.println("Invalid quantity input");
            return;
        }
        char addressConfirmation = '0';
        do {
            System.out.print("Please enter your address: ");
            String orderAddress = "";
            try {
                orderAddress = bufferedReader.readLine();
            } catch (Exception i) {
                System.out.println("\nInvalid address input");
            }
            System.out.print("\nYour address is: " + orderAddress + "?(Y/N)" );
            try {
                addressConfirmation = bufferedReader.readLine().toLowerCase().charAt(0);
            } catch (Exception i) {
                System.out.println("Invalid address confirmation");
            }
            if (addressConfirmation == 'y'){
                newOrder.setAddress(orderAddress);
            } else if (addressConfirmation == 'n') {
                System.out.println("Try again");
            }else{
                System.out.println("Invalid address confirmation");
            }
        }while(addressConfirmation != 'y');
        char orderConfirmation = '0';
        do {
            System.out.print(
                        "You want to order " + product.getName() + '\n' +
                        "In amount of " + newOrder.getQuantity() + '\n' +
                        "Price: " + newOrder.getQuantity() * product.getPrice() +
                        "\nProceed? (Y/N)");
            try {
                orderConfirmation = bufferedReader.readLine().toLowerCase().charAt(0);
            } catch (IOException e) {
                System.out.println("Invalid order confirmation");
            }
            if (orderConfirmation == 'n'){
                return;
                }

        }while(orderConfirmation !='y');

        product.setQuantity(product.getQuantity() - orderQuantity);
        productsAndOrders.SaveProducts(productsAndOrders.products);

        Date date = new Date(System.currentTimeMillis());

        newOrder.setSubtotal(newOrder.getQuantity() * product.getPrice());
        newOrder.setOrderId(ProductsAndOrders.getInstance().orders.size() + 1);
        newOrder.setOrderDate(date);
        newOrder.setStatus(OrderData.Delivery.Pending);

        ProductsAndOrders.getInstance().orders.add(newOrder);

        user.UserAddToPurchaseHistory(newOrder);

        ProductsAndOrders.getInstance().SaveOrders(ProductsAndOrders.getInstance().orders);
        System.out.println(
                "You successfully ordered " + product.getName() +
                "\nCheck user purchase history for more information");
    }

    public void ShopSearch() {
        System.out.println("Enter search criteria:\n" +
                "1 - Price\n" +
                "2 - Category");
        int searchOption;
        try {
            searchOption = Integer.parseInt(bufferedReader.readLine());
            switch (searchOption) {
                case 1:
                    SearchByPrice();
                    break;
                case 2:
                    SearchByCategory();
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Invalid input");
        }
    }

    private void SearchByPrice() {
        System.out.print("Enter maximum price: ");
        try {
            int maxPrice = Integer.parseInt(bufferedReader.readLine());
            var results = productsAndOrders.products.stream()
                    .filter(p -> p.getPrice() <= maxPrice)
                    .collect(Collectors.toList());
            DisplaySearchResults(results);
        } catch (IOException | NumberFormatException e) {
            System.out.println("Invalid input");
        }
    }

    private void SearchByCategory() {
        System.out.println("Available categories:");
        for (ProductData.Category category : ProductData.Category.values()) {
            System.out.println(category.name());
        }
        System.out.print("Enter category: ");
        try {
            String category = bufferedReader.readLine();
            var results = productsAndOrders.products.stream()
                    .filter(p -> p.getCategory().name().equalsIgnoreCase(category))
                    .collect(Collectors.toList());
            DisplaySearchResults(results);
        } catch (IOException e) {
            System.out.println("Invalid input");
        }
    }

    private void DisplaySearchResults(List<ProductData> results) {
        if (!results.isEmpty()) {
            System.out.println("Search results:");
            for (ProductData product : results) {
                System.out.println("Product ID: " + product.getProductId() + "\n" +
                        "Name: " + product.getName() + "\n" +
                        "Price: " + product.getPrice() + "\n");
            }
        } else {
            System.out.println("No matching products found.");
        }
    }
}
