import java.io.Serializable;

public class Get_user_balance_confirm implements Serializable {
    int code;
    String message;
    double user_balance;

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setUser_balance(double user_balance) {
        this.user_balance = user_balance;
    }

    public double getUser_balance() {
        return user_balance;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}