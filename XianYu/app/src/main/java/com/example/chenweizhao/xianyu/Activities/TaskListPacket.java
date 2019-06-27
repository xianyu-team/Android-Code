package com.example.chenweizhao.xianyu.Activities;

import java.io.Serializable;
import java.util.ArrayList;

public class TaskListPacket implements Serializable {
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
        ArrayList<TaskSketch> tasks;

        public ArrayList<TaskSketch> getTasks() {
            return tasks;
        }

        public void setTasks(ArrayList<TaskSketch> tasks) {
            this.tasks = tasks;
        }
    }


}
