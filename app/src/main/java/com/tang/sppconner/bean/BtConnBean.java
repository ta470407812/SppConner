package com.tang.sppconner.bean;

import io.realm.annotations.PrimaryKey;

public class BtConnBean {
    @PrimaryKey
    private String uuid;
    private long connTime;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public long getConnTime() {
        return connTime;
    }

    public void setConnTime(long connTime) {
        this.connTime = connTime;
    }
}
