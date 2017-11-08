package com.tjlcast.Device;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by tangjialiang on 2017/11/7.
 *
 */
public class DeviceData implements Serializable {
    // {"uid":"922291","data":0,"current_time":"2017-11-06 17:44:45"}

    private String uid ;
    private double data ;
    private Date current_time ;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public double getData() {
        return data;
    }

    public void setData(double data) {
        this.data = data;
    }

    public Date getCurrent_time() {
        return current_time;
    }

    public void setCurrent_time(Date current_time) {
        this.current_time = current_time;
    }
}
