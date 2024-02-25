import java.io.IOException;
import java.security.NoSuchAlgorithmException;

class Program
{
    public static void main(String[] args) {
        Simulation simulation = new Simulation();
        ProductsAndOrders productsAndOrders = ProductsAndOrders.getInstance();

        productsAndOrders.LoadProducts();
        productsAndOrders.LoadOrders();

        try {
            simulation.ShopSimulation();
        } catch (IOException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}