package com.samsung.android.share.executor;

import com.samsung.android.fingerprint.FingerprintManager;
import com.samsung.android.share.executor.data.CHObject;
import com.samsung.android.share.executor.data.ParamFilling;
import com.samsung.android.share.executor.data.ScreenParameter;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

class ParamFillingReader {
    ParamFillingReader() {
    }

    public static ParamFilling read(String str) throws IllegalArgumentException {
        List arrayList = new ArrayList();
        List arrayList2 = new ArrayList();
        try {
            JSONObject jSONObject = new JSONObject(str);
            String string = jSONObject.getString("utterance");
            String string2 = jSONObject.getString("intent");
            String string3 = jSONObject.getString(FingerprintManager.CLIENTSPEC_KEY_APPNAME);
            JSONArray jSONArray = jSONObject.getJSONArray("screenStates");
            for (int i = 0; i < jSONArray.length(); i++) {
                arrayList.add(jSONArray.optString(i));
            }
            JSONArray jSONArray2 = jSONObject.getJSONArray("screenParameters");
            for (int i2 = 0; i2 < jSONArray2.length(); i2++) {
                JSONObject jSONObject2 = jSONArray2.getJSONObject(i2);
                ScreenParameter screenParameter = new ScreenParameter();
                if (jSONObject2.has("slotType")) {
                    screenParameter.setSlotType(jSONObject2.getString("slotType"));
                } else {
                    screenParameter.setSlotType("");
                }
                if (jSONObject2.has("slotName")) {
                    screenParameter.setSlotName(jSONObject2.getString("slotName"));
                } else {
                    screenParameter.setSlotName("");
                }
                if (jSONObject2.has("slotValue")) {
                    screenParameter.setSlotValue(jSONObject2.getString("slotValue"));
                } else {
                    screenParameter.setSlotValue("");
                }
                if (jSONObject2.has("CH_ObjectType")) {
                    screenParameter.setCHObjectType(jSONObject2.getString("CH_ObjectType"));
                } else {
                    screenParameter.setCHObjectType("");
                }
                if (jSONObject2.has("CH_Objects")) {
                    List arrayList3 = new ArrayList();
                    JSONArray jSONArray3 = jSONObject2.getJSONArray("CH_Objects");
                    for (int i3 = 0; i3 < jSONArray3.length(); i3++) {
                        JSONObject jSONObject3 = jSONArray3.getJSONObject(i3);
                        CHObject cHObject = new CHObject();
                        if (jSONObject3.has("CH_Type")) {
                            cHObject.setCHType(jSONObject3.getString("CH_Type"));
                        } else {
                            cHObject.setCHType("");
                        }
                        if (jSONObject3.has("CH_Value")) {
                            cHObject.setCHValue(jSONObject3.getString("CH_Value"));
                        } else {
                            cHObject.setCHValue("");
                        }
                        if (jSONObject3.has("CH_ValueType")) {
                            cHObject.setCHValueType(jSONObject3.getString("CH_ValueType"));
                        } else {
                            cHObject.setCHValueType("");
                        }
                        arrayList3.add(cHObject);
                    }
                    screenParameter.setCHObjects(arrayList3);
                } else {
                    screenParameter.setCHObjects(null);
                }
                if (jSONObject2.has("parameterName")) {
                    screenParameter.setParameterName(jSONObject2.getString("parameterName"));
                } else {
                    screenParameter.setParameterName("");
                }
                if (jSONObject2.has("parameterType")) {
                    screenParameter.setParameterType(jSONObject2.getString("parameterType"));
                } else {
                    screenParameter.setParameterType("");
                }
                arrayList2.add(screenParameter);
            }
            return new ParamFilling(string, string2, string3, arrayList, arrayList2);
        } catch (Throwable e) {
            throw new IllegalArgumentException(e.toString());
        }
    }
}
