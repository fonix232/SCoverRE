package com.samsung.android.app.ledcover.fota;

import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;

public class CoverHexParser {
    static final /* synthetic */ boolean $assertionsDisabled;
    private static final boolean f8D = true;
    private static final String TAG = "IntelHexRecord";
    private int address;
    private int checksum;
    private byte[] data;
    private int length;
    private String record;
    private int type;

    static {
        $assertionsDisabled = !CoverHexParser.class.desiredAssertionStatus() ? f8D : $assertionsDisabled;
    }

    private char parseStartCode() {
        return this.record.charAt(0);
    }

    private int parseLength() {
        return Integer.parseInt(this.record.substring(1, 3), 16);
    }

    private int parseAddress() {
        return Integer.parseInt(this.record.substring(3, 7), 16);
    }

    private int parseType() {
        return Integer.parseInt(this.record.substring(7, 9), 16);
    }

    private int parseChecksum() {
        return Integer.parseInt(this.record.substring(this.record.length() - 2), 16);
    }

    private byte[] parseData(int size) {
        byte[] result = new byte[size];
        for (int i = 0; i != size; i++) {
            int pos = (i * 2) + 9;
            result[i] = (byte) Integer.parseInt(this.record.substring(pos, pos + 2), 16);
        }
        if ($assertionsDisabled || result.length == size) {
            return result;
        }
        throw new AssertionError();
    }

    private int computeChecksum() {
        int checksum = 0;
        for (int i = 1; i != this.record.length() - 2; i += 2) {
            checksum += Integer.parseInt(this.record.substring(i, i + 2), 16);
        }
        return (256 - (checksum % AccessibilityNodeInfoCompat.ACTION_NEXT_AT_MOVEMENT_GRANULARITY)) % AccessibilityNodeInfoCompat.ACTION_NEXT_AT_MOVEMENT_GRANULARITY;
    }

    private boolean parse() {
        boolean z = f8D;
        if (parseStartCode() != ':') {
            return $assertionsDisabled;
        }
        this.length = parseLength();
        this.address = parseAddress();
        this.type = parseType();
        this.data = parseData(this.length);
        this.checksum = parseChecksum();
        System.out.println("address: " + Integer.toHexString(this.address));
        System.out.println("length: " + Integer.toHexString(this.length));
        System.out.println("checksum: " + Integer.toString(this.checksum));
        for (int i = 0; i < this.length; i++) {
            System.out.print(String.format("%02X ", new Object[]{Byte.valueOf(this.data[i])}));
        }
        System.out.println();
        System.out.println("< Done >");
        if (this.checksum != computeChecksum()) {
            z = false;
        }
        return z;
    }

    public CoverHexParser(String line) throws IllegalArgumentException {
        this.record = null;
        this.length = 0;
        this.address = 0;
        this.type = 0;
        this.checksum = 0;
        this.data = null;
        this.record = line;
        if (!parse()) {
            throw new IllegalArgumentException(line);
        }
    }

    public String length() {
        return Integer.toHexString(this.length);
    }

    public int type() {
        return this.type;
    }

    public int address() {
        return this.address;
    }

    public String addressString() {
        return this.record.substring(3, 7);
    }

    public byte[] data() {
        return this.data;
    }
}
