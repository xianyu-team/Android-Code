import java.io.Serializable;
import java.util.ArrayList;
public class Get_answered_questionnaire_confirm implements Serializable {
    int code;
    String message;
    ArrayList<answerSheet> answerSheet;

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setAnswerSheet(ArrayList<answerSheet> answerSheet) {
        this.answerSheet = answerSheet;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<answerSheet> getAnswerSheet() {
        return answerSheet;
    }
}