
import java.io.Serializable;

public class Login_confirm_packet implements Serializable {
    int code;
    String message;
    int user_fillln;

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setUser_fillln(int user_fillln) {
        this.user_fillln = user_fillln;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }

    public int getUser_fillln() {
        return user_fillln;
    }
}