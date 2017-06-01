package com.samsung.android.share.executor.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParamFilling implements Parcelable {
    public static final Creator<ParamFilling> CREATOR = new C02481();
    String appName;
    String intent;
    List<ScreenParameter> mScreenParameters = new ArrayList();
    List<String> screenStates = new ArrayList();
    String utterance;

    static class C02481 implements Creator<ParamFilling> {
        C02481() {
        }

        public ParamFilling createFromParcel(Parcel parcel) {
            return new ParamFilling(parcel);
        }

        public ParamFilling[] newArray(int i) {
            return new ParamFilling[i];
        }
    }

    protected ParamFilling(Parcel parcel) {
        this.utterance = parcel.readString();
        this.intent = parcel.readString();
        this.appName = parcel.readString();
        this.screenStates = parcel.createStringArrayList();
        this.mScreenParameters = parcel.createTypedArrayList(ScreenParameter.CREATOR);
    }

    public ParamFilling(String str, String str2, String str3, List<String> list, List<ScreenParameter> list2) {
        this.utterance = str;
        this.intent = str2;
        this.appName = str3;
        this.screenStates = list;
        this.mScreenParameters = list2;
    }

    public static Creator<ParamFilling> getCREATOR() {
        return CREATOR;
    }

    public int describeContents() {
        return 0;
    }

    public String getAppName() {
        return this.appName;
    }

    public String getIntent() {
        return this.intent;
    }

    public Map<String, ScreenParameter> getScreenParamMap() {
        Map<String, ScreenParameter> hashMap = new HashMap();
        for (ScreenParameter screenParameter : this.mScreenParameters) {
            hashMap.put(screenParameter.getParameterName(), screenParameter);
        }
        return hashMap;
    }

    public List<ScreenParameter> getScreenParameters() {
        return this.mScreenParameters;
    }

    public List<String> getScreenStates() {
        return this.screenStates;
    }

    public String getUtterance() {
        return this.utterance;
    }

    public void setAppName(String str) {
        this.appName = str;
    }

    public void setIntent(String str) {
        this.intent = str;
    }

    public void setScreenParameters(List<ScreenParameter> list) {
        this.mScreenParameters = list;
    }

    public void setScreenStates(List<String> list) {
        this.screenStates = list;
    }

    public void setUtterance(String str) {
        this.utterance = str;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.utterance);
        parcel.writeString(this.intent);
        parcel.writeString(this.appName);
        parcel.writeStringList(this.screenStates);
        parcel.writeTypedList(this.mScreenParameters);
    }
}
