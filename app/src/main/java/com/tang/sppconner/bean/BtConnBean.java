package com.tang.sppconner.bean;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class BtConnBean extends RealmObject {
    @PrimaryKey
    private String uuid;

    private String name;
    private long connTime;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getConnTime() {
        return connTime;
    }

    public void setConnTime(long connTime) {
        this.connTime = connTime;
    }
}
