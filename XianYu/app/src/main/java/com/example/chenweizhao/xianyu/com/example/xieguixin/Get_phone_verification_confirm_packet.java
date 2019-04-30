

import java.io.Serializable;

public class Get_phone_verification_confirm_packet implements Serializable {
    int code;
    String message;
    String verification_code;

    Get_phone_verification_confirm_packet(int code,String message,String verification_code){
        this.code=code;
        this.message=message;
        this.verification_code=verification_code;
    }
    Get_phone_verification_confirm_packet(int code,String message){
        this.code=code;
        this.message=message
    }
    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getVerification_code() {
        return verification_code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setVerification_code(String verification_code) {
        this.verification_code = verification_code;
    }
}