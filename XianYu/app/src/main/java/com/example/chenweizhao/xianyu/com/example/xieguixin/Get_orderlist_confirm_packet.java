import java.io.Serializable;
import java.util.ArrayList;

public class Get_orderlist_confirm_packet implements Serializable {
    int code;
    String message;
    ArrayList<Tasks> tasks;

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setTasks(ArrayList<Tasks> tasks) {
        this.tasks = tasks;
    }

    public ArrayList<Tasks> getTasks() {
        return tasks;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}