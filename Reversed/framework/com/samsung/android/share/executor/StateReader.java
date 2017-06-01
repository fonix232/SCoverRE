package com.samsung.android.share.executor;

import com.samsung.android.fingerprint.FingerprintManager;
import com.samsung.android.mateservice.common.BundleArgs;
import com.samsung.android.share.executor.data.CHObject;
import com.samsung.android.share.executor.data.Parameter;
import com.samsung.android.share.executor.data.State;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

class StateReader {
    StateReader() {
    }

    public static State read(String str) throws IllegalArgumentException {
        List arrayList = new ArrayList();
        try {
            String string;
            Boolean valueOf;
            String string2;
            JSONObject jSONObject = new JSONObject(str);
            if (jSONObject.has("specVer")) {
                string = jSONObject.getString("specVer");
            } else {
                string = "1.0";
            }
            Integer valueOf2 = Integer.valueOf(jSONObject.getInt("seqNum"));
            Boolean valueOf3 = Boolean.valueOf(jSONObject.getBoolean("isExecuted"));
            String string3 = jSONObject.getString(FingerprintManager.CLIENTSPEC_KEY_APPNAME);
            String string4 = jSONObject.getString(BundleArgs.STATE_ID);
            String string5 = jSONObject.getString("ruleId");
            Boolean valueOf4 = Boolean.valueOf(jSONObject.getBoolean("isLandingState"));
            if (jSONObject.has("isLastState")) {
                valueOf = Boolean.valueOf(jSONObject.getBoolean("isLastState"));
            } else {
                valueOf = Boolean.valueOf(false);
            }
            if (jSONObject.has("subIntent")) {
                string2 = jSONObject.getString("subIntent");
            } else {
                string2 = "";
            }
            JSONArray jSONArray = jSONObject.getJSONArray("parameters");
            for (int i = 0; i < jSONArray.length(); i++) {
                JSONObject jSONObject2 = jSONArray.getJSONObject(i);
                Parameter parameter = new Parameter();
                if (jSONObject2.has("slotType")) {
                    parameter.setSlotType(jSONObject2.getString("slotType"));
                } else {
                    parameter.setSlotType("");
                }
                if (jSONObject2.has("slotName")) {
                    parameter.setSlotName(jSONObject2.getString("slotName"));
                } else {
                    parameter.setSlotName("");
                }
                if (jSONObject2.has("slotValue")) {
                    parameter.setSlotValue(jSONObject2.getString("slotValue"));
                } else {
                    parameter.setSlotValue("");
                }
                if (jSONObject2.has("slotValueType")) {
                    parameter.setSlotValueType(jSONObject2.getString("slotValueType"));
                } else {
                    parameter.setSlotValueType("");
                }
                if (jSONObject2.has("CH_ObjectType")) {
                    parameter.setCHObjectType(jSONObject2.getString("CH_ObjectType"));
                } else {
                    parameter.setCHObjectType("");
                }
                if (jSONObject2.has("CH_Objects")) {
                    List arrayList2 = new ArrayList();
                    JSONArray jSONArray2 = jSONObject2.getJSONArray("CH_Objects");
                    for (int i2 = 0; i2 < jSONArray2.length(); i2++) {
                        JSONObject jSONObject3 = jSONArray2.getJSONObject(i2);
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
                        arrayList2.add(cHObject);
                    }
                    parameter.setCHObjects(arrayList2);
                } else {
                    parameter.setCHObjects(null);
                }
                if (jSONObject2.has("parameterName")) {
                    parameter.setParameterName(jSONObject2.getString("parameterName"));
                } else {
                    parameter.setParameterName("");
                }
                if (jSONObject2.has("parameterType")) {
                    parameter.setParameterType(jSONObject2.getString("parameterType"));
                } else {
                    parameter.setParameterType("");
                }
                if (jSONObject2.has("isMandatory")) {
                    Parameter parameter2 = parameter;
                    parameter2.setIsMandatory(Boolean.valueOf(jSONObject2.getBoolean("isMandatory")));
                } else {
                    parameter.setIsMandatory(Boolean.valueOf(false));
                }
                arrayList.add(parameter);
            }
            return new State(string, valueOf2, valueOf3, string3, string5, string4, valueOf4, valueOf, string2, arrayList);
        } catch (Throwable e) {
            throw new IllegalArgumentException(e.toString());
        }
    }
}
