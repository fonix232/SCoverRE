package com.samsung.android.share.executor.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class State implements Parcelable {
    public static final Creator<State> CREATOR = new C02511();
    private String appName;
    private Boolean isExecuted;
    private Boolean isLandingState;
    private Boolean isLastState;
    private List<Parameter> parameters = new ArrayList();
    private String ruleId;
    private Integer seqNum;
    private String specVer = "1.0";
    private String stateId;
    private String subIntent;

    static class C02511 implements Creator<State> {
        C02511() {
        }

        public State createFromParcel(Parcel parcel) {
            return new State(parcel);
        }

        public State[] newArray(int i) {
            return new State[i];
        }
    }

    public State(Parcel parcel) {
        boolean z = true;
        this.specVer = parcel.readString();
        this.seqNum = Integer.valueOf(parcel.readInt());
        this.isExecuted = Boolean.valueOf(parcel.readByte() != (byte) 0);
        this.appName = parcel.readString();
        this.ruleId = parcel.readString();
        this.stateId = parcel.readString();
        this.isLandingState = Boolean.valueOf(parcel.readByte() != (byte) 0);
        if (parcel.readByte() == (byte) 0) {
            z = false;
        }
        this.isLastState = Boolean.valueOf(z);
        this.subIntent = parcel.readString();
        this.parameters = parcel.createTypedArrayList(Parameter.CREATOR);
    }

    public State(String str, Integer num, Boolean bool, String str2, String str3, String str4, Boolean bool2, Boolean bool3, String str5, List<Parameter> list) {
        this.specVer = str;
        this.seqNum = num;
        this.isExecuted = bool;
        this.appName = str2;
        this.ruleId = str3;
        this.stateId = str4;
        this.isLandingState = bool2;
        this.isLastState = bool3;
        this.subIntent = str5;
        this.parameters = list;
    }

    public int describeContents() {
        return 0;
    }

    public String getAppName() {
        return this.appName;
    }

    public Map<String, Parameter> getParamMap() {
        Map<String, Parameter> hashMap = new HashMap();
        for (Parameter parameter : this.parameters) {
            hashMap.put(parameter.getParameterName(), parameter);
        }
        return hashMap;
    }

    public List<Parameter> getParameters() {
        return this.parameters;
    }

    public String getRuleId() {
        return this.ruleId;
    }

    public Integer getSeqNum() {
        return this.seqNum;
    }

    public String getStateId() {
        return this.stateId;
    }

    public String getSubIntent() {
        return this.subIntent;
    }

    public Boolean isExecuted() {
        return this.isExecuted;
    }

    public Boolean isLandingState() {
        return this.isLandingState;
    }

    public Boolean isLastState() {
        return this.isLastState;
    }

    public void setAppName(String str) {
        this.appName = str;
    }

    public void setExecuted(Boolean bool) {
        this.isExecuted = bool;
    }

    public void setLandingState(Boolean bool) {
        this.isLandingState = bool;
    }

    public void setLastState(Boolean bool) {
        this.isLastState = bool;
    }

    public void setParameters(List<Parameter> list) {
        this.parameters = list;
    }

    public void setRuleId(String str) {
        this.ruleId = str;
    }

    public void setSeqNum(Integer num) {
        this.seqNum = num;
    }

    public void setStateId(String str) {
        this.stateId = str;
    }

    public void setSubIntent(String str) {
        this.subIntent = str;
    }

    public void writeToParcel(Parcel parcel, int i) {
        int i2 = 1;
        parcel.writeString(this.specVer);
        parcel.writeInt(this.seqNum.intValue());
        parcel.writeByte((byte) (this.isExecuted.booleanValue() ? 1 : 0));
        parcel.writeString(this.appName);
        parcel.writeString(this.ruleId);
        parcel.writeString(this.stateId);
        parcel.writeByte((byte) (this.isLandingState.booleanValue() ? 1 : 0));
        if (!this.isLastState.booleanValue()) {
            i2 = 0;
        }
        parcel.writeByte((byte) i2);
        parcel.writeString(this.subIntent);
        parcel.writeTypedList(this.parameters);
    }
}
