package com.tang.sppconner.config;

public interface BtConfig {
    interface Action {
        String Open_Bluetooth = "com.SppConner.Action.Open_Bluetooth";
        String Close_Bluetooth = "com.SppConner.Action.Close_Bluetooth";
        String Conn_Spp = "com.SppConner.Action.Conn_Spp";
        String Send_Cmd = "com.SppConner.Action.Send_Cmd";
    }

    interface Key {
        String UUID = "uuid";//
        String Cmd = "cmd";//所携带的蓝牙数据
    }
}
