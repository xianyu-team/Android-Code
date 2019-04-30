import java.io.Serializable;

public class Change_user_info_packet implements Serializable{
    String user_icon;
    String user_name;
    String user_school;
    String user_academy;
    String user_number;
    int user_gender//0为女，1为男

    Change_user_info_packet(String user_icon,String user_name,
                            String user_school,String user_academy,String user_number,int user_gender){
        this.user_icon=user_icon;
        this.user_name=user_name;
        this.user_school=user_school;
        this.user_academy=user_academy;
        this.user_number=user_number;
        this.user_gender=user_gender;
    }

    public void setUser_academy(String user_academy) {
        this.user_academy = user_academy;
    }

    public void setUser_icon(String user_icon) {
        this.user_icon = user_icon;
    }

    public void setUser_number(String user_number) {
        this.user_number = user_number;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setUser_school(String user_school) {
        this.user_school = user_school;
    }

    public void setUser_gender(int user_gender) {
        this.user_gender = user_gender;
    }

    public int getUser_gender() {
        return user_gender;
    }

    public String getUser_academy() {
        return user_academy;
    }

    public String getUser_icon() {
        return user_icon;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getUser_number() {
        return user_number;
    }

    public String getUser_school() {
        return user_school;
    }
}
