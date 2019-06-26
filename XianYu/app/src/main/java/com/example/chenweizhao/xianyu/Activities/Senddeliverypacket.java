package com.example.chenweizhao.xianyu.Activities;

import java.io.Serializable;

public class Senddeliverypacket implements Serializable {

    public Senddeliverypacket(String task_sketch, int task_bonus, String delivery_detail) {
        task = new Task(task_sketch, task_bonus);
        delivery = new Delivery(delivery_detail);
    }

    Task task;
    Delivery delivery;

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Delivery getDelivery() {
        return delivery;
    }

    public Task getTask() {
        return task;
    }

    class Task {
        int task_type;
        int task_bonus;
        String task_sketch;
        public Task(String task_sketch, int task_bonus){
            this.task_bonus = task_bonus;
            this.task_sketch = task_sketch;
            this.task_type = 0;
        }

        public void setTask_bonus(int task_bonus) {
            this.task_bonus = task_bonus;
        }

        public void setTask_sketch(String task_sketch) {
            this.task_sketch = task_sketch;
        }

        public void setTask_type(int task_type) {
            this.task_type = task_type;
        }

        public int getTask_bonus() {
            return task_bonus;
        }

        public int getTask_type() {
            return task_type;
        }

        public String getTask_sketch() {
            return task_sketch;
        }
    }

    class Delivery {
        public Delivery(String delivery_detail) {
            this.delivery_detail = delivery_detail;
        }
        String delivery_detail;
        public void setDelivery_detail(String delivery_detail) {
            this.delivery_detail = delivery_detail;
        }

        public String getDelivery_detail() {
            return delivery_detail;
        }
    }
}
