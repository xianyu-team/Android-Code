

import java.io.Serializable;

public class Get_phone_verification_packet implements Serializable {
    String user_phone;

    Get_phone_verification_packet(String user_phone) {
        this.user_phone=user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getUser_phone() {
        return user_phone;
    }
}