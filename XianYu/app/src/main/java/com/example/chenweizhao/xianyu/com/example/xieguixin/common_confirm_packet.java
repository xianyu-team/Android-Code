

import java.io.Serializable;

public class common_confirm_packet implements Serializable {
    int code;
    String message;

    public void setCode(int code) {
        this.code=code;
    }
    public void setMessage(String message){
        this.message=message;
    }
    public String getMessage(){
        return message;
    }
    public int getCode(){
        return code;
    }

}