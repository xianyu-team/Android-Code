package com.example.chenweizhao.xianyu.Activities;

import java.io.Serializable;
import java.util.List;

public class Following_packets implements Serializable {

    /**
     * code : 200
     * message : OK
     * data : {"followings":[{"following_id":5},{"following_id":12}]}
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
        private List<FollowingsBean> followings;

        public List<FollowingsBean> getFollowings() {
            return followings;
        }

        public void setFollowings(List<FollowingsBean> followings) {
            this.followings = followings;
        }

        public static class FollowingsBean {
            /**
             * following_id : 5
             */

            private int following_id;

            public int getFollowing_id() {
                return following_id;
            }

            public void setFollowing_id(int following_id) {
                this.following_id = following_id;
            }
        }
    }
}
