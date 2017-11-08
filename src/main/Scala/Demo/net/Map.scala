package Demo.net

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * Created by tangjialiang on 2017/11/7.
  */

object Map {
    def main(args: Array[String]): Unit = {
        val conf = new SparkConf().setMaster("local[2]").setAppName("NetWorkWordCount")
        val ssc = new StreamingContext(conf, Seconds(1))

        val lines = ssc.socketTextStream("client", 9999)

        val linesNew = lines.map(lines => lines + "_NEW")

        linesNew.print()

        ssc.start()
        ssc.awaitTermination()
    }
}

class Map {

}
