package com.onenet.datapush.receiver.response.triggers;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TriggerDeviceDetailItem {
    @JsonProperty(value="id")
    private String id;

    @JsonProperty(value="title")
    private String title;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
