

import java.io.Serializable;

public class Login_or_register_packet implements Serializable {
    String user_phone;
    String user_password;

    Login_or_register_packet(String user_phone,String user_password) {
        this.user_phone = user_phone;
        this.user_password = user_password;
    }
    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }
    public void  setUser_password(String user_password) {
        this.user_password = user_password;
    }
    public String getUser_phone() {
        return user_phone;
    }
    public String getUser_password() {
        return user_password;
    }
}