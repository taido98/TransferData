package com.example.transferdata.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.transferdata.R;
import com.example.transferdata.Interface.ClickItemListener;

import java.util.List;

public class adapterDetail extends BaseAdapter {
    private Activity context;
    private ClickItemListener detail;
    private List<DataItem> list;
    private int resource;

    public adapterDetail(Activity context2, int resource2, List<DataItem> list2, ClickItemListener detail2) {
        this.list = list2;
        this.context = context2;
        this.resource = resource2;
        this.detail = detail2;
    }

    public int getCount() {
        if (!this.list.isEmpty()) {
            return this.list.size();
        }
        return 0;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row = context.getLayoutInflater().inflate(this.resource, null);
        CheckBox cb_detail = row.findViewById(R.id.select_all);
        ImageView img_detail = row.findViewById(R.id.detail_icon);
        ((TextView) row.findViewById(R.id.detail_name)).setText((list.get(position)).name);
        cb_detail.setChecked(( list.get(position)).isChecked());
        if (this.context.getClass().getSimpleName().contains("message")) {
            img_detail.setVisibility(View.GONE);
        } else if (context.getClass().getSimpleName().contains("app")) {
            img_detail.setImageDrawable(( list.get(position)).imgDrawable);
            img_detail.setVisibility(View.VISIBLE);
        } else {
            img_detail.setImageResource(( list.get(position)).img_resource);
            img_detail.setVisibility(View.VISIBLE);
        }
        clickCheckBox(cb_detail, position);
        return row;
    }

    /* access modifiers changed from: 0000 */
    private void clickCheckBox(final CheckBox checkBox, final int pos) {
        checkBox.setOnClickListener(v -> {
            if (checkBox.isChecked()) {
                (adapterDetail.this.list.get(pos)).setChecked(true);
            } else {
                (adapterDetail.this.list.get(pos)).setChecked(false);
            }
            if (adapterDetail.this.setCheckAll()) {
                adapterDetail.this.detail.statusCheck(true);
            } else {
                adapterDetail.this.detail.statusCheck(false);
            }
        });
    }

    /* access modifiers changed from: 0000 */
    private boolean setCheckAll() {
        for (int i = 0; i < this.list.size(); i++) {
            if (!(this.list.get(i)).isChecked()) {
                return false;
            }
        }
        return true;
    }
}
