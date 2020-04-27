package com.example.transferdata.adapter;

import android.app.Activity;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.example.transferdata.R;
import com.example.transferdata.media.DetailListVideo;

import java.util.List;

public class adapterVideo extends BaseAdapter {
    private DetailListVideo context;
    private Boolean folder;
    private List<itemVideo> listVideo;
    private int resource;

    public adapterVideo(DetailListVideo context2, int resource2, List<itemVideo> list, Boolean folder2) {
        this.context = context2;
        this.resource = resource2;
        this.listVideo = list;
        this.folder = folder2;
    }

    public int getCount() {
        if (listVideo.isEmpty()) {
            return 0;
        }
        if (this.folder) {
            return this.listVideo.size();
        }
        return this.listVideo.get(0).listVideo.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View row = inflater.inflate(this.resource, null);
        ImageView img = row.findViewById(R.id.img_item_grid_view);
        TextView total_image = row.findViewById(R.id.total_image);
        TextView folder_name = row.findViewById(R.id.folder_name);
        CheckBox checkBox = row.findViewById(R.id.cb_select_image);
        row.findViewById(R.id.play_video).setVisibility(View.VISIBLE);
        Display display = this.context.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        img.setMinimumHeight(width / 3);
        img.setMinimumWidth(width / 3);
        if (this.folder) {
            int total = this.listVideo.get(position).listVideo.size();
            String name = this.listVideo.get(position).folderName;
            total_image.setText(Integer.toString(total));
            folder_name.setText(name);
            row.setPadding(1, 0, 1, 30);
            checkBox.setChecked(this.listVideo.get(position).select);
            clickCheckBoxFolder(checkBox, position);
            ((RequestBuilder) ((RequestBuilder) Glide.with((Activity) this.context).load(this.listVideo.get(position).listVideo.get(0).source).centerCrop()).thumbnail(0.1f).placeholder((int) R.color.cardview_light_background)).into(img);
        } else {
            clickCheckBoxVideo(checkBox, position);
            checkBox.setChecked(this.listVideo.get(0).listVideo.get(position).select);
            ((RequestBuilder) ((RequestBuilder) Glide.with((Activity) this.context).load(this.listVideo.get(0).listVideo.get(position).source).centerCrop()).thumbnail(0.1f).placeholder((int) R.color.cardview_light_background)).into(img);
        }
        return row;
    }

    /* access modifiers changed from: 0000 */
    private void clickCheckBoxFolder(final CheckBox checkBox, final int pos) {
        checkBox.setOnClickListener(v -> {
            if (checkBox.isChecked()) {
                adapterVideo.this.listVideo.get(pos).select = Boolean.TRUE;
                adapterVideo.this.checkVideo(Boolean.TRUE, pos);
                return;
            }
            adapterVideo.this.listVideo.get(pos).select = Boolean.FALSE;
            adapterVideo.this.checkVideo(Boolean.FALSE, pos);
        });
    }

    /* access modifiers changed from: 0000 */
    private void clickCheckBoxVideo(final CheckBox checkBox, final int pos) {
        checkBox.setOnClickListener(v -> {
            adapterVideo.this.listVideo.get(0).listVideo.get(pos).select = checkBox.isChecked();
            adapterVideo.this.listVideo.get(0).select = adapterVideo.this.checkFolder(0);
            adapterVideo.this.context.adapterFolder.notifyDataSetChanged();
        });
    }

    /* access modifiers changed from: 0000 */
    private void checkVideo(Boolean check, int pos) {
        for (infoItemVideo video : this.listVideo.get(pos).listVideo) {
            video.select = check;
        }
        this.context.adapterVideo.notifyDataSetChanged();
    }

    /* access modifiers changed from: 0000 */
    private boolean checkFolder(int pos) {
        for (infoItemVideo video : this.listVideo.get(pos).listVideo) {
            if (video.select) {
                return true;
            }
        }
        return false;
    }
}
