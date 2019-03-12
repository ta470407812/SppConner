package com.tang.sppconner.bean;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class CmdBean extends RealmObject {

    private byte cmdType;//蓝牙类型
    private byte[] cmdData;//具体数据
    private long cmdTime;//发生的时间
    private String msg;//备注信息

    public byte getCmdType() {
        return cmdType;
    }

    public void setCmdType(byte cmdType) {
        this.cmdType = cmdType;
    }

    public byte[] getCmdData() {
        return cmdData;
    }

    public void setCmdData(byte[] cmdData) {
        this.cmdData = cmdData;
    }

    public long getCmdTime() {
        return cmdTime;
    }

    public void setCmdTime(long cmdTime) {
        this.cmdTime = cmdTime;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
