package com.samsung.android.share.executor.data;

import java.util.LinkedHashSet;

public class ScreenStateInfo {
    public static final ScreenStateInfo STATE_NOT_APPLICABLE = null;
    String mCallerAppName = "";
    LinkedHashSet<String> mStateList;

    public ScreenStateInfo(String str) throws IllegalArgumentException {
        if (str == null || str.trim().isEmpty()) {
            throw new IllegalArgumentException("The input parameter is null or empty.");
        }
        this.mStateList = new LinkedHashSet();
        this.mStateList.add(str);
    }

    public ScreenStateInfo(LinkedHashSet<String> linkedHashSet) throws IllegalArgumentException {
        if (linkedHashSet == null || linkedHashSet.isEmpty()) {
            throw new IllegalArgumentException("The input parameter is null or empty.");
        }
        this.mStateList = linkedHashSet;
    }

    public ScreenStateInfo addState(String str) throws IllegalArgumentException {
        if (str == null || str.trim().isEmpty()) {
            throw new IllegalArgumentException("The input parameter is null or empty.");
        } else if (this.mStateList.contains(str)) {
            throw new IllegalArgumentException("The screen parameter name is duplicated. " + str);
        } else {
            this.mStateList.add(str);
            return this;
        }
    }

    public String getCallerAppName() {
        return this.mCallerAppName;
    }

    public LinkedHashSet<String> getStates() {
        return this.mStateList;
    }

    public ScreenStateInfo setCallerAppName(String str) throws IllegalArgumentException {
        if (str == null || str.trim().isEmpty()) {
            throw new IllegalArgumentException("The input parameter is null or empty.");
        }
        this.mCallerAppName = str;
        return this;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        if (!this.mCallerAppName.isEmpty()) {
            stringBuilder.append("\"callerAppName\":");
            stringBuilder.append("\"").append(this.mCallerAppName).append("\",");
        }
        stringBuilder.append("\"stateIds\":[");
        if (this.mStateList == null || this.mStateList.size() <= 0) {
            return null;
        }
        for (String append : this.mStateList) {
            stringBuilder.append("\"").append(append).append("\",");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        stringBuilder.append("]");
        return stringBuilder.toString();
    }
}
