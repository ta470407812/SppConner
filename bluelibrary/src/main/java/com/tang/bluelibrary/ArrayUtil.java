package com.tang.bluelibrary;

import java.util.zip.CRC32;

/**
 * Created by zhaowanxing on 2017/4/23.
 */

public class ArrayUtil {
    public static byte[] extractBytes(byte[] data, int start, int length) {
        byte[] bytes = new byte[length];
        System.arraycopy(data, start, bytes, 0, length);
        return bytes;
    }

    public static boolean isEqual(byte[] array_1, byte[] array_2) {
        if (array_1 == null) {
            return array_2 == null;
        }
        if (array_2 == null) {
            return false;
        }
        if (array_1 == array_2) {
            return true;
        }
        if (array_1.length != array_2.length) {
            return false;
        }
        for (int i = 0; i < array_1.length; i++) {
            if (array_1[i] != array_2[i]) {
                return false;
            }
        }
        return true;
    }

    public static boolean contains(byte[] parent, byte[] child) {
        if (parent == null) {
            return child == null;
        }
        if (child == null || child.length == 0) {
            return true;
        }
        if (parent == child) {
            return true;
        }
        return new String(parent).contains(new String(child));
    }

    public static long crc32(byte[] data, int offset, int length) {
        CRC32 crc32 = new CRC32();
        crc32.update(data, offset, length);
        return crc32.getValue();
    }



    public static byte checkSum(byte[] data, int len) {
        byte sum = (byte) 0;
        for (int i = 0; i < len; i++) {
            sum ^= data[i];
        }
        return sum;
    }

    public static String toHex(byte[] data) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            buffer.append(String.format("%02x", data[i])).append(",");
        }
        return buffer.toString();
    }

    public static boolean startsWith(byte[] data, byte[] param) {
        if (data == null) {
            return param == null;
        }
        if (param == null) {
            return true;
        }
        if (data.length < param.length) {
            return false;
        }
        for (int i = 0; i < param.length; i++) {
            if (data[i] != param[i]) {
                return false;
            }
        }
        return true;
    }
}
