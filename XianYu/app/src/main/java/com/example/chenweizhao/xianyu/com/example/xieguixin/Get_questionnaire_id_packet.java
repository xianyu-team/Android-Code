import java.io.Serializable;

public class Get_questionnaire_id_packet implements Serializable {
    int questionnaire_id;

    public void setQuestionnaire_id(int questionnaire_id) {
        this.questionnaire_id = questionnaire_id;
    }

    public int getQuestionnaire_id() {
        return questionnaire_id;
    }
}