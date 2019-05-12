
import java.io.Serializable;

public class Get_user_info_confirm_packet implements Serializable {
    int code;
    String message;
    User user;
    Student student;
    public void setCode(int code) {
        this.code = code;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Student getStudent() {
        return student;
    }

    public User getUser() {
        return user;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}