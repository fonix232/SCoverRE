package com.samsung.android.aod;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import com.samsung.android.aod.IAlwaysOnDisplayService.Stub;
import java.util.List;

public class AlwaysOnDisplayService extends Service {
    public static String TAG = AlwaysOnDisplayService.class.getSimpleName();
    private final Handler mHandler = new Handler();

    private final class AlwaysOnDisplayServiceWrapper extends Stub {

        class C09841 implements Runnable {
            C09841() {
            }

            public void run() {
                AlwaysOnDisplayService.this.startAOD();
            }
        }

        class C09852 implements Runnable {
            C09852() {
            }

            public void run() {
                AlwaysOnDisplayService.this.stopAOD();
            }
        }

        class C09863 implements Runnable {
            C09863() {
            }

            public void run() {
                AlwaysOnDisplayService.this.requestHide();
            }
        }

        private AlwaysOnDisplayServiceWrapper() {
        }

        public void requestHide() {
            AlwaysOnDisplayService.this.mHandler.post(new C09863());
        }

        public void startAOD() {
            AlwaysOnDisplayService.this.mHandler.post(new C09841());
        }

        public void stopAOD() {
            AlwaysOnDisplayService.this.mHandler.post(new C09852());
        }

        public void updateCalendarData(final List<String> list, final List<String> list2) {
            AlwaysOnDisplayService.this.mHandler.post(new Runnable() {
                public void run() {
                    AlwaysOnDisplayService.this.updateCalendarData(list, list2);
                }
            });
        }

        public void updateNotificationKeys(final int i, final List<String> list) {
            AlwaysOnDisplayService.this.mHandler.post(new Runnable() {
                public void run() {
                    AlwaysOnDisplayService.this.updateNotificationKeys(i, list);
                }
            });
        }
    }

    public final IBinder onBind(Intent intent) {
        return new AlwaysOnDisplayServiceWrapper();
    }

    public void requestHide() {
        Log.m29d(TAG, "requestHide Called!");
    }

    public void startAOD() {
        Log.m29d(TAG, "startAOD Called!");
    }

    public void stopAOD() {
        Log.m29d(TAG, "stopAOD Called!");
    }

    public void updateCalendarData(List<String> list, List<String> list2) {
        Log.m29d(TAG, "updateCalendarData!");
    }

    public void updateNotificationKeys(int i, List<String> list) {
        Log.m29d(TAG, "updateNotificationKeys!");
    }
}
