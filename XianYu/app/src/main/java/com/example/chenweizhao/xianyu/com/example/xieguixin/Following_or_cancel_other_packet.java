import java.io.Serializable;
import java.util.ArrayList;
public class Following_or_cancel_other_packet implements Serializable {
    int user_id;

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getUser_id() {
        return user_id;
    }
}