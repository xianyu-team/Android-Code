import java.io.Serializable;

public class Get_questionnaire_detail_confirm_packet implements Serializable {
    int code;
    String message;
    Questionnaire questionnaire;

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setQuestionnaire(Questionnaire questionnaire) {
        this.questionnaire = questionnaire;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }

    public Questionnaire getQuestionnaire() {
        return questionnaire;
    }
}