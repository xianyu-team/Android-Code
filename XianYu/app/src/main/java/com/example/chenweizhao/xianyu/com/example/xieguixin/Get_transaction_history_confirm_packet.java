import java.io.Serializable;
import java.util.ArrayList;

public class Get_transaction_history_confirm_packet implements Serializable {
    int code;
    String message;
    ArrayList<bills> bill;

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setBill(ArrayList<bills> bill) {
        this.bill = bill;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }

    public ArrayList<bills> getBill() {
        return bill;
    }
}