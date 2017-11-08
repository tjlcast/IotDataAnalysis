package com.tjlcast.DataSource;

import org.apache.spark.streaming.api.java.JavaInputDStream;

/**
 * Created by tangjialiang on 2017/11/7.
 */
public abstract class Source {
    public abstract JavaInputDStream getStream() ;
}
