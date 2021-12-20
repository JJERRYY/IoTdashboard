package com.jerry.iotdashboard.pojo;

/**
 * Copyright 2021 json.cn
 */
import java.util.Date;
import java.util.List;

/**
 * Auto-generated: 2021-12-20 3:13:12
 *
 * @author json.cn (i@json.cn)
 * @website http://www.json.cn/java2pojo/
 */
public class JsonRootBean {

    private Feed feed;
    private Parameters parameters;
    private List<String> columns;
    private List<List<Date>> data;
    public void setFeed(Feed feed) {
        this.feed = feed;
    }
    public Feed getFeed() {
        return feed;
    }

    public void setParameters(Parameters parameters) {
        this.parameters = parameters;
    }
    public Parameters getParameters() {
        return parameters;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }
    public List<String> getColumns() {
        return columns;
    }

    public void setData(List<List<Date>> data) {
        this.data = data;
    }
    public List<List<Date>> getData() {
        return data;
    }

}