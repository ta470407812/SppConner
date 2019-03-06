package com.tang.bluelibrary;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Build;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by zhaowanxing on 2017/4/17.
 */

public class BtHelper {

    public static BluetoothManager getBluetoothManager(Context context) {
        return (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
    }

    public static BluetoothAdapter getBluetoothAdapter(Context context) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN_MR1)
            return BluetoothAdapter.getDefaultAdapter();
        else {
            return getBluetoothManager(context).getAdapter();
        }
    }

    public static BluetoothDevice getRemoteDevice(Context context, String address) {
        try {
            return getBluetoothAdapter(context).getRemoteDevice(address);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] parseManufacturerSpecificData(byte[] scanRecord) {
        if (scanRecord != null) {
            int currentPos = 0;
            while (currentPos < scanRecord.length) {
                // length is unsigned int.
                int length = scanRecord[currentPos++] & 0xFF;
                if (length == 0) {
                    break;
                }
                // Note the length includes the length of the field type itself.
                int dataLength = length - 1;
                // fieldType is unsigned int.
                int fieldType = scanRecord[currentPos++] & 0xFF;
                if (fieldType == 0xFF && dataLength > 2) {
                    // The first two bytes of the manufacturer specific data are
                    // manufacturer ids in little endian.
                    byte[] manufacturerDataBytes = ArrayUtil.extractBytes(scanRecord, currentPos + 2,
                            dataLength - 2);
                    return manufacturerDataBytes;
                }
                currentPos += dataLength;
            }
        }
        return null;
    }

    public static boolean createBond(BluetoothDevice btDevice) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return btDevice.createBond();
        } else {
            try {
                Class<?> cls = btDevice.getClass();
                Method method = cls.getMethod("createBond", new Class[0]);
                Boolean result = (Boolean) method.invoke(btDevice, new Object[0]);
                return result.booleanValue();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean removeBond(BluetoothDevice btDevice) {
        Boolean returnValue = Boolean.FALSE;
        try {
            returnValue = ((Boolean) btDevice.getClass().getMethod("removeBond", new Class[0]).invoke(btDevice, new Object[0])).booleanValue();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return returnValue.booleanValue();
    }

    public static boolean setPin(BluetoothDevice btDevice, String str) throws Exception {
        Boolean returnValue = Boolean.FALSE;
        try {
            returnValue = (Boolean) btDevice.getClass().getDeclaredMethod("setPin", new Class[]{byte[].class}).invoke(btDevice, new Object[]{str.getBytes("UTF-8")});
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e2) {
            e2.printStackTrace();
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        return returnValue.booleanValue();
    }

    public static boolean cancelPairingUserInput(BluetoothDevice btDevice) {
        Boolean returnValue = Boolean.FALSE;
        try {
            ((Boolean) btDevice.getClass().getMethod("cancelPairingUserInput", new Class[0]).invoke(btDevice, new Object[0])).booleanValue();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return returnValue.booleanValue();
    }

    public static boolean cancelBondProcess(BluetoothDevice btDevice) {
        Boolean returnValue = Boolean.FALSE;
        try {
            returnValue = (Boolean) btDevice.getClass().getMethod("cancelBondProcess", new Class[0]).invoke(btDevice, new Object[0]);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e2) {
            e2.printStackTrace();
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        return returnValue.booleanValue();
    }
}
