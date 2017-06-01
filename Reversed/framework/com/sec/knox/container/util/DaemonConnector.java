package com.sec.knox.container.util;

import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.net.LocalSocketAddress.Namespace;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.HandlerThread;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import com.google.android.collect.Lists;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class DaemonConnector implements Runnable, Callback {
    private static final int DEFAULT_TIMEOUT = 90000;
    private static final boolean LOGD = true;
    private static final long WARN_EXECUTE_DELAY_MS = 500;
    private final int BUFFER_SIZE = 4096;
    private final String TAG;
    private Handler mCallbackHandler;
    private IDaemonConnectorCallbacks mCallbacks;
    private final Object mDaemonLock = new Object();
    private OutputStream mOutputStream;
    private final ResponseQueue mResponseQueue;
    private AtomicInteger mSequenceNumber;
    private String mSocket;

    public static class Command {
        private ArrayList<Object> mArguments = Lists.newArrayList();
        private String mCmd;

        public Command(String str, Object... objArr) {
            this.mCmd = str;
            for (Object appendArg : objArr) {
                appendArg(appendArg);
            }
        }

        public Command appendArg(Object obj) {
            this.mArguments.add(obj);
            return this;
        }
    }

    private static class DaemonArgumentException extends DaemonConnectorException {
        public DaemonArgumentException(String str, DaemonEvent daemonEvent) {
            super(str, daemonEvent);
        }

        public IllegalArgumentException rethrowAsParcelableException() {
            throw new IllegalArgumentException(getMessage(), this);
        }
    }

    private static class DaemonFailureException extends DaemonConnectorException {
        public DaemonFailureException(String str, DaemonEvent daemonEvent) {
            super(str, daemonEvent);
        }
    }

    private static class ResponseQueue {
        private int mMaxCount;
        private final LinkedList<PendingCmd> mPendingCmds = new LinkedList();

        private static class PendingCmd {
            public int availableResponseCount;
            public int cmdNum;
            public String request;
            public BlockingQueue<DaemonEvent> responses = new ArrayBlockingQueue(10);

            public PendingCmd(int i, String str) {
                this.cmdNum = i;
                this.request = str;
            }
        }

        ResponseQueue(int i) {
            this.mMaxCount = i;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void add(int r10, com.sec.knox.container.util.DaemonEvent r11) {
            /*
            r9 = this;
            r1 = 0;
            r6 = r9.mPendingCmds;
            monitor-enter(r6);
            r5 = r9.mPendingCmds;	 Catch:{ all -> 0x00b5 }
            r4 = r5.iterator();	 Catch:{ all -> 0x00b5 }
        L_0x000a:
            r5 = r4.hasNext();	 Catch:{ all -> 0x00b5 }
            if (r5 == 0) goto L_0x00b9;
        L_0x0010:
            r3 = r4.next();	 Catch:{ all -> 0x00b5 }
            r3 = (com.sec.knox.container.util.DaemonConnector.ResponseQueue.PendingCmd) r3;	 Catch:{ all -> 0x00b5 }
            r5 = r3.cmdNum;	 Catch:{ all -> 0x00b5 }
            if (r5 != r10) goto L_0x000a;
        L_0x001a:
            r1 = r3;
            r2 = r1;
        L_0x001c:
            if (r2 != 0) goto L_0x00b7;
        L_0x001e:
            r5 = r9.mPendingCmds;	 Catch:{ all -> 0x008e }
            r5 = r5.size();	 Catch:{ all -> 0x008e }
            r7 = r9.mMaxCount;	 Catch:{ all -> 0x008e }
            if (r5 < r7) goto L_0x0092;
        L_0x0028:
            r5 = "DaemonConnector.ResponseQueue";
            r7 = new java.lang.StringBuilder;	 Catch:{ all -> 0x008e }
            r7.<init>();	 Catch:{ all -> 0x008e }
            r8 = "more buffered than allowed: ";
            r7 = r7.append(r8);	 Catch:{ all -> 0x008e }
            r8 = r9.mPendingCmds;	 Catch:{ all -> 0x008e }
            r8 = r8.size();	 Catch:{ all -> 0x008e }
            r7 = r7.append(r8);	 Catch:{ all -> 0x008e }
            r8 = " >= ";
            r7 = r7.append(r8);	 Catch:{ all -> 0x008e }
            r8 = r9.mMaxCount;	 Catch:{ all -> 0x008e }
            r7 = r7.append(r8);	 Catch:{ all -> 0x008e }
            r7 = r7.toString();	 Catch:{ all -> 0x008e }
            android.util.Log.e(r5, r7);	 Catch:{ all -> 0x008e }
            r5 = r9.mPendingCmds;	 Catch:{ all -> 0x008e }
            r3 = r5.remove();	 Catch:{ all -> 0x008e }
            r3 = (com.sec.knox.container.util.DaemonConnector.ResponseQueue.PendingCmd) r3;	 Catch:{ all -> 0x008e }
            r5 = "DaemonConnector.ResponseQueue";
            r7 = new java.lang.StringBuilder;	 Catch:{ all -> 0x008e }
            r7.<init>();	 Catch:{ all -> 0x008e }
            r8 = "Removing request: ";
            r7 = r7.append(r8);	 Catch:{ all -> 0x008e }
            r8 = r3.request;	 Catch:{ all -> 0x008e }
            r7 = r7.append(r8);	 Catch:{ all -> 0x008e }
            r8 = " (";
            r7 = r7.append(r8);	 Catch:{ all -> 0x008e }
            r8 = r3.cmdNum;	 Catch:{ all -> 0x008e }
            r7 = r7.append(r8);	 Catch:{ all -> 0x008e }
            r8 = ")";
            r7 = r7.append(r8);	 Catch:{ all -> 0x008e }
            r7 = r7.toString();	 Catch:{ all -> 0x008e }
            android.util.Log.e(r5, r7);	 Catch:{ all -> 0x008e }
            goto L_0x001e;
        L_0x008e:
            r5 = move-exception;
            r1 = r2;
        L_0x0090:
            monitor-exit(r6);
            throw r5;
        L_0x0092:
            r1 = new com.sec.knox.container.util.DaemonConnector$ResponseQueue$PendingCmd;	 Catch:{ all -> 0x008e }
            r5 = 0;
            r1.<init>(r10, r5);	 Catch:{ all -> 0x008e }
            r5 = r9.mPendingCmds;	 Catch:{ all -> 0x00b5 }
            r5.add(r1);	 Catch:{ all -> 0x00b5 }
        L_0x009d:
            r5 = r1.availableResponseCount;	 Catch:{ all -> 0x00b5 }
            r5 = r5 + 1;
            r1.availableResponseCount = r5;	 Catch:{ all -> 0x00b5 }
            r5 = r1.availableResponseCount;	 Catch:{ all -> 0x00b5 }
            if (r5 != 0) goto L_0x00ac;
        L_0x00a7:
            r5 = r9.mPendingCmds;	 Catch:{ all -> 0x00b5 }
            r5.remove(r1);	 Catch:{ all -> 0x00b5 }
        L_0x00ac:
            monitor-exit(r6);
            r5 = r1.responses;	 Catch:{ InterruptedException -> 0x00b3 }
            r5.put(r11);	 Catch:{ InterruptedException -> 0x00b3 }
        L_0x00b2:
            return;
        L_0x00b3:
            r0 = move-exception;
            goto L_0x00b2;
        L_0x00b5:
            r5 = move-exception;
            goto L_0x0090;
        L_0x00b7:
            r1 = r2;
            goto L_0x009d;
        L_0x00b9:
            r2 = r1;
            goto L_0x001c;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.sec.knox.container.util.DaemonConnector.ResponseQueue.add(int, com.sec.knox.container.util.DaemonEvent):void");
        }

        public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
            printWriter.println("Pending requests:");
            synchronized (this.mPendingCmds) {
                for (PendingCmd pendingCmd : this.mPendingCmds) {
                    printWriter.println("  Cmd " + pendingCmd.cmdNum + " - " + pendingCmd.request);
                }
            }
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public com.sec.knox.container.util.DaemonEvent remove(int r13, int r14, java.lang.String r15) {
            /*
            r12 = this;
            r3 = 0;
            r9 = r12.mPendingCmds;
            monitor-enter(r9);
            r8 = r12.mPendingCmds;	 Catch:{ all -> 0x0052 }
            r6 = r8.iterator();	 Catch:{ all -> 0x0052 }
        L_0x000a:
            r8 = r6.hasNext();	 Catch:{ all -> 0x0052 }
            if (r8 == 0) goto L_0x005c;
        L_0x0010:
            r5 = r6.next();	 Catch:{ all -> 0x0052 }
            r5 = (com.sec.knox.container.util.DaemonConnector.ResponseQueue.PendingCmd) r5;	 Catch:{ all -> 0x0052 }
            r8 = r5.cmdNum;	 Catch:{ all -> 0x0052 }
            if (r8 != r13) goto L_0x000a;
        L_0x001a:
            r3 = r5;
            r4 = r3;
        L_0x001c:
            if (r4 != 0) goto L_0x005a;
        L_0x001e:
            r3 = new com.sec.knox.container.util.DaemonConnector$ResponseQueue$PendingCmd;	 Catch:{ all -> 0x0057 }
            r3.<init>(r13, r15);	 Catch:{ all -> 0x0057 }
            r8 = r12.mPendingCmds;	 Catch:{ all -> 0x0052 }
            r8.add(r3);	 Catch:{ all -> 0x0052 }
        L_0x0028:
            r8 = r3.availableResponseCount;	 Catch:{ all -> 0x0052 }
            r8 = r8 + -1;
            r3.availableResponseCount = r8;	 Catch:{ all -> 0x0052 }
            r8 = r3.availableResponseCount;	 Catch:{ all -> 0x0052 }
            if (r8 != 0) goto L_0x0037;
        L_0x0032:
            r8 = r12.mPendingCmds;	 Catch:{ all -> 0x0052 }
            r8.remove(r3);	 Catch:{ all -> 0x0052 }
        L_0x0037:
            monitor-exit(r9);
            r7 = 0;
            r8 = r3.responses;	 Catch:{ InterruptedException -> 0x0055 }
            r10 = (long) r14;	 Catch:{ InterruptedException -> 0x0055 }
            r9 = java.util.concurrent.TimeUnit.MILLISECONDS;	 Catch:{ InterruptedException -> 0x0055 }
            r8 = r8.poll(r10, r9);	 Catch:{ InterruptedException -> 0x0055 }
            r0 = r8;
            r0 = (com.sec.knox.container.util.DaemonEvent) r0;	 Catch:{ InterruptedException -> 0x0055 }
            r7 = r0;
        L_0x0046:
            if (r7 != 0) goto L_0x0051;
        L_0x0048:
            r8 = "DaemonConnector.ResponseQueue";
            r9 = "Timeout waiting for response";
            android.util.Log.e(r8, r9);
        L_0x0051:
            return r7;
        L_0x0052:
            r8 = move-exception;
        L_0x0053:
            monitor-exit(r9);
            throw r8;
        L_0x0055:
            r2 = move-exception;
            goto L_0x0046;
        L_0x0057:
            r8 = move-exception;
            r3 = r4;
            goto L_0x0053;
        L_0x005a:
            r3 = r4;
            goto L_0x0028;
        L_0x005c:
            r4 = r3;
            goto L_0x001c;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.sec.knox.container.util.DaemonConnector.ResponseQueue.remove(int, int, java.lang.String):com.sec.knox.container.util.DaemonEvent");
        }
    }

    public DaemonConnector(IDaemonConnectorCallbacks iDaemonConnectorCallbacks, String str, int i, String str2, int i2) {
        this.mCallbacks = iDaemonConnectorCallbacks;
        this.mSocket = str;
        this.mResponseQueue = new ResponseQueue(i);
        this.mSequenceNumber = new AtomicInteger(0);
        if (str2 == null) {
            str2 = "ECS_DaemonConnector";
        }
        this.TAG = str2;
    }

    static void appendEscaped(StringBuilder stringBuilder, String str) {
        Object obj = null;
        if (str.indexOf(32) >= 0) {
            obj = 1;
        }
        if (obj != null) {
            stringBuilder.append('\"');
        }
        int length = str.length();
        for (int i = 0; i < length; i++) {
            char charAt = str.charAt(i);
            if (charAt == '\"') {
                stringBuilder.append("\\\"");
            } else if (charAt == '\\') {
                stringBuilder.append("\\\\");
            } else {
                stringBuilder.append(charAt);
            }
        }
        if (obj != null) {
            stringBuilder.append('\"');
        }
    }

    private void listenToSocket() throws IOException {
        String str;
        Throwable e;
        Throwable th;
        LocalSocket localSocket = null;
        byte[] bArr = new byte[4096];
        int i = 0;
        try {
            LocalSocket localSocket2 = new LocalSocket();
            try {
                int read;
                localSocket2.connect(new LocalSocketAddress(this.mSocket, Namespace.RESERVED));
                InputStream inputStream = localSocket2.getInputStream();
                synchronized (this.mDaemonLock) {
                    this.mOutputStream = localSocket2.getOutputStream();
                }
                this.mCallbacks.onDaemonConnected();
                while (true) {
                    read = inputStream.read(bArr, i, 4096 - i);
                    if (read < 0) {
                        break;
                    }
                    read += i;
                    i = 0;
                    for (int i2 = 0; i2 < read; i2++) {
                        if (bArr[i2] == (byte) 0) {
                            str = new String(bArr, i, i2 - i, Charset.forName("UTF-8"));
                            log("RCV <-");
                            try {
                                DaemonEvent parseRawEvent = DaemonEvent.parseRawEvent(str);
                                if (parseRawEvent.isClassUnsolicited()) {
                                    this.mCallbackHandler.sendMessage(this.mCallbackHandler.obtainMessage(parseRawEvent.getCode(), parseRawEvent.getRawEvent()));
                                } else {
                                    this.mResponseQueue.add(parseRawEvent.getCmdNumber(), parseRawEvent);
                                }
                            } catch (IllegalArgumentException e2) {
                                log("Problem parsing message: " + str + " - " + e2);
                            }
                            i = i2 + 1;
                        }
                    }
                    if (i == 0) {
                        str = new String(bArr, i, read, Charset.forName("UTF-8"));
                        log("RCV incomplete <-");
                    }
                    if (i != read) {
                        int i3 = 4096 - i;
                        System.arraycopy(bArr, i, bArr, 0, i3);
                        i = i3;
                    } else {
                        i = 0;
                        if (bArr != null) {
                            Arrays.fill(bArr, 0, bArr.length, (byte) 0);
                        }
                    }
                }
                loge("got " + read + " reading with start = " + i);
                synchronized (this.mDaemonLock) {
                    if (this.mOutputStream != null) {
                        try {
                            loge("closing stream for " + this.mSocket);
                            this.mOutputStream.close();
                        } catch (IOException e3) {
                            loge("Failed closing output stream: " + e3);
                        }
                        this.mOutputStream = null;
                    }
                }
                if (localSocket2 != null) {
                    try {
                        localSocket2.close();
                    } catch (IOException e4) {
                        loge("Failed closing socket: " + e4);
                    }
                }
                if (bArr != null) {
                    Arrays.fill(bArr, 0, bArr.length, (byte) 0);
                }
            } catch (IOException e5) {
                e = e5;
                localSocket = localSocket2;
            } catch (Throwable th2) {
                th = th2;
                localSocket = localSocket2;
            }
        } catch (IOException e6) {
            e = e6;
            try {
                loge("Communications error: " + e);
                e.printStackTrace();
                throw e;
            } catch (Throwable th3) {
                th = th3;
                synchronized (this.mDaemonLock) {
                    if (this.mOutputStream != null) {
                        try {
                            loge("closing stream for " + this.mSocket);
                            this.mOutputStream.close();
                        } catch (IOException e32) {
                            loge("Failed closing output stream: " + e32);
                        }
                        this.mOutputStream = null;
                    }
                }
                if (localSocket != null) {
                    try {
                        localSocket.close();
                    } catch (IOException e42) {
                        loge("Failed closing socket: " + e42);
                    }
                }
                throw th;
            }
        }
    }

    private void log(String str) {
        Log.d(this.TAG, str);
    }

    private void loge(String str) {
        Log.e(this.TAG, str);
    }

    private void makeCommand(StringBuilder stringBuilder, String str, Object... objArr) throws DaemonConnectorException {
        if (str.indexOf(0) >= 0) {
            throw new IllegalArgumentException("unexpected command: " + str);
        }
        stringBuilder.append(str);
        for (Object obj : objArr) {
            String valueOf = String.valueOf(obj);
            if (valueOf.indexOf(0) >= 0) {
                throw new IllegalArgumentException("unexpected argument: " + obj);
            }
            stringBuilder.append(' ');
            appendEscaped(stringBuilder, valueOf);
        }
    }

    @Deprecated
    public ArrayList<String> doCommand(String str) throws DaemonConnectorException {
        int i = 0;
        ArrayList<String> newArrayList = Lists.newArrayList();
        DaemonEvent[] executeForList = executeForList(str, new Object[0]);
        int length = executeForList.length;
        while (i < length) {
            newArrayList.add(executeForList[i].getRawEvent());
            i++;
        }
        return newArrayList;
    }

    @Deprecated
    public String[] doListCommand(String str, int i) throws DaemonConnectorException {
        ArrayList newArrayList = Lists.newArrayList();
        DaemonEvent[] executeForList = executeForList(str, new Object[0]);
        int i2 = 0;
        while (i2 < executeForList.length - 1) {
            DaemonEvent daemonEvent = executeForList[i2];
            int code = daemonEvent.getCode();
            if (code == i) {
                newArrayList.add(daemonEvent.getMessage());
                i2++;
            } else {
                throw new DaemonConnectorException("unexpected list response " + code + " instead of " + i);
            }
        }
        DaemonEvent daemonEvent2 = executeForList[executeForList.length - 1];
        if (daemonEvent2.isClassOk()) {
            return (String[]) newArrayList.toArray(new String[newArrayList.size()]);
        }
        throw new DaemonConnectorException("unexpected final event: " + daemonEvent2);
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println();
        this.mResponseQueue.dump(fileDescriptor, printWriter, strArr);
    }

    public DaemonEvent execute(Command command) throws DaemonConnectorException {
        return execute(command.mCmd, command.mArguments.toArray());
    }

    public DaemonEvent execute(String str, Object... objArr) throws DaemonConnectorException {
        DaemonEvent[] executeForList = executeForList(str, objArr);
        if (executeForList.length == 1) {
            return executeForList[0];
        }
        throw new DaemonConnectorException("Expected exactly one response, but received " + executeForList.length);
    }

    public DaemonEvent[] execute(int i, String str, Object... objArr) throws DaemonConnectorException {
        ArrayList newArrayList = Lists.newArrayList();
        int incrementAndGet = this.mSequenceNumber.incrementAndGet();
        StringBuilder append = new StringBuilder(Integer.toString(incrementAndGet)).append(' ');
        long elapsedRealtime = SystemClock.elapsedRealtime();
        makeCommand(append, str, objArr);
        String stringBuilder = append.toString();
        if (objArr.length > 0) {
            log("SND -> {" + str + " " + objArr[0].toString() + "}");
        }
        append.append('\u0000');
        String stringBuilder2 = append.toString();
        synchronized (this.mDaemonLock) {
            if (this.mOutputStream == null) {
                throw new DaemonConnectorException("missing output stream");
            }
            try {
                this.mOutputStream.write(stringBuilder2.getBytes(Charset.forName("UTF-8")));
            } catch (Throwable e) {
                throw new DaemonConnectorException("problem sending command", e);
            }
        }
        DaemonEvent remove;
        do {
            remove = this.mResponseQueue.remove(incrementAndGet, i, stringBuilder2);
            if (remove == null) {
                loge("timed-out waiting for response");
                throw new DaemonFailureException(str, remove);
            }
            log("RMV <-");
            newArrayList.add(remove);
        } while (remove.isClassContinue());
        if (SystemClock.elapsedRealtime() - elapsedRealtime > WARN_EXECUTE_DELAY_MS) {
        }
        if (remove.isClassClientError()) {
            throw new DaemonArgumentException(str, remove);
        } else if (!remove.isClassServerError()) {
            return (DaemonEvent[]) newArrayList.toArray(new DaemonEvent[newArrayList.size()]);
        } else {
            throw new DaemonFailureException(str, remove);
        }
    }

    public DaemonEvent[] executeForList(Command command) throws DaemonConnectorException {
        return executeForList(command.mCmd, command.mArguments.toArray());
    }

    public DaemonEvent[] executeForList(String str, Object... objArr) throws DaemonConnectorException {
        return execute(DEFAULT_TIMEOUT, str, objArr);
    }

    public boolean handleMessage(Message message) {
        String str = (String) message.obj;
        try {
            if (!this.mCallbacks.onEvent(message.what, str, DaemonEvent.unescapeArgs(str))) {
                log(String.format("Unhandled event '%s'", new Object[]{str}));
            }
        } catch (Exception e) {
            loge("Error handling '" + str + "': " + e);
        }
        return true;
    }

    public void monitor() {
    }

    public void run() {
        Thread handlerThread = new HandlerThread(this.TAG + ".CallbackHandler");
        handlerThread.start();
        this.mCallbackHandler = new Handler(handlerThread.getLooper(), this);
        while (true) {
            try {
                listenToSocket();
            } catch (Exception e) {
                loge("Error in DaemonConnector: " + e);
                SystemClock.sleep(5000);
            }
        }
    }
}
