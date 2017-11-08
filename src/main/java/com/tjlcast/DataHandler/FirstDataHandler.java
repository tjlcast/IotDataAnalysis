package com.tjlcast.DataHandler;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tjlcast.Device.AvgDeviceValueData;
import com.tjlcast.Device.DeviceData;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import scala.Tuple2;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by tangjialiang on 2017/11/7.
 *
 */
public class FirstDataHandler {

    public void process(JavaInputDStream<ConsumerRecord<String, String>> jDStream) {
//      {"uid":"922291","data":0,"current_time":"2017-11-06 17:44:45"}
        jDStream.foreachRDD(rdd ->
        {
            JavaRDD<DeviceData> deviceRdd = rdd.map(new DeviceMessageDataMapper()) ;
            JavaPairRDD<String, AvgDeviceValueData> valueByUidRdd = deviceRdd.mapToPair(d -> new Tuple2<>(d.getUid(), new AvgDeviceValueData(d.getData()))) ;
            valueByUidRdd = valueByUidRdd.reduceByKey((a, b) -> AvgDeviceValueData.sum(a, b)) ;

            List<DeviceData> aggData = valueByUidRdd.map(t -> new DeviceData(t._1, t._2.getAvgValue())).collect() ;
        });

//        jDStream.foreachRDD(rdd ->
//        {
//            // Map incoming JSON to WindSpeedAndGeoZoneData objects
//            JavaRDD<WindSpeedAndGeoZoneData> windRdd = rdd.map(new WeatherStationDataMapper());
//            // Map WindSpeedAndGeoZoneData objects by GeoZone
//            JavaPairRDD<String, AvgWindSpeedData> windByZoneRdd = windRdd.mapToPair(d -> new Tuple2<>(d.getGeoZone(), new AvgWindSpeedData(d.getWindSpeed())));
//            // Reduce all data volume by GeoZone key
//            windByZoneRdd = windByZoneRdd.reduceByKey((a, b) -> AvgWindSpeedData.sum(a, b));
//            // Map <GeoZone, AvgWindSpeedData> back to WindSpeedAndGeoZoneData
//            List<WindSpeedAndGeoZoneData> aggData = windByZoneRdd.map(t -> new WindSpeedAndGeoZoneData(t._1, t._2.getAvgValue())).collect();
//            // Push aggregated data to ThingsBoard Asset
//            restClient.sendTelemetryToAsset(aggData);
//        });
    }

    private static class DeviceMessageDataMapper implements Function<ConsumerRecord<String, String>, DeviceData> {
        private static final ObjectMapper mapper = new ObjectMapper();

        public DeviceData call(ConsumerRecord<String, String> record) throws Exception {
            mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")) ;
            return mapper.readValue(record.value(), DeviceData.class);
        }
    }
}
