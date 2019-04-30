import java.io.Serializable;

public class Release_order_packet implements Serializable {
    String order_type;
    int order_bonus;
    String order_punlished_date;
    String order_description;
    boolean order_accepted;
    boolean finished;
    String order_finished_date;

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public void setOrder_accepted(boolean order_accepted) {
        this.order_accepted = order_accepted;
    }

    public void setOrder_bonus(int order_bonus) {
        this.order_bonus = order_bonus;
    }

    public void setOrder_description(String order_description) {
        this.order_description = order_description;
    }

    public void setOrder_finished_date(String order_finished_date) {
        this.order_finished_date = order_finished_date;
    }

    public void setOrder_punlished_date(String order_punlished_date) {
        this.order_punlished_date = order_punlished_date;
    }

    public void setOrder_type(String order_type) {
        this.order_type = order_type;
    }

    public int getOrder_bonus() {
        return order_bonus;
    }

    public String getOrder_description() {
        return order_description;
    }

    public String getOrder_finished_date() {
        return order_finished_date;
    }

    public String getOrder_punlished_date() {
        return order_punlished_date;
    }

    public String getOrder_type() {
        return order_type;
    }

    public boolean isFinished() {
        return finished;
    }

    public boolean isOrder_accepted() {
        return order_accepted;
    }
}