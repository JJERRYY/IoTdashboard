package com.jerry.iotdashboard.pojo;

import java.util.Date;

/**
 * Auto-generated: 2021-12-20 3:13:12
 *
 * @author json.cn (i@json.cn)
 * @website http://www.json.cn/java2pojo/
 */
public class Parameters {

    private Date start_time;
    private Date end_time;
    private int resolution;
    private int hours;
    private String field;
    public void setStart_time(Date start_time) {
        this.start_time = start_time;
    }
    public Date getStart_time() {
        return start_time;
    }

    public void setEnd_time(Date end_time) {
        this.end_time = end_time;
    }
    public Date getEnd_time() {
        return end_time;
    }

    public void setResolution(int resolution) {
        this.resolution = resolution;
    }
    public int getResolution() {
        return resolution;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }
    public int getHours() {
        return hours;
    }

    public void setField(String field) {
        this.field = field;
    }
    public String getField() {
        return field;
    }

}
