package com.sau.hadoopassignment.SparkConfig;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.SparkSession;

public class SparkConfig {
    public static SparkSession getSparkSession() {
        SparkConf conf = new SparkConf()
                .setAppName("EmployeeExpenses")
                .setMaster("local[*]") // Use local mode for testing
                .set("spark.cassandra.connection.host", "127.0.0.1") // Replace with your Cassandra host
                .set("spark.cassandra.connection.port", "9042"); // Default Cassandra port

        return SparkSession.builder()
                .config(conf)
                .getOrCreate();
    }
}