import java.io.Serializable;

public class Get_user_balance_confirm implements Serializable {
    int code;
    String message;
    int user_balance;

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setUser_balance(int user_balance) {
        this.user_balance = user_balance;
    }

    public int getUser_balance() {
        return user_balance;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}