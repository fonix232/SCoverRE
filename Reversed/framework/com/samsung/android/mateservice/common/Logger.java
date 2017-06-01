package com.samsung.android.mateservice.common;

import android.os.SystemClock;
import com.samsung.android.mateservice.util.UtilLog;
import java.util.LinkedList;

public class Logger implements LoggerContract, Dump {
    private static final long TIME_DIFF = 86400000;
    private long mLastTimeStamp = 0;
    private final int mMaxCount;
    private final LinkedList<Node> mNodes = new LinkedList();

    private static class Node {
        String msg;
        long timeStamp;

        Node(String str) {
            this.timeStamp = System.currentTimeMillis();
            this.msg = str;
        }

        Node(String str, long j) {
            this.msg = str;
            this.timeStamp = j;
        }
    }

    public Logger(int i) {
        this.mMaxCount = i;
    }

    private void appendHistoryNode(Node node) {
        synchronized (this.mNodes) {
            long elapsedRealtime = SystemClock.elapsedRealtime();
            if (isExpired(elapsedRealtime)) {
                this.mNodes.clear();
            } else if (this.mNodes.size() >= this.mMaxCount) {
                this.mNodes.pop();
            }
            this.mLastTimeStamp = elapsedRealtime;
            this.mNodes.push(node);
        }
    }

    private boolean isExpired(long j) {
        return j - this.mLastTimeStamp >= TIME_DIFF;
    }

    public void append(long j, String str, Object... objArr) {
        if (this.mMaxCount != 0) {
            if (objArr != null && objArr.length > 0) {
                str = UtilLog.getMsg(str, objArr);
            }
            appendHistoryNode(new Node(str, j));
        }
    }

    public void append(String str, Object... objArr) {
        if (this.mMaxCount != 0) {
            if (objArr != null && objArr.length > 0) {
                str = UtilLog.getMsg(str, objArr);
            }
            appendHistoryNode(new Node(str));
        }
    }

    public void getDump(StringBuilder stringBuilder) {
        stringBuilder.append("\n---- history info.\n");
        synchronized (this.mNodes) {
            for (int size = this.mNodes.size() - 1; size >= 0; size--) {
                Node node = (Node) this.mNodes.get(size);
                if (node != null) {
                    stringBuilder.append(UtilLog.getDateString(node.timeStamp));
                    stringBuilder.append("  ");
                    stringBuilder.append(node.msg);
                    stringBuilder.append("\n");
                }
            }
            if (!FwDependency.isProductDev()) {
                this.mNodes.clear();
            }
        }
    }
}
