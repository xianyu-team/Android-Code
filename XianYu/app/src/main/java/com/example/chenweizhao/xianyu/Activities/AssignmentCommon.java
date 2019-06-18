package com.example.chenweizhao.xianyu.Activities;

import java.io.Serializable;

public class AssignmentCommon implements Serializable {

    /**
     * user_id : 20
     * task_type : 20
     * task_sketch : string
     * task_bonus : 20
     * task_publishDate : string
     */

    private int user_id;
    private int task_id;
    private int task_type;
    private String task_sketch;
    private int task_bonus;
    private String task_publishDate;

    AssignmentCommon (int user_id, int task_id, int task_type, String task_sketch, int task_bonus, String task_publishDate) {
        setUser_id(user_id);
        setTask_id(task_id);
        setTask_type(task_type);
        setTask_sketch(task_sketch);
        setTask_bonus(task_bonus);
        setTask_publishDate(task_publishDate);
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getTask_id() {
        return task_id;
    }

    public void setTask_id(int task_id) {
        this.task_id = task_id;
    }

    public int getTask_type() {
        return task_type;
    }

    public void setTask_type(int task_type) {
        this.task_type = task_type;
    }

    public String getTask_sketch() {
        return task_sketch;
    }

    public void setTask_sketch(String task_sketch) {
        this.task_sketch = task_sketch;
    }

    public int getTask_bonus() {
        return task_bonus;
    }

    public void setTask_bonus(int task_bonus) {
        this.task_bonus = task_bonus;
    }

    public String getTask_publishDate() {
        return task_publishDate;
    }

    public void setTask_publishDate(String task_publishDate) {
        this.task_publishDate = task_publishDate;
    }
}
