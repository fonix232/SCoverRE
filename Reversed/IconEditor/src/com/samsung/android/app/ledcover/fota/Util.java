package com.samsung.android.app.ledcover.fota;

import android.nfc.NfcAdapter;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Util {
    public static final byte HEADER = (byte) 0;
    public static final byte HEADER_FLAG = (byte) 21;
    public static final int HEADER_SIZE = 2;
    public static final String TAG = "CoverFotaTest";

    public static String getByteDataString(byte[] data) {
        if (data == null) {
            return "null";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            sb.append(String.format("%02X", new Object[]{Byte.valueOf(data[i])})).append(" ");
        }
        return sb.toString();
    }

    public static byte[] hexStringToBytes(String str) {
        if (str.length() == 0 || str.length() % HEADER_SIZE != 0) {
            return new byte[0];
        }
        byte[] buffer = new byte[(str.length() / HEADER_SIZE)];
        int bx = 0;
        int sx = 0;
        while (bx < buffer.length) {
            char c = str.charAt(sx);
            int i = c > '9' ? c > 'Z' ? (c - 97) + 10 : (c - 65) + 10 : c - 48;
            buffer[bx] = (byte) (i << 4);
            sx++;
            c = str.charAt(sx);
            byte b = buffer[bx];
            i = c > '9' ? c > 'Z' ? (c - 97) + 10 : (c - 65) + 10 : c - 48;
            buffer[bx] = (byte) (((byte) i) | b);
            bx++;
            sx++;
        }
        return buffer;
    }

    public static byte[] authVersionCheck(NfcAdapter mNfcAdapter) {
        byte[] response = mNfcAdapter.semTransceiveDataWithLedCover(Constants.AUTH_VERSION_CHECK_COMMAND);
        Log.d(TAG, "Version check response: " + getByteDataString(response));
        return response;
    }

    public static byte[] mcuVersionCheck(NfcAdapter mNfcAdapter) {
        byte[] response = mNfcAdapter.semTransceiveDataWithLedCover(Constants.MCU_VERSION_CHECK_COMMAND);
        Log.d(TAG, "Version check response: " + getByteDataString(response));
        return response;
    }

    public static byte[] chipReset(NfcAdapter nfcAdapter) {
        Log.d(TAG, "+++ chipReset Start +++");
        Log.d(TAG, "semStopLedCoverMode response: " + nfcAdapter.semStopLedCoverMode());
        byte[] startResponse = nfcAdapter.semStartLedCoverMode();
        Log.d(TAG, "semStartLedCoverMode response: " + getByteDataString(startResponse));
        return startResponse;
    }

    public static boolean checkErrorCode(byte[] response) {
        boolean key = false;
        if (response != null) {
            for (byte b : Constants.ERROR_CODE) {
                if (response[0] == b) {
                    key = true;
                }
            }
        }
        return key;
    }

    public static byte[] authMakeFlashWriteCommand(String addString, String lengthString, byte[] payload) {
        byte[] address = hexStringToBytes(addString);
        byte[] lengthStr = hexStringToBytes(lengthString);
        byte[] command = new byte[(((payload.length + HEADER_SIZE) + address.length) + lengthStr.length)];
        command[0] = HEADER;
        command[1] = HEADER_FLAG;
        System.arraycopy(address, 0, command, HEADER_SIZE, address.length);
        System.arraycopy(lengthStr, 0, command, address.length + HEADER_SIZE, lengthStr.length);
        System.arraycopy(payload, 0, command, (address.length + HEADER_SIZE) + lengthStr.length, payload.length);
        return command;
    }

    public static byte[] authFotaOnMode(NfcAdapter nfcAdapter) {
        Log.d(TAG, "[0040002000] Change FOTA Mode response: " + getByteDataString(nfcAdapter.semTransceiveDataWithLedCover(Constants.MESSAGE_CHANGE_BOOT_FW_MODE)));
        return chipReset(nfcAdapter);
    }

    public static byte[] authFotaOffMode(NfcAdapter nfcAdapter) {
        Log.d(TAG, "[0040002F00] Change Auth Mode response: " + getByteDataString(nfcAdapter.semTransceiveDataWithLedCover(Constants.MESSAGE_CHANGE_BOOT_AUTH_MODE)));
        return chipReset(nfcAdapter);
    }

    public static boolean authStopNfcMode(NfcAdapter nfcAdapter) {
        Log.d(TAG, "[stopNfcMode]");
        byte[] checkResponse = nfcAdapter.semTransceiveDataWithLedCover(Constants.MESSAGE_CHECKSUM_VALID);
        byte[] endResponse = authFotaOffMode(nfcAdapter);
        boolean key = nfcAdapter.semStopLedCoverMode();
        Log.d(TAG, "Check Sum Res: " + getByteDataString(checkResponse));
        Log.d(TAG, "Change Auth Mode response: " + getByteDataString(endResponse));
        Log.d(TAG, "semStopLedCoverMode response: " + key);
        return key;
    }

    public static boolean authStartNfcMode(NfcAdapter nfcAdapter) {
        Log.d(TAG, "[startNfcMode]");
        boolean key = false;
        if (nfcAdapter == null) {
            return 0;
        }
        byte[] startResponse = nfcAdapter.semStartLedCoverMode();
        Log.d(TAG, "semStartLedCoverMode response: " + getByteDataString(startResponse));
        if (!checkErrorCode(startResponse) && startResponse[0] == null) {
            if (startResponse[1] == 85) {
                byte[] response = authFotaOnMode(nfcAdapter);
                Log.d(TAG, "Change FOTA Mode response: " + getByteDataString(response));
                if (response[1] == Constants.FOTA_FW_MODE) {
                    Log.d(TAG, "Success StartNfcMode");
                    key = true;
                }
            } else if (startResponse[1] == Constants.FOTA_FW_MODE) {
                Log.d(TAG, "It is already FOTA F/W Mode");
                key = true;
            }
        }
        return key;
    }

    public static byte[] mcuMakeFlashWriteCommand(String addString, byte[] payload) {
        byte[] address = hexStringToBytes(addString);
        byte[] command = new byte[((Constants.MESSAGE_MCU_DOWNLOAD_HEADER.length + payload.length) + address.length)];
        System.arraycopy(Constants.MESSAGE_MCU_DOWNLOAD_HEADER, 0, command, 0, Constants.MESSAGE_MCU_DOWNLOAD_HEADER.length);
        System.arraycopy(address, 0, command, Constants.MESSAGE_MCU_DOWNLOAD_HEADER.length, address.length);
        System.arraycopy(payload, 0, command, address.length + Constants.MESSAGE_MCU_DOWNLOAD_HEADER.length, payload.length);
        return command;
    }

    public static boolean mcuStartMode(NfcAdapter nfcAdapter) {
        Log.d(TAG, "[startNfcMode]");
        boolean key = false;
        if (nfcAdapter == null) {
            return 0;
        }
        byte[] startResponse = nfcAdapter.semStartLedCoverMode();
        Log.d(TAG, "semStartLedCoverMode response: " + getByteDataString(startResponse));
        if (!checkErrorCode(startResponse)) {
            if (startResponse[1] == 85) {
                byte[] response = mcuFwDownLoadMode(nfcAdapter);
                if (response[0] == 33 || response[0] == 34) {
                    Log.d(TAG, "Success MUC DOWNLOAD");
                    response = nfcAdapter.semTransceiveDataWithLedCover(Constants.MESSAGE_MCU_ERASE_CMD);
                    Log.d(TAG, "Flash Erase response: " + getByteDataString(response));
                    if (response[0] == -112) {
                        key = true;
                    }
                }
            } else if (startResponse[1] == 90) {
                Log.d(TAG, "It is not Auth F/W Mode");
            } else {
                Log.d(TAG, "[mcuStartMode] Error Code");
            }
        }
        return key;
    }

    public static boolean mcuStopMode(NfcAdapter nfcAdapter) {
        Log.d(TAG, "[stopNfcMode]");
        boolean key = false;
        if (nfcAdapter == null) {
            return 0;
        }
        byte[] response = nfcAdapter.semTransceiveDataWithLedCover(Constants.MESSAGE_MCU_CHECKSUM_CMD);
        Log.d(TAG, "MCU CheckSum response: " + getByteDataString(response));
        if (response[0] == -112) {
            response = nfcAdapter.semTransceiveDataWithLedCover(Constants.MESSAGE_MCU_CHECKSUM_READ_CMD);
            byte[] endResponse = mcuFwDownLoadExit(nfcAdapter);
            chipReset(nfcAdapter);
            key = nfcAdapter.semStopLedCoverMode();
            Log.d(TAG, "MCU CheckSum Read response: " + getByteDataString(response));
            Log.d(TAG, "Change MUC DOWNLOAD EXIT response: " + getByteDataString(endResponse));
            Log.d(TAG, "semStopLedCoverMode response: " + key);
        } else {
            Log.d(TAG, "[mcuStopMode] Error Code");
        }
        return key;
    }

    public static byte[] mcuFwDownLoadMode(NfcAdapter nfcAdapter) {
        byte[] response = nfcAdapter.semTransceiveDataWithLedCover(Constants.MESSAGE_MCU_DOWNLOAD_MODE);
        Log.d(TAG, "[00A40001] Change MUC DOWNLOAD MODE response: " + getByteDataString(response));
        return response;
    }

    public static byte[] mcuFwDownLoadExit(NfcAdapter nfcAdapter) {
        byte[] response = nfcAdapter.semTransceiveDataWithLedCover(Constants.MESSAGE_MCU_DOWNLOAD_EXIT);
        Log.d(TAG, "[00A40001] Change MUC DOWNLOAD EXIT response: " + getByteDataString(response));
        return response;
    }

    public static byte[] RefStartCoverAuth(NfcAdapter mNfcAdapter) {
        byte[] returnObject = null;
        try {
            Method setNfcEnabled = Class.forName("android.nfc.NfcAdapter").getDeclaredMethod("startCoverAuth", new Class[0]);
            setNfcEnabled.setAccessible(true);
            return (byte[]) setNfcEnabled.invoke(mNfcAdapter, new Object[0]);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return returnObject;
        } catch (NoSuchMethodException e2) {
            e2.printStackTrace();
            return returnObject;
        } catch (IllegalAccessException e3) {
            e3.printStackTrace();
            return returnObject;
        } catch (InvocationTargetException e4) {
            e4.printStackTrace();
            return returnObject;
        }
    }

    public static boolean RefStopCoverAuth(NfcAdapter mNfcAdapter) {
        boolean returnObject = false;
        try {
            Method setNfcEnabled = Class.forName("android.nfc.NfcAdapter").getDeclaredMethod("stopCoverAuth", new Class[0]);
            setNfcEnabled.setAccessible(true);
            returnObject = ((Boolean) setNfcEnabled.invoke(mNfcAdapter, new Object[0])).booleanValue();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e2) {
            e2.printStackTrace();
        } catch (IllegalAccessException e3) {
            e3.printStackTrace();
        } catch (InvocationTargetException e4) {
            e4.printStackTrace();
        }
        return returnObject;
    }

    public static byte[] RefTransceiveAuthData(byte[] data, NfcAdapter mNfcAdapter) {
        byte[] response = null;
        try {
            Object[] methodParamObject = new Object[]{data};
            Method setNfcEnabled = Class.forName("android.nfc.NfcAdapter").getDeclaredMethod("transceiveAuthData", new Class[]{byte[].class});
            setNfcEnabled.setAccessible(true);
            return (byte[]) setNfcEnabled.invoke(mNfcAdapter, methodParamObject);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return response;
        } catch (NoSuchMethodException e2) {
            e2.printStackTrace();
            return response;
        } catch (IllegalAccessException e3) {
            e3.printStackTrace();
            return response;
        } catch (InvocationTargetException e4) {
            e4.printStackTrace();
            return response;
        }
    }
}
