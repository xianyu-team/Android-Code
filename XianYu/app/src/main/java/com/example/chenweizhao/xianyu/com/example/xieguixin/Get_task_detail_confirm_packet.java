import java.io.Serializable;

public class Get_task_detail_confirm_packet implements Serializable {
    int code;
    String message;
    Delivery delivery;

    public void setCode(int code) {
        this.code = code;
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Delivery getDelivery() {
        return delivery;
    }

    public int getCode() {
        return code;
    }



    public String getMessage() {
        return message;
    }
}