

import java.io.Serializable;

public class Register_packet implements Serializable {
    String user_phone;
    String user_password;
    String verification_code;

    Register_packet(String user_phone,String user_password,String verification_code) {
        this.user_phone = user_phone;
        this.user_password = user_password;
        this.verification_code = verification_code;
    }
    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }
    public void  setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public void setVerification_code(String verification_code) {
        this.verification_code = verification_code;
    }

    public String getUser_phone() {
        return user_phone;
    }
    public String getUser_password() {
        return user_password;
    }

    public String getVerification_code() {
        return verification_code;
    }
}