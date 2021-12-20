/**
  * Copyright 2021 bejson.com 
  */
package com.jerry.iotdashboard.pojo;
import java.util.List;

/**
 * Auto-generated: 2021-12-20 15:13:11
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class dataBean {

    private Feed feed;
    private Parameters parameters;
    private List<String> columns;
    private String storage;
    private List<List<String>> data;
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

    public void setStorage(String storage) {
         this.storage = storage;
     }
     public String getStorage() {
         return storage;
     }

    public void setData(List<List<String>> data) {
         this.data = data;
     }
     public List<List<String>> getData() {
         return data;
     }

}