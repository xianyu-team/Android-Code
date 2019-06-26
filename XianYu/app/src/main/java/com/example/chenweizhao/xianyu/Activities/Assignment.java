package com.example.chenweizhao.xianyu.Activities;

import java.io.Serializable;
import java.util.List;

public class Assignment implements Serializable {

    /**
     * code : 200
     * message : string
     * data : {"tasks":[{"task_id":20,"user_id":20,"task_type":20,"task_sketch":"string","task_bonus":200,"task_publishDate":"string"}]}
     */

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
        private List<TasksBean> tasks;

        public List<TasksBean> getTasks() {
            return tasks;
        }

        public void setTasks(List<TasksBean> tasks) {
            this.tasks = tasks;
        }

        public static class TasksBean {
            /**
             * task_id : 20
             * user_id : 20
             * task_type : 20
             * task_sketch : string
             * task_bonus : 200
             * task_publishDate : string
             */

            private int task_id;
            private int user_id;
            private int task_type;
            private String task_sketch;
            private int task_bonus;
            private String task_publishDate;

            public int getTask_id() {
                return task_id;
            }

            public void setTask_id(int task_id) {
                this.task_id = task_id;
            }

            public int getUser_id() {
                return user_id;
            }

            public void setUser_id(int user_id) {
                this.user_id = user_id;
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
    }
}
