package com.samsung.android.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.ExpandableListAdapter;
import android.widget.SemExpandableListView;
import android.widget.SemExpandableListView.OnChildClickListener;
import android.widget.SemExpandableListView.OnGroupCollapseListener;
import android.widget.SemExpandableListView.OnGroupExpandListener;
import com.android.internal.C0717R;

public class SemExpandableListActivity extends Activity implements OnCreateContextMenuListener, OnChildClickListener, OnGroupCollapseListener, OnGroupExpandListener {
    ExpandableListAdapter mAdapter;
    boolean mFinishedStart = false;
    SemExpandableListView mList;

    private void ensureList() {
        if (this.mList == null) {
            setContentView(C0717R.layout.expandable_list_content);
        }
    }

    public ExpandableListAdapter getExpandableListAdapter() {
        return this.mAdapter;
    }

    public SemExpandableListView getExpandableListView() {
        ensureList();
        return this.mList;
    }

    public long getSelectedId() {
        return this.mList.getSelectedId();
    }

    public long getSelectedPosition() {
        return this.mList.getSelectedPosition();
    }

    public boolean onChildClick(SemExpandableListView semExpandableListView, View view, int i, int i2, long j) {
        return false;
    }

    public void onContentChanged() {
        super.onContentChanged();
        View findViewById = findViewById(C0717R.id.empty);
        this.mList = (SemExpandableListView) findViewById(C0717R.id.list);
        if (this.mList == null) {
            throw new RuntimeException("Your content must have a SemExpandableListView whose id attribute is 'R.id.list'");
        }
        if (findViewById != null) {
            this.mList.setEmptyView(findViewById);
        }
        this.mList.setOnChildClickListener(this);
        this.mList.setOnGroupExpandListener(this);
        this.mList.setOnGroupCollapseListener(this);
        if (this.mFinishedStart) {
            setListAdapter(this.mAdapter);
        }
        this.mFinishedStart = true;
    }

    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenuInfo contextMenuInfo) {
    }

    public void onGroupCollapse(int i) {
    }

    public void onGroupExpand(int i) {
    }

    protected void onRestoreInstanceState(Bundle bundle) {
        ensureList();
        super.onRestoreInstanceState(bundle);
    }

    public void setListAdapter(ExpandableListAdapter expandableListAdapter) {
        synchronized (this) {
            ensureList();
            this.mAdapter = expandableListAdapter;
            this.mList.setAdapter(expandableListAdapter);
        }
    }

    public boolean setSelectedChild(int i, int i2, boolean z) {
        return this.mList.setSelectedChild(i, i2, z);
    }

    public void setSelectedGroup(int i) {
        this.mList.setSelectedGroup(i);
    }
}
