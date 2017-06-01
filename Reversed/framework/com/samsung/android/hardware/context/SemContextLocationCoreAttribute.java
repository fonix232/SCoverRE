package com.samsung.android.hardware.context;

import android.location.Location;
import android.os.BaseBundle;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.util.Log;

public class SemContextLocationCoreAttribute extends SemContextAttribute {
    public static final Creator<SemContextLocationCoreAttribute> CREATOR = new C01721();
    private static final String TAG = "SemContextLocationCoreAttribute";
    private int mAccuracy;
    private int mAction;
    private int mBatchingSize;
    private int mFenceId;
    private FusedBatchOption mFusedBatchOption;
    private double mLatitude;
    private Location mLocation;
    private double mLongitude;
    private int mMinDistance;
    private int mMinTime;
    private int mMode;
    private int mRadius;
    private int mRequestId;
    private int mStatus;
    private int mSuccessGpsCnt;
    private long mTimeStamp;
    private int mTotalGpsCnt;

    static class C01721 implements Creator<SemContextLocationCoreAttribute> {
        C01721() {
        }

        public SemContextLocationCoreAttribute createFromParcel(Parcel parcel) {
            return new SemContextLocationCoreAttribute(parcel);
        }

        public SemContextLocationCoreAttribute[] newArray(int i) {
            return new SemContextLocationCoreAttribute[i];
        }
    }

    private static class FusedBatchOption {
        final float distance_thrs;
        final int flags;
        final double max_power;
        final long period;
        final int user_info;

        FusedBatchOption(long j, int i, int i2, double d, float f) {
            this.period = j;
            this.user_info = i;
            this.flags = i2;
            this.max_power = d;
            this.distance_thrs = f;
        }

        boolean isValid() {
            if (this.period < 0) {
                Log.d(SemContextLocationCoreAttribute.TAG, "FusedBatchOption.period is wrong.");
                return false;
            } else if (this.user_info < 0) {
                Log.d(SemContextLocationCoreAttribute.TAG, "FusedBatchOption.user_info is wrong.");
                return false;
            } else if (this.flags < 0) {
                Log.d(SemContextLocationCoreAttribute.TAG, "FusedBatchOption.flags is wrong.");
                return false;
            } else if (this.max_power < 0.0d) {
                Log.d(SemContextLocationCoreAttribute.TAG, "FusedBatchOption.max_power is wrong.");
                return false;
            } else if (this.distance_thrs >= 0.0f) {
                return true;
            } else {
                Log.d(SemContextLocationCoreAttribute.TAG, "FusedBatchOption.distance_thrs is wrong.");
                return false;
            }
        }
    }

    SemContextLocationCoreAttribute() {
        this.mMode = -1;
        this.mAction = -1;
        this.mFenceId = 0;
        this.mRadius = 0;
        this.mStatus = 0;
        this.mTotalGpsCnt = 0;
        this.mSuccessGpsCnt = 0;
        this.mMinDistance = 0;
        this.mMinTime = 0;
        this.mAccuracy = 0;
        this.mTimeStamp = 0;
        this.mLongitude = 0.0d;
        this.mLatitude = 0.0d;
        this.mRequestId = 0;
        this.mBatchingSize = 0;
        this.mFusedBatchOption = null;
        this.mLocation = null;
        setAttribute();
    }

    public SemContextLocationCoreAttribute(int i, int i2) {
        this.mMode = -1;
        this.mAction = -1;
        this.mFenceId = 0;
        this.mRadius = 0;
        this.mStatus = 0;
        this.mTotalGpsCnt = 0;
        this.mSuccessGpsCnt = 0;
        this.mMinDistance = 0;
        this.mMinTime = 0;
        this.mAccuracy = 0;
        this.mTimeStamp = 0;
        this.mLongitude = 0.0d;
        this.mLatitude = 0.0d;
        this.mRequestId = 0;
        this.mBatchingSize = 0;
        this.mFusedBatchOption = null;
        this.mLocation = null;
        this.mMode = i;
        this.mAction = i2;
        setAttribute();
    }

    public SemContextLocationCoreAttribute(int i, int i2, double d, double d2, int i3, long j) {
        this.mMode = -1;
        this.mAction = -1;
        this.mFenceId = 0;
        this.mRadius = 0;
        this.mStatus = 0;
        this.mTotalGpsCnt = 0;
        this.mSuccessGpsCnt = 0;
        this.mMinDistance = 0;
        this.mMinTime = 0;
        this.mAccuracy = 0;
        this.mTimeStamp = 0;
        this.mLongitude = 0.0d;
        this.mLatitude = 0.0d;
        this.mRequestId = 0;
        this.mBatchingSize = 0;
        this.mFusedBatchOption = null;
        this.mLocation = null;
        this.mMode = i;
        this.mAction = i2;
        this.mLatitude = d;
        this.mLongitude = d2;
        this.mAccuracy = i3;
        this.mTimeStamp = j;
        setAttribute();
    }

    public SemContextLocationCoreAttribute(int i, int i2, int i3) {
        this.mMode = -1;
        this.mAction = -1;
        this.mFenceId = 0;
        this.mRadius = 0;
        this.mStatus = 0;
        this.mTotalGpsCnt = 0;
        this.mSuccessGpsCnt = 0;
        this.mMinDistance = 0;
        this.mMinTime = 0;
        this.mAccuracy = 0;
        this.mTimeStamp = 0;
        this.mLongitude = 0.0d;
        this.mLatitude = 0.0d;
        this.mRequestId = 0;
        this.mBatchingSize = 0;
        this.mFusedBatchOption = null;
        this.mLocation = null;
        this.mMode = i;
        this.mAction = i2;
        if (this.mMode == 0) {
            this.mFenceId = i3;
        } else if (this.mMode == 3) {
            if (this.mAction == 18) {
                this.mRequestId = i3;
            } else if (this.mAction == 19) {
                this.mBatchingSize = i3;
            }
        }
        setAttribute();
    }

    public SemContextLocationCoreAttribute(int i, int i2, int i3, double d, double d2, int i4, int i5, int i6) {
        this.mMode = -1;
        this.mAction = -1;
        this.mFenceId = 0;
        this.mRadius = 0;
        this.mStatus = 0;
        this.mTotalGpsCnt = 0;
        this.mSuccessGpsCnt = 0;
        this.mMinDistance = 0;
        this.mMinTime = 0;
        this.mAccuracy = 0;
        this.mTimeStamp = 0;
        this.mLongitude = 0.0d;
        this.mLatitude = 0.0d;
        this.mRequestId = 0;
        this.mBatchingSize = 0;
        this.mFusedBatchOption = null;
        this.mLocation = null;
        this.mMode = i;
        this.mAction = i2;
        this.mFenceId = i3;
        this.mRadius = i4;
        this.mTotalGpsCnt = i5;
        this.mSuccessGpsCnt = i6;
        this.mLatitude = d;
        this.mLongitude = d2;
        setAttribute();
    }

    public SemContextLocationCoreAttribute(int i, int i2, int i3, int i4) {
        this.mMode = -1;
        this.mAction = -1;
        this.mFenceId = 0;
        this.mRadius = 0;
        this.mStatus = 0;
        this.mTotalGpsCnt = 0;
        this.mSuccessGpsCnt = 0;
        this.mMinDistance = 0;
        this.mMinTime = 0;
        this.mAccuracy = 0;
        this.mTimeStamp = 0;
        this.mLongitude = 0.0d;
        this.mLatitude = 0.0d;
        this.mRequestId = 0;
        this.mBatchingSize = 0;
        this.mFusedBatchOption = null;
        this.mLocation = null;
        this.mMode = i;
        this.mAction = i2;
        this.mMinDistance = i3;
        this.mMinTime = i4;
        setAttribute();
    }

    public SemContextLocationCoreAttribute(int i, int i2, int i3, int i4, int i5) {
        this.mMode = -1;
        this.mAction = -1;
        this.mFenceId = 0;
        this.mRadius = 0;
        this.mStatus = 0;
        this.mTotalGpsCnt = 0;
        this.mSuccessGpsCnt = 0;
        this.mMinDistance = 0;
        this.mMinTime = 0;
        this.mAccuracy = 0;
        this.mTimeStamp = 0;
        this.mLongitude = 0.0d;
        this.mLatitude = 0.0d;
        this.mRequestId = 0;
        this.mBatchingSize = 0;
        this.mFusedBatchOption = null;
        this.mLocation = null;
        this.mMode = i;
        this.mAction = i2;
        this.mFenceId = i3;
        this.mRadius = i4;
        this.mStatus = i5;
        setAttribute();
    }

    public SemContextLocationCoreAttribute(int i, int i2, int i3, long j, int i4, int i5, double d, float f) {
        this.mMode = -1;
        this.mAction = -1;
        this.mFenceId = 0;
        this.mRadius = 0;
        this.mStatus = 0;
        this.mTotalGpsCnt = 0;
        this.mSuccessGpsCnt = 0;
        this.mMinDistance = 0;
        this.mMinTime = 0;
        this.mAccuracy = 0;
        this.mTimeStamp = 0;
        this.mLongitude = 0.0d;
        this.mLatitude = 0.0d;
        this.mRequestId = 0;
        this.mBatchingSize = 0;
        this.mFusedBatchOption = null;
        this.mLocation = null;
        this.mMode = i;
        this.mAction = i2;
        this.mRequestId = i3;
        this.mFusedBatchOption = new FusedBatchOption(j, i4, i5, d, f);
        setAttribute();
    }

    public SemContextLocationCoreAttribute(int i, int i2, Location location) {
        this.mMode = -1;
        this.mAction = -1;
        this.mFenceId = 0;
        this.mRadius = 0;
        this.mStatus = 0;
        this.mTotalGpsCnt = 0;
        this.mSuccessGpsCnt = 0;
        this.mMinDistance = 0;
        this.mMinTime = 0;
        this.mAccuracy = 0;
        this.mTimeStamp = 0;
        this.mLongitude = 0.0d;
        this.mLatitude = 0.0d;
        this.mRequestId = 0;
        this.mBatchingSize = 0;
        this.mFusedBatchOption = null;
        this.mLocation = null;
        this.mMode = i;
        this.mAction = i2;
        this.mLocation = location;
        setAttribute();
    }

    private SemContextLocationCoreAttribute(Parcel parcel) {
        super(parcel);
        this.mMode = -1;
        this.mAction = -1;
        this.mFenceId = 0;
        this.mRadius = 0;
        this.mStatus = 0;
        this.mTotalGpsCnt = 0;
        this.mSuccessGpsCnt = 0;
        this.mMinDistance = 0;
        this.mMinTime = 0;
        this.mAccuracy = 0;
        this.mTimeStamp = 0;
        this.mLongitude = 0.0d;
        this.mLatitude = 0.0d;
        this.mRequestId = 0;
        this.mBatchingSize = 0;
        this.mFusedBatchOption = null;
        this.mLocation = null;
    }

    private void setAttribute() {
        BaseBundle bundle = new Bundle();
        double[] dArr;
        int[] iArr;
        long[] jArr;
        switch (this.mMode) {
            case 0:
                if (this.mAction != 1) {
                    if (this.mAction != 2) {
                        if (this.mAction != 7) {
                            if (this.mAction != 9) {
                                Log.d(TAG, "This Type is default attribute type");
                                break;
                            }
                            bundle.putIntArray("IntType", new int[]{this.mMinDistance, this.mMinTime});
                            break;
                        }
                        bundle.putIntArray("IntType", new int[]{this.mFenceId, this.mRadius, this.mStatus});
                        break;
                    }
                    bundle.putIntArray("IntType", new int[]{this.mFenceId});
                    break;
                }
                dArr = new double[2];
                iArr = new int[]{this.mLatitude, this.mLongitude, this.mFenceId, this.mRadius};
                iArr[2] = this.mTotalGpsCnt;
                iArr[3] = this.mSuccessGpsCnt;
                bundle.putIntArray("IntType", iArr);
                bundle.putDoubleArray("DoubleType", dArr);
                break;
            case 1:
                if (this.mAction != 8) {
                    Log.d(TAG, "This Type is default attribute type");
                    break;
                }
                dArr = new double[2];
                iArr = new int[1];
                jArr = new long[]{this.mLatitude};
                dArr[1] = this.mLongitude;
                iArr[0] = this.mAccuracy;
                jArr[0] = this.mTimeStamp;
                bundle.putDoubleArray("DoubleType", dArr);
                bundle.putIntArray("IntType", iArr);
                bundle.putLongArray("LongType", jArr);
                break;
            case 3:
                float[] fArr;
                if (this.mAction != 16 && this.mAction != 17) {
                    if (this.mAction != 18) {
                        if (this.mAction != 19) {
                            if (this.mAction != 21) {
                                Log.d(TAG, "This Type is default attribute type");
                                break;
                            }
                            jArr = new long[1];
                            dArr = new double[3];
                            fArr = new float[3];
                            String provider = this.mLocation.getProvider();
                            jArr[0] = this.mLocation.getTime();
                            dArr[0] = this.mLocation.getLatitude();
                            dArr[1] = this.mLocation.getLongitude();
                            dArr[2] = this.mLocation.getAltitude();
                            fArr[0] = this.mLocation.getSpeed();
                            fArr[1] = this.mLocation.getBearing();
                            fArr[2] = this.mLocation.getAccuracy();
                            bundle.putString("StringType", provider);
                            bundle.putLongArray("IntType", jArr);
                            bundle.putDoubleArray("DoubleType", dArr);
                            bundle.putFloatArray("FloatType", fArr);
                            break;
                        }
                        bundle.putIntArray("IntType", new int[]{this.mBatchingSize});
                        break;
                    }
                    bundle.putIntArray("IntType", new int[]{this.mRequestId});
                    break;
                }
                iArr = new int[3];
                jArr = new long[1];
                dArr = new double[1];
                fArr = new float[]{this.mRequestId};
                iArr[1] = this.mFusedBatchOption.user_info;
                iArr[2] = this.mFusedBatchOption.flags;
                jArr[0] = this.mFusedBatchOption.period;
                dArr[0] = this.mFusedBatchOption.max_power;
                fArr[0] = this.mFusedBatchOption.distance_thrs;
                bundle.putIntArray("IntType", iArr);
                bundle.putLongArray("LongType", jArr);
                bundle.putDoubleArray("DoubleType", dArr);
                bundle.putFloatArray("FloatType", fArr);
                break;
                break;
        }
        bundle.putInt("Mode", this.mMode);
        bundle.putInt("Action", this.mAction);
        Log.d(TAG, "setAttribute() mode : " + bundle.getInt("Mode") + " action : " + bundle.getInt("Action"));
        super.setAttribute(47, bundle);
    }

    public boolean checkAttribute() {
        if (this.mMode < -1 || this.mMode > 3) {
            Log.d(TAG, "Mode value is wrong!!");
            return false;
        }
        if (this.mMode == 0) {
            if (this.mAction < -1 || this.mAction > 10) {
                Log.d(TAG, "Action value is wrong!!");
                return false;
            }
        } else if (this.mMode == 1) {
            if (this.mAction < -1 || this.mAction > 14) {
                Log.d(TAG, "Action value is wrong!!");
                return false;
            }
        } else if (this.mMode == 3) {
            if (this.mAction < 16 || this.mAction > 22) {
                Log.d(TAG, "Action value is wrong!!");
                return false;
            } else if ((this.mAction == 16 || this.mAction == 17) && !this.mFusedBatchOption.isValid()) {
                Log.d(TAG, "FusedBatchOption is wrong");
                return false;
            }
        }
        if (this.mFenceId < 0) {
            Log.d(TAG, "FenceID is wrong!!");
            return false;
        } else if (this.mRadius < 0) {
            Log.d(TAG, "Radius is wrong!!");
            return false;
        } else if (this.mStatus < 0) {
            Log.d(TAG, "Status is wrong!1");
            return false;
        } else if (this.mTotalGpsCnt < 0) {
            Log.d(TAG, "TotalGpsCount is wrong!!");
            return false;
        } else if (this.mSuccessGpsCnt < 0) {
            Log.d(TAG, "Success gps count is wrong");
            return false;
        } else if (this.mMinDistance < 0) {
            Log.d(TAG, "Minimum distance is wrong");
            return false;
        } else if (this.mMinTime < 0) {
            Log.d(TAG, "Minimum time is wrong");
            return false;
        } else if (this.mAccuracy < 0) {
            Log.d(TAG, "Accuracy is wrong");
            return false;
        } else if (this.mTimeStamp < 0) {
            Log.d(TAG, "Timestamp is wrong");
            return false;
        } else if (this.mLongitude < -180.0d || this.mLongitude > 180.0d) {
            Log.d(TAG, "Longitude is wrong");
            return false;
        } else if (this.mLatitude < -90.0d || this.mLatitude > 90.0d) {
            Log.d(TAG, "Latitude is wrong");
            return false;
        } else if (this.mRequestId < 0) {
            Log.d(TAG, "RequestId is wrong");
            return false;
        } else if (this.mBatchingSize >= 0) {
            return true;
        } else {
            Log.d(TAG, "BatchingSize is wrong");
            return false;
        }
    }
}
