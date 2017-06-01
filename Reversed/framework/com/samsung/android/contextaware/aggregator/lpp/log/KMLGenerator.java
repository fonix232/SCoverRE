package com.samsung.android.contextaware.aggregator.lpp.log;

import android.location.Location;
import android.util.Log;
import com.samsung.android.bridge.multiwindow.MultiWindowManagerBridge;
import java.util.ArrayList;

public class KMLGenerator {
    private static String TAG = "KMLGenerator";
    String mCoordinates = MultiWindowManagerBridge.SNAP_WINDOW_VISBLE_CALLER_UNSET;
    ArrayList<Location> mGPSCoordinates = new ArrayList();
    String mLineStyle = "ffffffff";
    String mPlaceID = "temp";
    String mPolyStyleColor = "00ffffff";
    int mWidth = 10;

    public KMLGenerator(String str) {
        SetPlaceID(str);
    }

    public void AddCoordinate(ArrayList<Location> arrayList) {
        Log.m29d(TAG, "[KMLGen]LppLocation size : " + arrayList.size());
        for (int i = 0; i < arrayList.size(); i++) {
            this.mCoordinates += ((Location) arrayList.get(i)).getLongitude() + "," + ((Location) arrayList.get(i)).getLatitude() + "," + ((Location) arrayList.get(i)).getAltitude() + " ";
        }
    }

    public void AddCoordinateRT(Location location) {
        this.mCoordinates += location.getLongitude() + "," + location.getLatitude() + "," + location.getAltitude() + " ";
    }

    public void AddGPSCoordinate(ArrayList<Location> arrayList) {
        Log.m29d(TAG, "[KMLGen]LppLocation size : " + arrayList.size());
        for (int i = 0; i < arrayList.size(); i++) {
            this.mGPSCoordinates.add(new Location((Location) arrayList.get(i)));
        }
    }

    public String GenerateLineString() {
        return MultiWindowManagerBridge.SNAP_WINDOW_VISBLE_CALLER_UNSET;
    }

    public void SetLineStyle(String str) {
        this.mLineStyle = str;
    }

    void SetLineWidth(int i) {
        this.mWidth = i;
    }

    void SetPlaceID(String str) {
        this.mPlaceID = str;
    }

    void SetPolyStyleColor(String str) {
        this.mPolyStyleColor = str;
    }
}
