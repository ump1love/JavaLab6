import java.io.Serializable;
import java.util.Date;

public class OrderData implements Serializable {

    private int orderId;
    private int quantity;
    private double subtotal;
    private String address;
    private Date orderDate;
    private Delivery status;
    private String product;

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public Delivery getStatus() {
        return status;
    }

    public int getOrderId() {
        return orderId;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public String getProduct() {
        return product;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public void setStatus(Delivery status) {
        this.status = status;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public enum Delivery{
        Pending,
        Shipped,
        Delivered,
    }
}
