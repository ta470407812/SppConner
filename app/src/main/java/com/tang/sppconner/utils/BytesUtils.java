package com.tang.sppconner.utils;

import android.text.TextUtils;

/**
 * Created by tang on 16/9/22.
 */

public class BytesUtils {
    /*
    二进制数组转字符打印
     */
    public static String bytes2Hex(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder("[");
        String tempString;
        for (byte tempByte : bytes) {
            tempString = Integer.toHexString(tempByte);
            if (tempString.length() > 2) {
                tempString = tempString.substring(tempString.length() - 2, tempString.length());
            } else if (tempString.length() == 1) {
                tempString = "0" + tempString;
            }
            stringBuilder.append(tempString).append(",");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        stringBuilder.append("]");
        return stringBuilder.toString().toUpperCase();
    }

    /*
    ab,dc,fe,02,00,00,01,00,25,ee
    字符转二进制数组
     */
    public static byte[] hex2Bytes(String byteString) {
        if (TextUtils.isEmpty(byteString))
            return null;
        try {
            String[] spitArray = byteString.split(",");
            if (spitArray.length == 0)
                return null;
            byte[] bytes = new byte[spitArray.length];
            for (int i = 0; i < spitArray.length; i++) {
                bytes[i] = (byte) Integer.parseInt(spitArray[i], 16);
            }
            return bytes;
        } catch (Exception e) {

        }
        return null;
    }

    public static int lowOnStart(byte byte1, byte byte2, byte byte3, byte byte4) {//低位在前
        return getIntValue(byte1)
                + (getIntValue(byte2) << 8)
                + (getIntValue(byte3) << 16)
                + (getIntValue(byte4) << 24);
    }

    //    private static int getIntValue(byte byt) {
//        return ((0xff) & byt);
//    }
    private static int getIntValue(byte byt) {
        return byt < 0 ? Math.abs(byt) + (128 - Math.abs(byt)) * 2 : byt;
    }
}
