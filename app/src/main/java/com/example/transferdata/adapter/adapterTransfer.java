package com.example.transferdata.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.transferdata.R;

import java.util.LinkedList;
import java.util.List;
import pl.droidsonroids.gif.GifImageView;

public class adapterTransfer extends BaseAdapter {
    Activity context;
    GifImageView gifLoad;
    List<DataItem> listItem;
    int resource;
    LinkedList<Long> size;

    public adapterTransfer(Activity contex, int resource2, List<DataItem> listItem2, LinkedList<Long> size2) {
        this.listItem = listItem2;
        this.resource = resource2;
        this.context = contex;
        this.size = size2;
    }

    public int getCount() {
        if (!this.listItem.isEmpty()) {
            return this.listItem.size();
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
        View row = this.context.getLayoutInflater().inflate(this.resource, null);
        TextView txt_size = (TextView) row.findViewById(R.id.tranfer_size_title);
        ((TextView) row.findViewById(R.id.txt_item_tranfer)).setText(((DataItem) this.listItem.get(position)).name);
        if (((Long) this.size.get(position)).intValue() > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(this.size.get(position));
            sb.append(" MB");
            txt_size.setText(sb.toString());
        } else {
            txt_size.setText("0.1 MB");
        }
        this.gifLoad = (GifImageView) row.findViewById(R.id.wait_tranfer);
        if (!((DataItem) this.listItem.get(position)).statusLoad) {
            this.gifLoad.setImageResource(R.drawable.ic_done);
        }
        return row;
    }
}
