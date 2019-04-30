import java.io.Serializable;

public class Accept_or_delete_order_packet implements Serializable {
   int order_id;

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getOrder_id() {
        return order_id;
    }
}