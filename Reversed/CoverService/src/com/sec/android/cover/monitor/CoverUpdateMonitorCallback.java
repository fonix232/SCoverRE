package com.sec.android.cover.monitor;

import android.net.Uri;
import com.sec.android.cover.monitor.CoverUpdateMonitor.BatteryStatus;
import com.sec.android.cover.monitor.CoverUpdateMonitor.RemoteViewInfo;

public class CoverUpdateMonitorCallback {
    public void onTimeChanged() {
    }

    public void onScreenTurnedOn() {
    }

    public void onScreenTurnedOff() {
    }

    public void onBatteryLow() {
    }

    public void onBatteryCritical() {
    }

    public void onPowerConnectionUpdate(boolean connected) {
    }

    public void onRefreshBatteryInfo(BatteryStatus status) {
    }

    public void onRemoteViewUpdated(RemoteViewInfo remoteViewInfo) {
    }

    public void onVolumeChanged(int streamType, int val) {
    }

    public void onUserSwitched(int newUserId, int oldUserId) {
    }

    public void onContentChanged(Uri uri) {
    }

    public void onBixbyStateChanged(int state) {
    }
}
