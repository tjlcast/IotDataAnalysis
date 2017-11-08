package com.tjlcast.Device;

import java.io.Serializable;

/**
 * Created by tangjialiang on 2017/11/7.
 */
public class AvgDeviceValueData implements Serializable {

    private double data ;

    public double getData() {
        return data;
    }

    public void setData(double data) {
        this.data = data;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    private int count ;

    public AvgDeviceValueData(double data) {
        this.data = data ;
        this.count = 1 ;
    }

    public AvgDeviceValueData(double data, int count) {
        this.data = data ;
        this.count = count ;
    }

    public double getAvgValue() {
        return data / count ;
    }

    public static AvgDeviceValueData sum(AvgDeviceValueData a, AvgDeviceValueData b) {
        return new AvgDeviceValueData(a.getData() + b.getData(), a.getCount()+b.getCount()) ;
    }
}
