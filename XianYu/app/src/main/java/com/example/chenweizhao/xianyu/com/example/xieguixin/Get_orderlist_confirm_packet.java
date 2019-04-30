import java.io.Serializable;
import java.util.ArrayList;

public class Get_orderlist_confirm_packet implements Serializable {
    int code;
    String message;
    ArrayList<order> orders;

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setOrders(ArrayList<order> orders) {
        this.orders = orders;
    }

    public ArrayList<order> getOrders() {
        return orders;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}