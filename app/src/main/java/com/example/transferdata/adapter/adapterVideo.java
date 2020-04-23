package com.example.transferdata.adapter;

import android.app.Activity;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
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
import com.example.transferdata.media.DetailListVideo;

import java.util.List;

public class adapterVideo extends BaseAdapter {
    DetailListVideo context;
    Boolean folder;
    List<itemVideo> listVideo;
    int resource;

    public adapterVideo(DetailListVideo context2, int resource2, List<itemVideo> list, Boolean folder2) {
        this.context = context2;
        this.resource = resource2;
        this.listVideo = list;
        this.folder = folder2;
    }

    public int getCount() {
        if (this.listVideo.isEmpty()) {
            return 0;
        }
        if (this.folder.booleanValue()) {
            return this.listVideo.size();
        }
        return ((itemVideo) this.listVideo.get(0)).listVideo.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        int i = position;
        LayoutInflater inflater = this.context.getLayoutInflater();
        View row = inflater.inflate(this.resource, null);
        ImageView img = (ImageView) row.findViewById(R.id.img_item_grid_view);
        TextView total_image = (TextView) row.findViewById(R.id.total_image);
        TextView folder_name = (TextView) row.findViewById(R.id.folder_name);
        CheckBox checkBox = (CheckBox) row.findViewById(R.id.cb_select_image);
        ((ImageView) row.findViewById(R.id.play_video)).setVisibility(View.VISIBLE);
        Display display = this.context.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        img.setMinimumHeight(width / 3);
        img.setMinimumWidth(width / 3);
        if (this.folder.booleanValue()) {
            int total = ((itemVideo) this.listVideo.get(i)).listVideo.size();
            String name = ((itemVideo) this.listVideo.get(i)).folderName;
            total_image.setText(Integer.toString(total));
            folder_name.setText(name);
            LayoutInflater layoutInflater = inflater;
            row.setPadding(1, 0, 1, 30);
            checkBox.setChecked(((itemVideo) this.listVideo.get(i)).select.booleanValue());
            clickCheckBoxFolder(checkBox, i);
            ((RequestBuilder) ((RequestBuilder) Glide.with((Activity) this.context).load(((infoItemVideo) ((itemVideo) this.listVideo.get(i)).listVideo.get(0)).thumbnails).centerCrop()).thumbnail(0.1f).placeholder((int) R.color.cardview_light_background)).into(img);
        } else {
            clickCheckBoxVideo(checkBox, i);
            checkBox.setChecked(((infoItemVideo) ((itemVideo) this.listVideo.get(0)).listVideo.get(i)).select);
            ((RequestBuilder) ((RequestBuilder) Glide.with((Activity) this.context).load(((infoItemVideo) ((itemVideo) this.listVideo.get(0)).listVideo.get(i)).thumbnails).centerCrop()).thumbnail(0.1f).placeholder((int) R.color.cardview_light_background)).into(img);
        }
        return row;
    }

    /* access modifiers changed from: 0000 */
    public void clickCheckBoxFolder(final CheckBox checkBox, final int pos) {
        checkBox.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    ((itemVideo) adapterVideo.this.listVideo.get(pos)).select = Boolean.valueOf(true);
                    adapterVideo.this.checkVideo(Boolean.valueOf(true), pos);
                    return;
                }
                ((itemVideo) adapterVideo.this.listVideo.get(pos)).select = Boolean.valueOf(false);
                adapterVideo.this.checkVideo(Boolean.valueOf(false), pos);
            }
        });
    }

    /* access modifiers changed from: 0000 */
    public void clickCheckBoxVideo(final CheckBox checkBox, final int pos) {
        checkBox.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    ((infoItemVideo) ((itemVideo) adapterVideo.this.listVideo.get(0)).listVideo.get(pos)).select = true;
                } else {
                    ((infoItemVideo) ((itemVideo) adapterVideo.this.listVideo.get(0)).listVideo.get(pos)).select = false;
                }
                ((itemVideo) adapterVideo.this.listVideo.get(0)).select = Boolean.valueOf(adapterVideo.this.checkFolder(0));
                adapterVideo.this.context.adapter.notifyDataSetChanged();
            }
        });
    }

    /* access modifiers changed from: 0000 */
    public void checkVideo(Boolean check, int pos) {
        for (infoItemVideo video : ((itemVideo) this.listVideo.get(pos)).listVideo) {
            video.select = check.booleanValue();
        }
        this.context.adapterVideo.notifyDataSetChanged();
    }

    /* access modifiers changed from: 0000 */
    public boolean checkFolder(int pos) {
        for (infoItemVideo video : ((itemVideo) this.listVideo.get(pos)).listVideo) {
            if (video.select) {
                return true;
            }
        }
        return false;
    }
}
