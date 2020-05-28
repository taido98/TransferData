package com.example.transferdata.adapter;

import android.app.Activity;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
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
import com.example.transferdata.media.DetailListAudio;

import java.util.List;

public class adapterAudio extends BaseAdapter {
    private DetailListAudio context;
    private Boolean folder;
    private List<itemAudio> listAudio;
    private int resource;

    public adapterAudio(DetailListAudio context2, int resource2, List<itemAudio> list, Boolean folder2) {
        this.context = context2;
        this.resource = resource2;
        this.listAudio = list;
        this.folder = folder2;
    }

    public int getCount() {
        if (listAudio.isEmpty()) {
            return 0;
        }
        if (this.folder) {
            return this.listAudio.size();
        }
        return this.listAudio.get(0).listPathAudio.size();
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
//        row.findViewById(R.id.play_video).setVisibility(View.VISIBLE);
        Display display = this.context.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        img.setMinimumHeight(width / 3);
        img.setMinimumWidth(width / 3);
        if (this.folder) {
            int total = this.listAudio.get(position).listPathAudio.size();
            String name = this.listAudio.get(position).folderName;
            total_image.setText(Integer.toString(total));
            folder_name.setText(name);
            row.setPadding(1, 0, 1, 30);
            checkBox.setChecked(this.listAudio.get(position).select);
            clickCheckBoxFolder(checkBox, position);
//            ((RequestBuilder) ((RequestBuilder) Glide.with((Activity) this.context).load(this.listAudio.get(position).listPathAudio.get(0).source).centerCrop()).thumbnail(0.1f).placeholder((int) R.color.cardview_light_background)).into(img);
//            Drawable audioIcon = get;
//            int auditest = R.drawable.audio_file;
//            img.setImageDrawable(R.drawable.audio_file);
        } else {
            folder_name.setText(this.listAudio.get(0).listPathAudio.get(position).name);
            clickCheckBoxVideo(checkBox, position);
            checkBox.setChecked(this.listAudio.get(0).listPathAudio.get(position).select);
            ((RequestBuilder) ((RequestBuilder) Glide.with((Activity) this.context).load(this.listAudio.get(0).listPathAudio.get(position).source).centerCrop()).thumbnail(0.1f).placeholder((int) R.color.cardview_light_background)).into(img);
        }
        return row;
    }

    /* access modifiers changed from: 0000 */
    private void clickCheckBoxFolder(final CheckBox checkBox, final int pos) {
        checkBox.setOnClickListener(v -> {
            if (checkBox.isChecked()) {
                adapterAudio.this.listAudio.get(pos).select = Boolean.TRUE;
                adapterAudio.this.checkVideo(Boolean.TRUE, pos);
                return;
            }
            adapterAudio.this.listAudio.get(pos).select = Boolean.FALSE;
            adapterAudio.this.checkVideo(Boolean.FALSE, pos);
        });
    }

    /* access modifiers changed from: 0000 */
    private void clickCheckBoxVideo(final CheckBox checkBox, final int pos) {
        checkBox.setOnClickListener(v -> {
            adapterAudio.this.listAudio.get(0).listPathAudio.get(pos).select = checkBox.isChecked();
            adapterAudio.this.listAudio.get(0).select = adapterAudio.this.checkFolder(0);
            adapterAudio.this.context.adapterFolder.notifyDataSetChanged();
        });
    }

    /* access modifiers changed from: 0000 */
    private void checkVideo(Boolean check, int pos) {
        for (infoItemAudio audio : this.listAudio.get(pos).listPathAudio) {
            audio.select = check;
        }
        this.context.adapterAudio.notifyDataSetChanged();
    }

    /* access modifiers changed from: 0000 */
    private boolean checkFolder(int pos) {
        for (infoItemAudio audio : this.listAudio.get(pos).listPathAudio) {
            if (audio.select) {
                return true;
            }
        }
        return false;
    }
}
