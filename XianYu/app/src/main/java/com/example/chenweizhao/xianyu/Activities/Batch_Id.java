package com.example.chenweizhao.xianyu.Activities;

import java.io.Serializable;
import java.util.List;

public class Batch_Id implements Serializable {
    private List<UserIdsBean> user_ids;

//    Batch_Id(List<UserIdsBean> userIdsBeans) {
//        user_ids = new ArrayList<>();
//        setUser_ids(userIdsBeans);
//    }

    public List<UserIdsBean> getUser_ids() {
        return user_ids;
    }

    public void setUser_ids(List<UserIdsBean> user_ids) {
        this.user_ids = user_ids;
    }

    public static class UserIdsBean {
        /**
         * user_id : 1
         */
//        UserIdsBean(int user_id) {
//            setUser_id(user_id);
//        }

        private int user_id;

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }
    }
}
