package com.jerry.iotdashboard.pojo;

import java.util.Objects;

public class noticeBean {
    String type;
    String time;



    Float value;
    String action;

    public String getType() {
        return type;
    }

    public noticeBean setType(String type) {
        this.type = type;
        return this;
    }
    public Float getValue() {
        return value;
    }

    public noticeBean setValue(Float value) {
        this.value = value;
        return this;
    }
    public String getTime() {
        return time;
    }

    public noticeBean setTime(String time) {
        this.time = time;
        return this;
    }

    public String getAction() {
        return action;
    }

    public noticeBean setAction(String action) {
        this.action = action;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        noticeBean that = (noticeBean) o;
        return Objects.equals(type, that.type) && time.equals(that.time) && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, time, value);
    }
}
