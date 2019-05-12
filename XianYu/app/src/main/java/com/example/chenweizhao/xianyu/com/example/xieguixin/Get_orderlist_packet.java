import java.io.Serializable;

public class Get_orderlist_packet implements Serializable {
    int type;

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}