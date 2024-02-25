import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserData implements Serializable
{
     private int id;
     private static int userCount = 0;
     private String username;
     private String salt;
     private String hashedPassword;
     private Date dateOfCreation;
     private Date dateOfModification;
     private List<OrderData> orders;


    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public static int getUserCount() {
        return userCount;
    }
    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getSalt() {
        return salt;
    }
    public void setSalt(String salt) {
        this.salt = salt;
    }
    public String getHashedPassword() {
        return hashedPassword;
    }
    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }
    public Date getDateOfCreation() {
        return dateOfCreation;
    }
    public void setDateOfCreation(Date dateOfCreation){
        this.dateOfCreation = dateOfCreation;
    }
    public Date getDateOfModification() {
        return dateOfModification;
    }
    public void setDateOfModification(Date dateOfModification) {
        this.dateOfModification = dateOfModification;
    }



    public void setOrders(List<OrderData> orders) {
        this.orders = orders;
    }

    public List<OrderData> getOrders() {
        if (orders == null) {
            orders = new ArrayList<>();
        }
        return orders;
    }

    public static void incrementUserCount() {
        userCount++;
    }
}
