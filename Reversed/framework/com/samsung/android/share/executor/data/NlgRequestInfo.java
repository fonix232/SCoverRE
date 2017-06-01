package com.samsung.android.share.executor.data;

import java.util.HashMap;
import java.util.Map.Entry;

public class NlgRequestInfo {
    private String mRequestStateId;
    private HashMap<String, String> mResultParams = new HashMap();
    private HashMap<String, Attribute> mScreenParams = new HashMap();

    private static class Attribute {
        public String name;
        public String value;

        private Attribute(String str, String str2) {
            this.name = str;
            this.value = str2;
        }
    }

    public NlgRequestInfo(String str) throws IllegalArgumentException {
        if (str == null || str.trim().isEmpty()) {
            throw new IllegalArgumentException("The input parameter is null or empty.");
        }
        this.mRequestStateId = str;
    }

    public NlgRequestInfo addResultParam(String str, String str2) {
        if (str == null || str.trim().isEmpty() || str2 == null || str2.trim().isEmpty()) {
            throw new IllegalArgumentException("The input parameter is null or empty");
        } else if (this.mResultParams.containsKey(str)) {
            throw new IllegalArgumentException("The result parameter name is duplicated." + str);
        } else {
            this.mResultParams.put(str, str2);
            return this;
        }
    }

    public NlgRequestInfo addScreenParam(String str, String str2, String str3) throws IllegalArgumentException {
        if (str == null || str.trim().isEmpty()) {
            throw new IllegalArgumentException("The input parameter is null or empty.");
        } else if (this.mScreenParams.containsKey(str)) {
            throw new IllegalArgumentException("The screen parameter name is duplicated. " + str);
        } else {
            this.mScreenParams.put(str, new Attribute(str2, str3));
            return this;
        }
    }

    public String getRequestStateId() {
        return this.mRequestStateId;
    }

    public HashMap<String, String> getResultParams() {
        return this.mResultParams;
    }

    public HashMap<String, Attribute> getScreenParams() {
        return this.mScreenParams;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\"requestedStateId\":\"").append(this.mRequestStateId).append("\"");
        if (this.mScreenParams.size() > 0) {
            stringBuilder.append(",\"screenParameters\":[");
            for (Entry entry : this.mScreenParams.entrySet()) {
                stringBuilder.append("{\"parameterName\":\"").append((String) entry.getKey()).append("\",").append("\"attributeName\":\"").append(((Attribute) entry.getValue()).name).append("\",\"attributeValue\":\"").append(((Attribute) entry.getValue()).value).append("\"},");
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            stringBuilder.append("]");
        }
        if (this.mResultParams.size() > 0) {
            stringBuilder.append(",\"resultParameters\":[");
            for (Entry entry2 : this.mResultParams.entrySet()) {
                stringBuilder.append("{\"name\":\"").append((String) entry2.getKey()).append("\",\"value\":\"").append((String) entry2.getValue()).append("\"},");
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            stringBuilder.append("]");
        }
        return stringBuilder.toString();
    }
}
