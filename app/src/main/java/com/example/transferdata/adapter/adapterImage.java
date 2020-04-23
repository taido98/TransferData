package com.example.transferdata.adapter;

import android.app.Activity;
import android.graphics.Point;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.example.transferdata.R;
import com.example.transferdata.media.DetailListImage;

import java.util.List;

public class adapterImage extends BaseAdapter {
    DetailListImage context;
    Boolean folder;
    List<itemImage> listImage;
    int resource;

    public adapterImage(DetailListImage context2, int resource2, List<itemImage> list, Boolean folder2) {
        this.context = context2;
        this.resource = resource2;
        this.listImage = list;
        this.folder = folder2;
    }

    public int getCount() {
        if (this.listImage.isEmpty()) {
            return 0;
        }
        if (this.folder.booleanValue()) {
            return this.listImage.size();
        }
        return ((itemImage) this.listImage.get(0)).listPathImage.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        int i = position;
        View row = this.context.getLayoutInflater().inflate(this.resource, null);
        ImageView img = (ImageView) row.findViewById(R.id.img_item_grid_view);
        TextView total_image = (TextView) row.findViewById(R.id.total_image);
        TextView folder_name = (TextView) row.findViewById(R.id.folder_name);
        CheckBox checkBox = (CheckBox) row.findViewById(R.id.cb_select_image);
        Display display = this.context.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        img.setMinimumHeight(width / 3);
        img.setMinimumWidth(width / 3);
        if (this.folder.booleanValue()) {
            int total = ((itemImage) this.listImage.get(i)).listPathImage.size();
            String name = ((itemImage) this.listImage.get(i)).folderName;
            total_image.setText(Integer.toString(total));
            folder_name.setText(name);
            row.setPadding(1, 0, 1, 30);
            checkBox.setChecked(((itemImage) this.listImage.get(i)).select.booleanValue());
            clickCheckBoxFolder(checkBox, i);
            ((RequestBuilder) ((RequestBuilder) Glide.with((Activity) this.context).load(((infoImage) ((itemImage) this.listImage.get(i)).listPathImage.get(0)).source).centerCrop()).thumbnail(0.1f).placeholder((int) R.color.cardview_light_background)).into(img);
        } else {
            clickCheckBoxImage(checkBox, i);
            checkBox.setChecked(((infoImage) ((itemImage) this.listImage.get(0)).listPathImage.get(i)).select);
            ((RequestBuilder) ((RequestBuilder) Glide.with((Activity) this.context).load(((infoImage) ((itemImage) this.listImage.get(0)).listPathImage.get(i)).source).centerCrop()).thumbnail(0.1f).placeholder((int) R.color.cardview_light_background)).into(img);
        }
        return row;
    }

    /* access modifiers changed from: 0000 */
    public void clickCheckBoxFolder(final CheckBox checkBox, final int pos) {
        checkBox.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    ((itemImage) adapterImage.this.listImage.get(pos)).select = Boolean.valueOf(true);
                    adapterImage.this.checkImage(Boolean.valueOf(true), pos);
                    return;
                }
                ((itemImage) adapterImage.this.listImage.get(pos)).select = Boolean.valueOf(false);
                adapterImage.this.checkImage(Boolean.valueOf(false), pos);
            }
        });
    }

    /* access modifiers changed from: 0000 */
    public void clickCheckBoxImage(final CheckBox checkBox, final int pos) {
        checkBox.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    ((infoImage) ((itemImage) adapterImage.this.listImage.get(0)).listPathImage.get(pos)).select = true;
                } else {
                    ((infoImage) ((itemImage) adapterImage.this.listImage.get(0)).listPathImage.get(pos)).select = false;
                }
                ((itemImage) adapterImage.this.listImage.get(0)).select = Boolean.valueOf(adapterImage.this.checkFolder(0));
                adapterImage.this.context.adapter.notifyDataSetChanged();
            }
        });
    }

    /* access modifiers changed from: 0000 */
    public void checkImage(Boolean check, int pos) {
        for (infoImage infoImage : ((itemImage) this.listImage.get(pos)).listPathImage) {
            infoImage.select = check.booleanValue();
        }
        this.context.adapterImage.notifyDataSetChanged();
    }

    /* access modifiers changed from: 0000 */
    public boolean checkFolder(int pos) {
        for (infoImage infoImage : ((itemImage) this.listImage.get(pos)).listPathImage) {
            if (infoImage.select) {
                return true;
            }
        }
        return false;
    }
}
