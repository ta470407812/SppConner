package com.tang.sppconner.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tang.sppconner.config.BtConfig;
import com.tang.sppconner.utils.SimpleLog;

public class BtReceiver extends BroadcastReceiver {
    /*
    //adb shell am broadcast -a com.SppConner.Action.Open_Bluetooth  -n com.tang.sppconner/.activity.BtReceiver   打开蓝牙自定义广播
    //adb shell am broadcast -a com.SppConner.Action.Close_Bluetooth  -n com.tang.sppconner/.activity.BtReceiver   关闭蓝牙自定义广播
    //adb shell am broadcast -a com.SppConner.Action.Conn_Spp  -n com.tang.sppconner/.activity.BtReceiver --es uuid "11:11:22:33:35:23"  连接指定Uuid蓝牙设备
    //adb shell am broadcast -a com.SppConner.Action.Send_Cmd  -n com.tang.sppconner/.activity.BtReceiver --es cmd ab,dc,fe,02,00,00,01,00,25,ee  向设备发送命令
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        SimpleLog.print(this.getClass(), "action " + action);
        if (null != action) {
            Intent btIntent = new Intent(context, BtService.class);
            btIntent.putExtra("action", action);
            intent.setAction(action);
            switch (action) {
                case BtConfig
                        .Action.Open_Bluetooth: {
                }
                break;
                case BtConfig
                        .Action.Close_Bluetooth: {
                }
                break;
                case BtConfig
                        .Action.Conn_Spp: {
                    btIntent.putExtra(BtConfig.Key.UUID, intent.getStringExtra(BtConfig.Key.UUID));
                }
                break;
                case BtConfig
                        .Action.Send_Cmd: {
                    btIntent.putExtra(BtConfig.Key.Cmd, intent.getStringExtra(BtConfig.Key.Cmd));
                }
                break;
                default:
                    break;
            }

            context.startService(btIntent);
        }
    }
}
