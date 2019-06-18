package com.example.chenweizhao.xianyu.Activities;

import java.io.Serializable;

public class User_profile_packet implements Serializable {

    /**
     * code : 200
     * message : OK
     * data : {"user":{"user_id":100,"user_phone":"132343","user_icon":"1233534","user_balance":100,"user_fillln":0},"student":{"student_id":100,"user_id":100,"student_number":"1234","student_name":"1234","student_university":"1234","student_academy":"1234","student_gender":0}}
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
        /**
         * user : {"user_id":100,"user_phone":"132343","user_icon":"1233534","user_balance":100,"user_fillln":0}
         * student : {"student_id":100,"user_id":100,"student_number":"1234","student_name":"1234","student_university":"1234","student_academy":"1234","student_gender":0}
         */

        private UserBean user;
        private StudentBean student;

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public StudentBean getStudent() {
            return student;
        }

        public void setStudent(StudentBean student) {
            this.student = student;
        }

        public static class UserBean {
            /**
             * user_id : 100
             * user_phone : 132343
             * user_icon : 1233534
             * user_balance : 100
             * user_fillln : 0
             */

            private int user_id;
            private String user_phone;
            private String user_icon;
            private int user_balance;
            private int user_fillln;

            public int getUser_id() {
                return user_id;
            }

            public void setUser_id(int user_id) {
                this.user_id = user_id;
            }

            public String getUser_phone() {
                return user_phone;
            }

            public void setUser_phone(String user_phone) {
                this.user_phone = user_phone;
            }

            public String getUser_icon() {
                return user_icon;
            }

            public void setUser_icon(String user_icon) {
                this.user_icon = user_icon;
            }

            public int getUser_balance() {
                return user_balance;
            }

            public void setUser_balance(int user_balance) {
                this.user_balance = user_balance;
            }

            public int getUser_fillln() {
                return user_fillln;
            }

            public void setUser_fillln(int user_fillln) {
                this.user_fillln = user_fillln;
            }
        }

        public static class StudentBean {
            /**
             * student_id : 100
             * user_id : 100
             * student_number : 1234
             * student_name : 1234
             * student_university : 1234
             * student_academy : 1234
             * student_gender : 0
             */

            private int student_id;
            private int user_id;
            private String student_number;
            private String student_name;
            private String student_university;
            private String student_academy;
            private int student_gender;

            public int getStudent_id() {
                return student_id;
            }

            public void setStudent_id(int student_id) {
                this.student_id = student_id;
            }

            public int getUser_id() {
                return user_id;
            }

            public void setUser_id(int user_id) {
                this.user_id = user_id;
            }

            public String getStudent_number() {
                return student_number;
            }

            public void setStudent_number(String student_number) {
                this.student_number = student_number;
            }

            public String getStudent_name() {
                return student_name;
            }

            public void setStudent_name(String student_name) {
                this.student_name = student_name;
            }

            public String getStudent_university() {
                return student_university;
            }

            public void setStudent_university(String student_university) {
                this.student_university = student_university;
            }

            public String getStudent_academy() {
                return student_academy;
            }

            public void setStudent_academy(String student_academy) {
                this.student_academy = student_academy;
            }

            public int getStudent_gender() {
                return student_gender;
            }

            public void setStudent_gender(int student_gender) {
                this.student_gender = student_gender;
            }
        }
    }
}
