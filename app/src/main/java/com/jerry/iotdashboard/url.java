package com.jerry.iotdashboard;

public class url {
    public static String BloodOxygen ="https://io.adafruit.com/api/v2/abao/feeds/blood-oxygen/data/chart?hours=48";
    public static String HeartRate ="https://io.adafruit.com/api/v2/abao/feeds/heart-rate/data/chart?hours=48";
    public static String Humidity ="https://io.adafruit.com/api/v2/abao/feeds/humidity/data/chart?hours=48";
    public static String Room_Temperature ="https://io.adafruit.com/api/v2/abao/feeds/room-temperature/data/chart?hours=48";
    public static String Temperature ="https://io.adafruit.com/api/v2/abao/feeds/temperature/data/chart?hours=48";
    public static String ir1 ="https://io.adafruit.com/api/v2/abao/feeds/lwir-one/data/chart?hours=48";
    public static String ir2 ="https://io.adafruit.com/api/v2/abao/feeds/lwir-two/data/chart?hours=48";
    public static String controller ="https://io.adafruit.com/api/v2/abao/feeds/controller/data/chart?hours=48";
    public static String[] chartURLs = {BloodOxygen,HeartRate,Humidity,Room_Temperature,Temperature};
    public static String[] dbUrls = {BloodOxygen,HeartRate,Humidity,Room_Temperature,Temperature,ir1,ir2,controller};
}
