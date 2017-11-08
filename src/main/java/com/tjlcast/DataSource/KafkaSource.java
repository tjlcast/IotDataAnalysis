package com.tjlcast.DataSource;

import com.tjlcast.conf.kafkaConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tangjialiang on 2017/11/7.
 *
 */

public class KafkaSource extends Source {
    // Kafka brokers URL for Spark Streaming to connect and fetched messages from.
    private static final String KAFKA_BROKER_LIST = kafkaConfig.KAFKA_BROKER_LIST;
    // Time interval in milliseconds of Spark Streaming Job, 10 seconds by default.
    private static final int STREAM_WINDOW_MILLISECONDS = 10000; // 10 seconds
    // Kafka telemetry topic to subscribe to. This should match to the topic in the rule action.
    private static final Collection<String> TOPICS = Arrays.asList(kafkaConfig.kafkaTopic);
    // The application name
    public static final String APP_NAME = "Kafka Spark Streaming App";

    Map<String, Object> kafkaParams = null ;

    JavaStreamingContext jsc = null ;

    private volatile static KafkaSource instance = null ;

    private KafkaSource() {
        init();
    }

    private void init() {
        kafkaParams = new HashMap<String, Object>();
        kafkaParams.put("bootstrap.servers", KAFKA_BROKER_LIST);
        kafkaParams.put("key.deserializer", StringDeserializer.class);
        kafkaParams.put("value.deserializer", StringDeserializer.class);
        kafkaParams.put("group.id", kafkaConfig.group_id);
        kafkaParams.put("auto.offset.reset", "latest");
        kafkaParams.put("enable.auto.commit", false);

        SparkConf conf = new SparkConf().setAppName(APP_NAME).setMaster("local[2]");
        jsc = new JavaStreamingContext(conf, new Duration(STREAM_WINDOW_MILLISECONDS)) ;
    }

    public JavaInputDStream getStream() {
        JavaInputDStream<ConsumerRecord<String, String>> stream =
                KafkaUtils.createDirectStream(
                        jsc,
                        LocationStrategies.PreferConsistent(),
                        ConsumerStrategies.<String, String>Subscribe(TOPICS, kafkaParams)
                );

        return stream ;
    }

    public JavaInputDStream getInstanceStream() {
        if (instance == null) {
            instance = new KafkaSource() ;
        }
        return instance.getStream() ;
    }
}
