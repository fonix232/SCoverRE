package com.samsung.android.infoextraction.regex;

import java.util.ArrayList;
import java.util.List;

public class SemEntityInfo {
    private ArrayList<String> dateInfo = new ArrayList();
    private ArrayList<String> dateMillisInfo = new ArrayList();
    private ArrayList<String> emailAddressInfo = new ArrayList();
    private ArrayList<String> phoneNumInfo = new ArrayList();
    private ArrayList<String> timeInfo = new ArrayList();
    private ArrayList<String> timeMillisInfo = new ArrayList();
    private ArrayList<String> urlInfo = new ArrayList();

    public class Type {
        public static final int DATE = 1;
        public static final int DATE_MILLISECOND = 2;
        public static final int EMAIL_ADDRESS = 6;
        public static final int PHONE_NUMBER = 5;
        public static final int TIME = 3;
        public static final int TIME_MILLISECOND = 4;
        public static final int URL = 7;

        private Type() {
        }
    }

    public void clear() {
        this.dateInfo.clear();
        this.dateMillisInfo.clear();
        this.timeInfo.clear();
        this.timeMillisInfo.clear();
        this.phoneNumInfo.clear();
        this.emailAddressInfo.clear();
        this.urlInfo.clear();
    }

    public boolean deleteInfo(int i, int i2) {
        switch (i2) {
            case 1:
                if (i >= this.dateInfo.size()) {
                    return false;
                }
                this.dateInfo.remove(i);
                return true;
            case 2:
                if (i >= this.dateMillisInfo.size()) {
                    return false;
                }
                this.dateMillisInfo.remove(i);
                return true;
            case 3:
                if (i >= this.timeInfo.size()) {
                    return false;
                }
                this.timeInfo.remove(i);
                return true;
            case 4:
                if (i >= this.timeMillisInfo.size()) {
                    return false;
                }
                this.timeMillisInfo.remove(i);
                return true;
            case 5:
                if (i >= this.phoneNumInfo.size()) {
                    return false;
                }
                this.phoneNumInfo.remove(i);
                return true;
            case 6:
                if (i >= this.emailAddressInfo.size()) {
                    return false;
                }
                this.emailAddressInfo.remove(i);
                return true;
            case 7:
                if (i >= this.urlInfo.size()) {
                    return false;
                }
                this.urlInfo.remove(i);
                return true;
            default:
                return false;
        }
    }

    public int getCount(int i) {
        switch (i) {
            case 1:
                return this.dateInfo.size();
            case 2:
                return this.dateMillisInfo.size();
            case 3:
                return this.timeInfo.size();
            case 4:
                return this.timeMillisInfo.size();
            case 5:
                return this.phoneNumInfo.size();
            case 6:
                return this.emailAddressInfo.size();
            case 7:
                return this.urlInfo.size();
            default:
                return 0;
        }
    }

    public List<String> getInfoList(int i) {
        switch (i) {
            case 1:
                return this.dateInfo;
            case 2:
                return this.dateMillisInfo;
            case 3:
                return this.timeInfo;
            case 4:
                return this.timeMillisInfo;
            case 5:
                return this.phoneNumInfo;
            case 6:
                return this.emailAddressInfo;
            case 7:
                return this.urlInfo;
            default:
                return new ArrayList();
        }
    }

    public void setInfo(String str, int i) {
        switch (i) {
            case 1:
                this.dateInfo.add(str);
                return;
            case 2:
                this.dateMillisInfo.add(str);
                return;
            case 3:
                this.timeInfo.add(str);
                return;
            case 4:
                this.timeMillisInfo.add(str);
                return;
            case 5:
                this.phoneNumInfo.add(str);
                return;
            case 6:
                this.emailAddressInfo.add(str);
                return;
            case 7:
                this.urlInfo.add(str);
                return;
            default:
                return;
        }
    }
}
