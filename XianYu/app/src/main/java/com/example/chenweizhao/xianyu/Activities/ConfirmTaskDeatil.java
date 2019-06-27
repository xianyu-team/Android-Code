package com.example.chenweizhao.xianyu.Activities;

import java.io.Serializable;

public class ConfirmTaskDeatil implements Serializable {
    private int code;
    private String message;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        Delivery delivery;

        public void setDelivery(Delivery delivery) {
            this.delivery = delivery;
        }

        public Delivery getDelivery() {
            return delivery;
        }

        public class Delivery{
            int delivery_id;
            int task_id;
            String delivery_detail;
            int delivery_picked;
            int delivery_complished;
            String delivery_complishDate;

            public void setTask_id(int task_id) {
                this.task_id = task_id;
            }

            public void setDelivery_detail(String delivery_detail) {
                this.delivery_detail = delivery_detail;
            }

            public void setDelivery_complished(int delivery_complished) {
                this.delivery_complished = delivery_complished;
            }

            public void setDelivery_id(int delivery_id) {
                this.delivery_id = delivery_id;
            }

            public void setDelivery_picked(int delivery_picked) {
                this.delivery_picked = delivery_picked;
            }

            public void setDelivery_complishDate(String delivery_complishDate) {
                this.delivery_complishDate = delivery_complishDate;
            }

            public int getTask_id() {
                return task_id;
            }

            public String getDelivery_detail() {
                return delivery_detail;
            }

            public int getDelivery_complished() {
                return delivery_complished;
            }

            public int getDelivery_id() {
                return delivery_id;
            }

            public int getDelivery_picked() {
                return delivery_picked;
            }

            public String getDelivery_complishDate() {
                return delivery_complishDate;
            }
        }


    }
}
