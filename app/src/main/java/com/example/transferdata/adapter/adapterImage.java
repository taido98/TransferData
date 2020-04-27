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
    private DetailListImage context;
    private Boolean folder;
    private List<itemImage> listImage;
    private int resource;

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
        if (this.folder) {
            return this.listImage.size();
        }
        return this.listImage.get(0).listPathImage.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row = this.context.getLayoutInflater().inflate(this.resource, null);
        ImageView img = row.findViewById(R.id.img_item_grid_view);
        TextView total_image = row.findViewById(R.id.total_image);
        TextView folder_name = row.findViewById(R.id.folder_name);
        CheckBox checkBox = row.findViewById(R.id.cb_select_image);
        Display display = this.context.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        img.setMinimumHeight(width / 3);
        img.setMinimumWidth(width / 3);
        if (this.folder) {
            int total = this.listImage.get(position).listPathImage.size();
            String name = this.listImage.get(position).folderName;
            total_image.setText(Integer.toString(total));
            folder_name.setText(name);
            row.setPadding(1, 0, 1, 30);
            checkBox.setChecked(listImage.get(position).select);
            clickCheckBoxFolder(checkBox, position);
            ((RequestBuilder) ((RequestBuilder) Glide.with((Activity) this.context).load(this.listImage.get(position).listPathImage.get(0).source).centerCrop()).thumbnail(0.1f).placeholder((int) R.color.cardview_light_background)).into(img);
        } else {
            clickCheckBoxImage(checkBox, position);
            checkBox.setChecked(this.listImage.get(0).listPathImage.get(position).select);
            ((RequestBuilder) ((RequestBuilder) Glide.with((Activity) this.context).load(this.listImage.get(0).listPathImage.get(position).source).centerCrop()).thumbnail(0.1f).placeholder((int) R.color.cardview_light_background)).into(img);
        }
        return row;
    }

    /* access modifiers changed from: 0000 */
    private void clickCheckBoxFolder(final CheckBox checkBox, final int pos) {
        checkBox.setOnClickListener(v -> {
            if (checkBox.isChecked()) {
                adapterImage.this.listImage.get(pos).select = Boolean.TRUE;
                adapterImage.this.checkImage(Boolean.TRUE, pos);
                return;
            }
            adapterImage.this.listImage.get(pos).select = Boolean.FALSE;
            adapterImage.this.checkImage(Boolean.FALSE, pos);
        });
    }

    /* access modifiers changed from: 0000 */
    private void clickCheckBoxImage(final CheckBox checkBox, final int pos) {
        checkBox.setOnClickListener(v -> {
            adapterImage.this.listImage.get(0).listPathImage.get(pos).select = checkBox.isChecked();
            adapterImage.this.listImage.get(0).select = adapterImage.this.checkFolder(0);
            adapterImage.this.context.adapterFolder.notifyDataSetChanged();
        });
    }

    /* access modifiers changed from: 0000 */
    private void checkImage(Boolean check, int pos) {
        for (infoImage infoImage : this.listImage.get(pos).listPathImage) {
            infoImage.select = check;
        }
        this.context.adapterImage.notifyDataSetChanged();
    }

    /* access modifiers changed from: 0000 */
    private boolean checkFolder(int pos) {
        for (infoImage infoImage : this.listImage.get(pos).listPathImage) {
            if (infoImage.select) {
                return true;
            }
        }
        return false;
    }
}
