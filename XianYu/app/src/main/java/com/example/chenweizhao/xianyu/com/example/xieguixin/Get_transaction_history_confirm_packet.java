import java.io.Serializable;
import java.util.ArrayList;

public class Get_transaction_history_confirm_packet implements Serializable {
    int code;
    String message;
    ArrayList<Transaction> transactions;

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setTransactions(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }
}