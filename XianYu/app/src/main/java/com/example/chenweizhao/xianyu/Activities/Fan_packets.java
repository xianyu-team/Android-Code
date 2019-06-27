package com.example.chenweizhao.xianyu.Activities;

import java.io.Serializable;
import java.util.List;

public class Fan_packets implements Serializable {

    /**
     * code : 200
     * message : OK
     * data : {"fans":[{"fan_id":5},{"fan_id":12}]}
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
        private List<FansBean> fans;

        public List<FansBean> getFans() {
            return fans;
        }

        public void setFans(List<FansBean> fans) {
            this.fans = fans;
        }

        public static class FansBean {
            /**
             * fan_id : 5
             */

            private int fan_id;

            public int getFan_id() {
                return fan_id;
            }

            public void setFan_id(int fan_id) {
                this.fan_id = fan_id;
            }
        }
    }
}
