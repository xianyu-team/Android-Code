import java.io.Serializable;
import java.util.ArrayList;
public class Get_all_answered_questionnaire_confirm_packet implements Serializable {
    int code;
    String message;
    ArrayList<statistics> statistics;

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setStatistics(ArrayList<statistics> statistics) {
        this.statistics = statistics;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }

    public ArrayList<statistics> getStatistics() {
        return statistics;
    }
}