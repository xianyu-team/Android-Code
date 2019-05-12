import java.io.Serializable;
import java.util.ArrayList;

public class Get_user_followings_or_fans_confirm_packet implements Serializable {
    int code;
    String message;
    ArrayList<followings> follower;

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setFollower(ArrayList<followings> follower) {
        this.follower = follower;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }

    public ArrayList<followings> getFollower() {
        return follower;
    }
}