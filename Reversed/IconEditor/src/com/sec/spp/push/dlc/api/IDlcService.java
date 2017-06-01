package com.sec.spp.push.dlc.api;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;

public interface IDlcService extends IInterface {

    /* renamed from: com.sec.spp.push.dlc.api.IDlcService.a */
    public static abstract class C0438a extends Binder implements IDlcService {
        static final int f216a = 1;
        static final int f217b = 2;
        static final int f218c = 3;
        static final int f219d = 4;
        private static final String f220e = "com.sec.spp.push.dlc.api.IDlcService";

        /* renamed from: com.sec.spp.push.dlc.api.IDlcService.a.a */
        private static class C0437a implements IDlcService {
            private IBinder f215a;

            C0437a(IBinder iBinder) {
                this.f215a = iBinder;
            }

            public String m195a() {
                return C0438a.f220e;
            }

            public IBinder asBinder() {
                return this.f215a;
            }

            public int requestSend(String str, String str2, long j, String str3, String str4, String str5, String str6, String str7) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(C0438a.f220e);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeLong(j);
                    obtain.writeString(str3);
                    obtain.writeString(str4);
                    obtain.writeString(str5);
                    obtain.writeString(str6);
                    obtain.writeString(str7);
                    this.f215a.transact(C0438a.f216a, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int requestSendAggregation(String str, String str2, long j, String str3, String str4, String str5, String str6, String str7, int i, long j2, long j3, long j4, long j5, long j6) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(C0438a.f220e);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeLong(j);
                    obtain.writeString(str3);
                    obtain.writeString(str4);
                    obtain.writeString(str5);
                    obtain.writeString(str6);
                    obtain.writeString(str7);
                    obtain.writeInt(i);
                    obtain.writeLong(j2);
                    obtain.writeLong(j3);
                    obtain.writeLong(j4);
                    obtain.writeLong(j5);
                    obtain.writeLong(j6);
                    this.f215a.transact(C0438a.f218c, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int requestSendSummary(String str, String str2, long j, String str3, String str4, String str5, String str6, String str7, String str8, long j2, long j3, long j4, long j5, long j6) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(C0438a.f220e);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeLong(j);
                    obtain.writeString(str3);
                    obtain.writeString(str4);
                    obtain.writeString(str5);
                    obtain.writeString(str6);
                    obtain.writeString(str7);
                    obtain.writeString(str8);
                    obtain.writeLong(j2);
                    obtain.writeLong(j3);
                    obtain.writeLong(j4);
                    obtain.writeLong(j5);
                    obtain.writeLong(j6);
                    this.f215a.transact(C0438a.f219d, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int requestSendUrgent(String str, String str2, long j, String str3, String str4, String str5, String str6, String str7) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(C0438a.f220e);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeLong(j);
                    obtain.writeString(str3);
                    obtain.writeString(str4);
                    obtain.writeString(str5);
                    obtain.writeString(str6);
                    obtain.writeString(str7);
                    this.f215a.transact(C0438a.f217b, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public C0438a() {
            attachInterface(this, f220e);
        }

        public static IDlcService m196a(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(f220e);
            return (queryLocalInterface == null || !(queryLocalInterface instanceof IDlcService)) ? new C0437a(iBinder) : (IDlcService) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) {
            int requestSend;
            switch (i) {
                case f216a /*1*/:
                    parcel.enforceInterface(f220e);
                    requestSend = requestSend(parcel.readString(), parcel.readString(), parcel.readLong(), parcel.readString(), parcel.readString(), parcel.readString(), parcel.readString(), parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(requestSend);
                    return true;
                case f217b /*2*/:
                    parcel.enforceInterface(f220e);
                    requestSend = requestSendUrgent(parcel.readString(), parcel.readString(), parcel.readLong(), parcel.readString(), parcel.readString(), parcel.readString(), parcel.readString(), parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(requestSend);
                    return true;
                case f218c /*3*/:
                    parcel.enforceInterface(f220e);
                    requestSend = requestSendAggregation(parcel.readString(), parcel.readString(), parcel.readLong(), parcel.readString(), parcel.readString(), parcel.readString(), parcel.readString(), parcel.readString(), parcel.readInt(), parcel.readLong(), parcel.readLong(), parcel.readLong(), parcel.readLong(), parcel.readLong());
                    parcel2.writeNoException();
                    parcel2.writeInt(requestSend);
                    return true;
                case f219d /*4*/:
                    parcel.enforceInterface(f220e);
                    requestSend = requestSendSummary(parcel.readString(), parcel.readString(), parcel.readLong(), parcel.readString(), parcel.readString(), parcel.readString(), parcel.readString(), parcel.readString(), parcel.readString(), parcel.readLong(), parcel.readLong(), parcel.readLong(), parcel.readLong(), parcel.readLong());
                    parcel2.writeNoException();
                    parcel2.writeInt(requestSend);
                    return true;
                case 1598968902:
                    parcel2.writeString(f220e);
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    int requestSend(String str, String str2, long j, String str3, String str4, String str5, String str6, String str7);

    int requestSendAggregation(String str, String str2, long j, String str3, String str4, String str5, String str6, String str7, int i, long j2, long j3, long j4, long j5, long j6);

    int requestSendSummary(String str, String str2, long j, String str3, String str4, String str5, String str6, String str7, String str8, long j2, long j3, long j4, long j5, long j6);

    int requestSendUrgent(String str, String str2, long j, String str3, String str4, String str5, String str6, String str7);
}
