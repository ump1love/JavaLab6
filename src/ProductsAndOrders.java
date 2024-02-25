import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class ProductsAndOrders {

    private static ProductsAndOrders productsAndOrders;
    private ProductsAndOrders(){}
    public static ProductsAndOrders getInstance(){
        if(productsAndOrders == null){
            productsAndOrders = new ProductsAndOrders();
        }
        return productsAndOrders;
    }
    private String filepathOrder = "orders.json";
    private String filepathProduct = "products.json";
    public List<ProductData> products = new ArrayList<>();
    public List<OrderData> orders = new ArrayList<>();

    public void SaveOrders(List<OrderData> products) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            FileWriter fileWriter = new FileWriter(filepathOrder);
            gson.toJson(products, fileWriter);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void LoadOrders() {
        Gson gson = new Gson();
        try (BufferedReader reader = new BufferedReader(new FileReader(filepathOrder))) {
            List<OrderData> loadedOrders = gson.fromJson(reader, new TypeToken<ArrayList<OrderData>>() {}.getType());
            orders.addAll(loadedOrders);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void SaveProducts(List<ProductData> products) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            FileWriter fileWriter = new FileWriter(filepathProduct);
            gson.toJson(products, fileWriter);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void LoadProducts() {
        Gson gson = new Gson();
        try (BufferedReader reader = new BufferedReader(new FileReader(filepathProduct))) {
            List<ProductData> loadedProducts = gson.fromJson(reader, new TypeToken<ArrayList<ProductData>>() {}.getType());
            products.addAll(loadedProducts);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void AddProducts() {
        AddProductsSave("Laptop",       ProductData.Category.Electronics,   "Powerful, sleek, versatile computing for work and play",               1000,   1000);
        AddProductsSave("Phone",        ProductData.Category.Electronics,   "Pocket-sized powerhouse, connecting you to the world",                 400,    1000);
        AddProductsSave("Candy",        ProductData.Category.Food,          "Sweet indulgence, a burst of flavor in every bite",                    1,      1000);
        AddProductsSave("Book",         ProductData.Category.Literature,    "Riveting tales await within these printed pages",                      20,     1000);
        AddProductsSave("Backpack",     ProductData.Category.Clothes,       "Durable, spacious, your trusty companion for adventures and commutes", 100,    1000);
        AddProductsSave("Sunglasses",   ProductData.Category.Clothes,       "Stylish protection, shielding your eyes with flair",                   10,     1000);
    }

    public void AddProductsSave(String name, ProductData.Category category, String description, int price, int quantity) {
        Optional<ProductData> existingProduct = products.stream()
                .filter(p -> p.getName().equalsIgnoreCase(name))
                .findFirst();
        if(existingProduct.isPresent()){
            ProductData product = existingProduct.get();
            product.setCategory(category);
            product.setDescription(description);
            product.setPrice(price);
            product.setQuantity(quantity);
        } else {
            String nameShop = name.toLowerCase();
            ProductData newProduct = new ProductData();
            newProduct.setName(name);
            newProduct.setNameShop(nameShop);
            newProduct.setProductId(products.size() + 1);
            newProduct.setCategory(category);
            newProduct.setDescription(description);
            newProduct.setPrice(price);
            newProduct.setQuantity(quantity);

            products.add(newProduct);
        }

        SaveProducts(products);
    }

}
