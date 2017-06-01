package com.samsung.android.share.executor;

import com.samsung.android.media.SemSoundAssistantManager;
import com.samsung.android.share.SShareConstants;
import com.samsung.android.smartface.SmartFaceManager;
import org.json.JSONObject;

public class Command {
    public String mCommand = null;
    public String mContent = null;
    public String mRequestId = null;
    public String mVersion = null;

    public Command(String str) {
        try {
            JSONObject jSONObject = new JSONObject(str);
            if (jSONObject.has(SemSoundAssistantManager.VERSION) && jSONObject.has(SShareConstants.EXTRA_CHOOSER_EM_COMMAND)) {
                if (jSONObject.has("content")) {
                    this.mVersion = jSONObject.getString(SemSoundAssistantManager.VERSION);
                    if (jSONObject.has("requestId")) {
                        this.mRequestId = jSONObject.getString("requestId");
                    } else {
                        this.mRequestId = SmartFaceManager.PAGE_BOTTOM;
                    }
                    this.mContent = jSONObject.get("content").toString();
                    this.mCommand = jSONObject.getString(SShareConstants.EXTRA_CHOOSER_EM_COMMAND);
                    return;
                }
            }
            throw new IllegalArgumentException("Invalid command!");
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public Command(String str, String str2, String str3, String str4) {
        this.mVersion = str;
        this.mCommand = str2;
        this.mRequestId = str3;
        this.mContent = str4;
    }

    public String getCommand() {
        return this.mCommand;
    }

    public String getContent() {
        return this.mContent;
    }

    public String getRequestId() {
        return this.mRequestId;
    }

    public String getVersion() {
        return this.mVersion;
    }

    public void setCommand(String str) {
        this.mCommand = str;
    }

    public void setContent(String str) {
        this.mContent = str;
    }

    public void setRequestId(String str) {
        this.mRequestId = str;
    }

    public void setVersion(String str) {
        this.mVersion = str;
    }

    public String toJson() {
        return (((("{" + "\"version\":\"" + this.mVersion + "\",") + "\"command\":\"" + this.mCommand + "\",") + "\"requestId\":\"" + this.mRequestId + "\",") + "\"content\":" + this.mContent) + "}";
    }
}
