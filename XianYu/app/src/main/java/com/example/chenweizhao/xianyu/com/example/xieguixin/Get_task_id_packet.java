import java.io.Serializable;

public class Get_task_id_packet implements Serializable {
    int task_id;

    public void setTask_id(int task_id) {
        this.task_id = task_id;
    }

    public int getTask_id() {
        return task_id;
    }
}