# SppConner

#### 介绍

android spp 开发工具


#### 软件架构
软件架构说明

#### 安装教程

#### 使用说明

1、配置adb环境
2、安装此app
3、发送指定adb命令进行操作

 //adb shell am broadcast -a com.SppConner.Action.Open_Bluetooth  -n com.tang.sppconner/.activity.BtReceiver   打开蓝牙自定义广播
 
 //adb shell am broadcast -a com.SppConner.Action.Close_Bluetooth  -n com.tang.sppconner/.activity.BtReceiver   关闭蓝牙自定义广播
 
 //adb shell am broadcast -a com.SppConner.Action.Conn_Spp  -n com.tang.sppconner/.activity.BtReceiver --es uuid "11:11:22:33:35:23"  连接指定Uuid蓝牙设备
 
 //adb shell am broadcast -a com.SppConner.Action.Send_Cmd  -n com.tang.sppconner/.activity.BtReceiver --es cmd ab,dc,fe,02,00,00,01,00,25,ee  向设备发送命令


