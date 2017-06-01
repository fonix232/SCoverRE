package com.samsung.android.graphics.spr.document;

import com.samsung.android.graphics.spr.document.shape.SprObjectBase;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class SprInputStream {
    private DataInputStream in;
    public ArrayList<SprObjectBase> mAnimationObject;
    public short mMajorVersion;
    private long mMark = 0;
    public short mMinorVersion;
    private long mPosition = 0;

    public SprInputStream(InputStream inputStream) {
        this.in = new DataInputStream(inputStream);
    }

    public long getPosition() {
        return this.mPosition;
    }

    public synchronized void mark(int i) {
        this.in.mark(i);
        this.mMark = this.mPosition;
    }

    public int read() throws IOException {
        int read = this.in.read();
        if (read >= 0) {
            this.mPosition++;
        }
        return read;
    }

    public int read(byte[] bArr, int i, int i2) throws IOException {
        int read = this.in.read(bArr, i, i2);
        if (read > 0) {
            this.mPosition += (long) read;
        }
        return read;
    }

    public byte readByte() throws IOException {
        byte readByte = this.in.readByte();
        this.mPosition++;
        return readByte;
    }

    public float readFloat() throws IOException {
        float readFloat = this.in.readFloat();
        this.mPosition += 4;
        return readFloat;
    }

    public int readInt() throws IOException {
        int readInt = this.in.readInt();
        this.mPosition += 4;
        return readInt;
    }

    public short readShort() throws IOException {
        short readShort = this.in.readShort();
        this.mPosition += 2;
        return readShort;
    }

    public synchronized void reset() throws IOException {
        if (this.in.markSupported()) {
            this.in.reset();
            this.mPosition = this.mMark;
        } else {
            throw new IOException("Mark not supported");
        }
    }

    public long skip(long j) throws IOException {
        long skip = this.in.skip(j);
        if (skip > 0) {
            this.mPosition += skip;
        }
        return skip;
    }
}
