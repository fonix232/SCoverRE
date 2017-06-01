package com.samsung.android.widget;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListAdapter;
import android.widget.SemHorizontalListView.FixedViewInfo;
import android.widget.WrapperListAdapter;
import java.util.ArrayList;

public class SemHorizontalHeaderViewListAdapter implements WrapperListAdapter, Filterable {
    static final ArrayList<FixedViewInfo> EMPTY_INFO_LIST = new ArrayList();
    private final ListAdapter mAdapter;
    boolean mAreAllFixedViewsSelectable;
    ArrayList<FixedViewInfo> mFooterViewInfos;
    ArrayList<FixedViewInfo> mHeaderViewInfos;
    private final boolean mIsFilterable;

    public SemHorizontalHeaderViewListAdapter(ArrayList<FixedViewInfo> arrayList, ArrayList<FixedViewInfo> arrayList2, ListAdapter listAdapter) {
        this.mAdapter = listAdapter;
        this.mIsFilterable = listAdapter instanceof Filterable;
        if (arrayList == null) {
            this.mHeaderViewInfos = EMPTY_INFO_LIST;
        } else {
            this.mHeaderViewInfos = arrayList;
        }
        if (arrayList2 == null) {
            this.mFooterViewInfos = EMPTY_INFO_LIST;
        } else {
            this.mFooterViewInfos = arrayList2;
        }
        this.mAreAllFixedViewsSelectable = areAllListInfosSelectable(this.mHeaderViewInfos) ? areAllListInfosSelectable(this.mFooterViewInfos) : false;
    }

    private boolean areAllListInfosSelectable(ArrayList<FixedViewInfo> arrayList) {
        if (arrayList != null) {
            for (FixedViewInfo fixedViewInfo : arrayList) {
                if (!fixedViewInfo.isSelectable) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean areAllItemsEnabled() {
        if (this.mAdapter == null) {
            return true;
        }
        return this.mAreAllFixedViewsSelectable ? this.mAdapter.areAllItemsEnabled() : false;
    }

    public int getCount() {
        return this.mAdapter != null ? (getFootersCount() + getHeadersCount()) + this.mAdapter.getCount() : getFootersCount() + getHeadersCount();
    }

    public Filter getFilter() {
        return this.mIsFilterable ? ((Filterable) this.mAdapter).getFilter() : null;
    }

    public int getFootersCount() {
        return this.mFooterViewInfos.size();
    }

    public int getHeadersCount() {
        return this.mHeaderViewInfos.size();
    }

    public Object getItem(int i) {
        int headersCount = getHeadersCount();
        if (i < headersCount) {
            return ((FixedViewInfo) this.mHeaderViewInfos.get(i)).data;
        }
        int i2 = i - headersCount;
        int i3 = 0;
        if (this.mAdapter != null) {
            i3 = this.mAdapter.getCount();
            if (i2 < i3) {
                return this.mAdapter.getItem(i2);
            }
        }
        return i2 - i3 >= this.mFooterViewInfos.size() ? null : ((FixedViewInfo) this.mFooterViewInfos.get(i2 - i3)).data;
    }

    public long getItemId(int i) {
        int headersCount = getHeadersCount();
        if (this.mAdapter != null && i >= headersCount) {
            int i2 = i - headersCount;
            if (i2 < this.mAdapter.getCount()) {
                return this.mAdapter.getItemId(i2);
            }
        }
        return -1;
    }

    public int getItemViewType(int i) {
        int headersCount = getHeadersCount();
        if (this.mAdapter != null && i >= headersCount) {
            int i2 = i - headersCount;
            if (i2 < this.mAdapter.getCount()) {
                return this.mAdapter.getItemViewType(i2);
            }
        }
        return -2;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        int headersCount = getHeadersCount();
        if (i < headersCount) {
            return ((FixedViewInfo) this.mHeaderViewInfos.get(i)).view;
        }
        int i2 = i - headersCount;
        int i3 = 0;
        if (this.mAdapter != null) {
            i3 = this.mAdapter.getCount();
            if (i2 < i3) {
                return this.mAdapter.getView(i2, view, viewGroup);
            }
        }
        return i2 - i3 >= this.mFooterViewInfos.size() ? null : ((FixedViewInfo) this.mFooterViewInfos.get(i2 - i3)).view;
    }

    public int getViewTypeCount() {
        return this.mAdapter != null ? this.mAdapter.getViewTypeCount() : 1;
    }

    public ListAdapter getWrappedAdapter() {
        return this.mAdapter;
    }

    public boolean hasStableIds() {
        return this.mAdapter != null ? this.mAdapter.hasStableIds() : false;
    }

    public boolean isEmpty() {
        return this.mAdapter != null ? this.mAdapter.isEmpty() : true;
    }

    public boolean isEnabled(int i) {
        int headersCount = getHeadersCount();
        if (i < headersCount) {
            return ((FixedViewInfo) this.mHeaderViewInfos.get(i)).isSelectable;
        }
        int i2 = i - headersCount;
        int i3 = 0;
        if (this.mAdapter != null) {
            i3 = this.mAdapter.getCount();
            if (i2 < i3) {
                return this.mAdapter.isEnabled(i2);
            }
        }
        return i2 - i3 >= this.mFooterViewInfos.size() ? false : ((FixedViewInfo) this.mFooterViewInfos.get(i2 - i3)).isSelectable;
    }

    public void registerDataSetObserver(DataSetObserver dataSetObserver) {
        if (this.mAdapter != null) {
            this.mAdapter.registerDataSetObserver(dataSetObserver);
        }
    }

    public boolean removeFooter(View view) {
        boolean z = false;
        for (int i = 0; i < this.mFooterViewInfos.size(); i++) {
            if (((FixedViewInfo) this.mFooterViewInfos.get(i)).view == view) {
                this.mFooterViewInfos.remove(i);
                if (areAllListInfosSelectable(this.mHeaderViewInfos)) {
                    z = areAllListInfosSelectable(this.mFooterViewInfos);
                }
                this.mAreAllFixedViewsSelectable = z;
                return true;
            }
        }
        return false;
    }

    public boolean removeHeader(View view) {
        boolean z = false;
        for (int i = 0; i < this.mHeaderViewInfos.size(); i++) {
            if (((FixedViewInfo) this.mHeaderViewInfos.get(i)).view == view) {
                this.mHeaderViewInfos.remove(i);
                if (areAllListInfosSelectable(this.mHeaderViewInfos)) {
                    z = areAllListInfosSelectable(this.mFooterViewInfos);
                }
                this.mAreAllFixedViewsSelectable = z;
                return true;
            }
        }
        return false;
    }

    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {
        if (this.mAdapter != null) {
            this.mAdapter.unregisterDataSetObserver(dataSetObserver);
        }
    }
}
