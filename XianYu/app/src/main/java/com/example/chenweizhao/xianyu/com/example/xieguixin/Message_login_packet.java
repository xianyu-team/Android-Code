import java.io.Serializable;

public class Message_login_packet implements Serializable {
    String user_phone;
    String verification_code;

    public void setVerification_code(String verification_code) {
        this.verification_code = verification_code;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getVerification_code() {
        return verification_code;
    }

    public String getUser_phone() {
        return user_phone;
    }
}