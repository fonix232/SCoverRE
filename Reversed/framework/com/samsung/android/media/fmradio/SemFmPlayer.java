package com.samsung.android.media.fmradio;

import android.content.Context;
import android.media.AudioManager;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.util.Log;
import com.samsung.android.media.fmradio.internal.IFMPlayer;
import com.samsung.android.media.fmradio.internal.IFMPlayer.Stub;

public class SemFmPlayer {
    public static final int AUDIO_MODE_MONO = 8;
    public static final int AUDIO_MODE_STEREO = 9;
    private static final boolean DEBUG = true;
    private static final String LOG_TAG = "FmPlayer";
    public static final int OFF_AIRPLANE_MODE_SET = 3;
    public static final int OFF_BATTERY_LOW = 7;
    public static final int OFF_CALL_ACTIVE = 1;
    public static final int OFF_DEVICE_SHUTDOWN = 6;
    public static final int OFF_EAR_PHONE_DISCONNECT = 2;
    public static final int OFF_NORMAL = 0;
    public static final int OFF_PAUSE_COMMAND = 5;
    public static final int OFF_STOP_COMMAND = 4;
    static Context mContext;
    private AudioManager mAudioManager;
    private IFMPlayer mPlayer = Stub.asInterface(ServiceManager.getService("FMPlayer"));

    public SemFmPlayer(Context context) {
        mContext = context;
        this.mAudioManager = (AudioManager) context.getSystemService("audio");
        log("Player created :" + this.mPlayer);
    }

    private void checkBusy() throws SemFmPlayerException {
        int i = 0;
        try {
            i = this.mPlayer.isBusy();
        } catch (RemoteException e) {
            remoteError(e);
        }
        if (i == 1) {
            throw new SemFmPlayerScanningException("Player is scanning channel", new Throwable("Player is busy in scanning. Use cancelScan to stop scanning"));
        }
    }

    private void checkOnStatus() throws SemFmPlayerException {
        if (!isRadioEnabled()) {
            throw new SemFmPlayerNotEnabledException("Player is not ON.Call on() method to start player", new Throwable("Player is not ON. use method on() to switch on FM player"));
        }
    }

    private void remoteError(RemoteException remoteException) throws SemFmPlayerException {
        remoteException.printStackTrace();
        throw new SemFmPlayerNotEnabledException("Radio service is not running restart the phone.", remoteException.fillInStackTrace());
    }

    public void addListener(SemFmEventListener semFmEventListener) throws SemFmPlayerException {
        if (semFmEventListener != null) {
            try {
                this.mPlayer.setListener(semFmEventListener.callback);
            } catch (RemoteException e) {
                remoteError(e);
            }
        }
    }

    public void cancelAFSwitching() throws SemFmPlayerException {
        try {
            this.mPlayer.cancelAFSwitching();
        } catch (RemoteException e) {
            remoteError(e);
        }
    }

    public boolean cancelScan() throws SemFmPlayerException {
        try {
            return this.mPlayer.cancelScan();
        } catch (RemoteException e) {
            remoteError(e);
            return false;
        }
    }

    public void cancelSeek() throws SemFmPlayerException {
        try {
            this.mPlayer.cancelSeek();
        } catch (RemoteException e) {
            remoteError(e);
        }
    }

    public boolean disableRadio() throws SemFmPlayerException {
        boolean z = false;
        try {
            z = this.mPlayer.off();
        } catch (RemoteException e) {
            remoteError(e);
        }
        return z;
    }

    public boolean enableRadio() throws SemFmPlayerException {
        if (isAirPlaneMode()) {
            throw new SemAirPlaneModeEnabledException("AirPlane mode is on.", new Throwable("AirPlane mode is on."));
        }
        boolean z = false;
        if ("factory".equalsIgnoreCase(SystemProperties.get("ro.factory.factory_binary", "Unknown"))) {
            try {
                z = this.mPlayer.on_in_testmode();
            } catch (RemoteException e) {
                remoteError(e);
            }
            return z;
        } else if (isTvOutPlugged()) {
            throw new SemTvOutConnectedException("TV out is on", new Throwable("TV out is on."));
        } else if (isHeadsetPlugged()) {
            try {
                z = this.mPlayer.on();
            } catch (RemoteException e2) {
                remoteError(e2);
            }
            if (!isBatteryLow()) {
                return z;
            }
            throw new SemLowBatteryException("Battery is low.", new Throwable("Batterys is low."));
        } else {
            throw new SemHeadsetNotConnectedException("Headset is not presents.", new Throwable("Headset is not presents."));
        }
    }

    protected void finalize() throws Throwable {
        super.finalize();
        this.mAudioManager = null;
        this.mPlayer = null;
    }

    public long getCurrentChannel() throws SemFmPlayerException {
        checkOnStatus();
        try {
            checkBusy();
            return this.mPlayer.getCurrentChannel();
        } catch (RemoteException e) {
            remoteError(e);
            return -1;
        }
    }

    public long[] getLastScanResult() throws SemFmPlayerException {
        if (isScanning()) {
            return null;
        }
        try {
            return this.mPlayer.getLastScanResult();
        } catch (RemoteException e) {
            remoteError(e);
            return null;
        }
    }

    public long getMaxVolume() throws SemFmPlayerException {
        try {
            return this.mPlayer.getMaxVolume();
        } catch (RemoteException e) {
            remoteError(e);
            return -1;
        }
    }

    public long getPlayedFreq() throws SemFmPlayerException {
        try {
            return this.mPlayer.getPlayedFreq();
        } catch (RemoteException e) {
            remoteError(e);
            return -1;
        }
    }

    public int getTunningParameter(String str, int i) throws SemFmPlayerException {
        int i2 = i;
        if (isRadioEnabled()) {
            try {
                i2 = this.mPlayer.getIntegerTunningParameter(str, i);
            } catch (RemoteException e) {
                remoteError(e);
            }
        }
        return i2;
    }

    public long getTunningParameter(String str, long j) throws SemFmPlayerException {
        long j2 = j;
        if (isRadioEnabled()) {
            try {
                j2 = this.mPlayer.getLongTunningParameter(str, j);
            } catch (RemoteException e) {
                remoteError(e);
            }
        }
        return j2;
    }

    public String getTunningParameter(String str, String str2) throws SemFmPlayerException {
        String str3 = str2;
        if (isRadioEnabled()) {
            try {
                str3 = this.mPlayer.getStringTunningParameter(str, str2);
            } catch (RemoteException e) {
                remoteError(e);
            }
        }
        return str3;
    }

    public long getVolume() throws SemFmPlayerException {
        try {
            return this.mPlayer.getVolume();
        } catch (RemoteException e) {
            remoteError(e);
            return -1;
        }
    }

    public boolean isAirPlaneMode() throws SemFmPlayerException {
        try {
            return this.mPlayer.isAirPlaneMode();
        } catch (RemoteException e) {
            remoteError(e);
            return false;
        }
    }

    public boolean isAlternateFrequencyEnabled() throws SemFmPlayerException {
        try {
            return this.mPlayer.isAFEnable();
        } catch (RemoteException e) {
            remoteError(e);
            return false;
        }
    }

    public boolean isBatteryLow() throws SemFmPlayerException {
        try {
            return this.mPlayer.isBatteryLow();
        } catch (RemoteException e) {
            remoteError(e);
            return false;
        }
    }

    public boolean isHeadsetPlugged() throws SemFmPlayerException {
        try {
            return this.mPlayer.isHeadsetPlugged();
        } catch (RemoteException e) {
            remoteError(e);
            return false;
        }
    }

    public boolean isRadioDataSystemEnabled() throws SemFmPlayerException {
        try {
            return this.mPlayer.isRDSEnable();
        } catch (RemoteException e) {
            remoteError(e);
            return false;
        }
    }

    public boolean isRadioDomainNameSystemEnabled() throws SemFmPlayerException {
        try {
            return this.mPlayer.isDNSEnable();
        } catch (RemoteException e) {
            remoteError(e);
            return false;
        }
    }

    public boolean isRadioEnabled() throws SemFmPlayerException {
        try {
            return this.mPlayer.isOn();
        } catch (RemoteException e) {
            remoteError(e);
            return false;
        }
    }

    public boolean isScanning() throws SemFmPlayerException {
        try {
            return this.mPlayer.isScanning();
        } catch (RemoteException e) {
            remoteError(e);
            return false;
        }
    }

    public boolean isSeeking() throws SemFmPlayerException {
        try {
            return this.mPlayer.isSeeking();
        } catch (RemoteException e) {
            remoteError(e);
            return false;
        }
    }

    public boolean isSoftmuteEnabled() throws SemFmPlayerException {
        boolean z = false;
        try {
            z = this.mPlayer.getSoftMuteMode();
        } catch (RemoteException e) {
            remoteError(e);
        }
        return z;
    }

    public boolean isTvOutPlugged() throws SemFmPlayerException {
        try {
            return this.mPlayer.isTvOutPlugged();
        } catch (RemoteException e) {
            remoteError(e);
            return false;
        }
    }

    public void log(String str) {
        Log.i(LOG_TAG, str);
    }

    public void removeListener(SemFmEventListener semFmEventListener) throws SemFmPlayerException {
        if (semFmEventListener != null) {
            try {
                this.mPlayer.removeListener(semFmEventListener.callback);
            } catch (RemoteException e) {
                remoteError(e);
            }
        }
    }

    public long searchAll() throws SemFmPlayerException {
        checkOnStatus();
        try {
            checkBusy();
            return this.mPlayer.searchAll();
        } catch (RemoteException e) {
            remoteError(e);
            return -1;
        }
    }

    public long searchDown() throws SemFmPlayerException {
        checkOnStatus();
        try {
            checkBusy();
            return this.mPlayer.searchDown();
        } catch (RemoteException e) {
            remoteError(e);
            return -1;
        }
    }

    public long searchUp() throws SemFmPlayerException {
        checkOnStatus();
        try {
            checkBusy();
            return this.mPlayer.searchUp();
        } catch (RemoteException e) {
            remoteError(e);
            return -1;
        }
    }

    public long seekDown() throws SemFmPlayerException {
        checkOnStatus();
        try {
            checkBusy();
            return this.mPlayer.seekDown();
        } catch (RemoteException e) {
            remoteError(e);
            return -1;
        }
    }

    public long seekUp() throws SemFmPlayerException {
        checkOnStatus();
        try {
            checkBusy();
            return this.mPlayer.seekUp();
        } catch (RemoteException e) {
            remoteError(e);
            return -1;
        }
    }

    public void setAlternateFrequencyEnabled(boolean z) throws SemFmPlayerException {
        checkOnStatus();
        if (z) {
            try {
                this.mPlayer.enableAF();
                return;
            } catch (RemoteException e) {
                remoteError(e);
                return;
            }
        }
        this.mPlayer.disableAF();
    }

    public void setAudioMode(int i) throws SemFmPlayerException {
        checkOnStatus();
        if (i == 9) {
            try {
                this.mPlayer.setStereo();
            } catch (RemoteException e) {
                remoteError(e);
            }
        } else if (i == 8) {
            this.mPlayer.setMono();
        }
    }

    public void setBand(int i) throws SemFmPlayerException {
        try {
            this.mPlayer.setBand(i);
        } catch (RemoteException e) {
            remoteError(e);
        }
    }

    public void setChannelSpacing(int i) throws SemFmPlayerException {
        try {
            this.mPlayer.setChannelSpacing(i);
        } catch (RemoteException e) {
            remoteError(e);
        }
    }

    public void setFMIntenna(boolean z) throws SemFmPlayerException {
        checkOnStatus();
        try {
            this.mPlayer.setFMIntenna(z);
        } catch (RemoteException e) {
            remoteError(e);
        }
    }

    public void setInternetStreamingEnabled(boolean z) throws SemFmPlayerException {
        try {
            this.mPlayer.setInternetStreamingMode(z);
        } catch (RemoteException e) {
            remoteError(e);
        }
    }

    public boolean setMuteEnabled(boolean z) throws SemFmPlayerException {
        checkOnStatus();
        try {
            this.mPlayer.mute(z);
            return true;
        } catch (RemoteException e) {
            remoteError(e);
            return false;
        }
    }

    public void setRadioDataSystemEnabled(boolean z) throws SemFmPlayerException {
        checkOnStatus();
        if (z) {
            try {
                this.mPlayer.enableRDS();
                return;
            } catch (RemoteException e) {
                remoteError(e);
                return;
            }
        }
        this.mPlayer.disableRDS();
    }

    public void setRadioDomainNameSystemEnabled(boolean z) throws SemFmPlayerException {
        checkOnStatus();
        if (z) {
            try {
                this.mPlayer.enableDNS();
                return;
            } catch (RemoteException e) {
                remoteError(e);
                return;
            }
        }
        this.mPlayer.disableDNS();
    }

    public void setRecordMode(boolean z) throws SemFmPlayerException {
        try {
            this.mPlayer.setRecordMode(z);
        } catch (RemoteException e) {
            remoteError(e);
        }
    }

    public void setSoftmuteEnabled(boolean z) throws SemFmPlayerException {
        try {
            this.mPlayer.setSoftmute(z);
        } catch (RemoteException e) {
            remoteError(e);
        }
    }

    public boolean setSpeakerEnabled(boolean z) throws SemFmPlayerException {
        log("setting speakerOn = :" + z);
        try {
            this.mPlayer.setSpeakerOn(z);
        } catch (RemoteException e) {
            remoteError(e);
        }
        this.mAudioManager.semSetRadioSpeakerOn(z);
        return this.mAudioManager.semIsRadioSpeakerOn();
    }

    public void setTunningParameter(String str, int i) throws SemFmPlayerException {
        checkOnStatus();
        try {
            this.mPlayer.setIntegerTunningParameter(str, i);
        } catch (RemoteException e) {
            remoteError(e);
        }
    }

    public void setTunningParameter(String str, long j) throws SemFmPlayerException {
        checkOnStatus();
        try {
            this.mPlayer.setLongTunningParameter(str, j);
        } catch (RemoteException e) {
            remoteError(e);
        }
    }

    public void setTunningParameter(String str, String str2) throws SemFmPlayerException {
        checkOnStatus();
        try {
            this.mPlayer.setStringTunningParameter(str, str2);
        } catch (RemoteException e) {
            remoteError(e);
        }
    }

    public void setVolume(long j) throws SemFmPlayerException {
        try {
            this.mPlayer.setVolume(j);
        } catch (RemoteException e) {
            remoteError(e);
        }
    }

    public void startScan() throws SemFmPlayerException {
        checkOnStatus();
        try {
            checkBusy();
            this.mPlayer.scan();
        } catch (RemoteException e) {
            remoteError(e);
        }
    }

    public boolean tune(long j) throws SemFmPlayerException {
        checkOnStatus();
        try {
            this.mPlayer.tune(j);
            return true;
        } catch (RemoteException e) {
            remoteError(e);
            return false;
        }
    }
}
