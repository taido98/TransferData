package com.example.transferdata.media;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.transferdata.R;
import com.example.transferdata.adapter.DataItem;
import com.example.transferdata.adapter.infoItemVideo;
import com.example.transferdata.adapter.itemVideo;
import com.example.transferdata.tranferdata.ClientActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class getAudio {
    public static List<itemVideo> listAudio = new ArrayList();
    private Activity context;
    private List<String> folder = new ArrayList();

    public getAudio(Activity context2) {
        this.context = context2;
    }

    public void getAudioPath() {
        getAudio getAudio = this;
        getAudio.folder.clear();
        listAudio.clear();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String str = "_size";
        String str2 = "_id";
        String str3 = "bucket_display_name";
        String str4 = "_data";
        String[] projection = {str4, str3, str2, str, str4};
        Cursor cursor = getAudio.context.getApplicationContext().getContentResolver().query(uri, projection, null, null, "datetaken DESC");
        int column_index_data = Objects.requireNonNull(cursor).getColumnIndexOrThrow(str4);
        int column_index_folder_name = cursor.getColumnIndexOrThrow(str3);
        int columnIndexOrThrow = cursor.getColumnIndexOrThrow(str2);
        int thum = cursor.getColumnIndexOrThrow(str4);
        while (cursor.moveToNext()) {
            String folderName = cursor.getString(column_index_folder_name);
            if (!getAudio.folder.contains(folderName)) {
                getAudio.folder.add(folderName);
            }
        }
        for (String f : getAudio.folder) {
            Cursor cursor2 = getAudio.context.getApplicationContext().getContentResolver().query(uri, projection, null, null, "datetaken DESC");
            List<infoItemVideo> path = new ArrayList<>();
            while (Objects.requireNonNull(cursor2).moveToNext()) {
                if (f.equals(cursor2.getString(column_index_folder_name))) {
                    Uri uri2 = uri;
                    path.add(new infoItemVideo(cursor2.getString(column_index_data), cursor2.getString(thum), true, cursor2.getInt(cursor2.getColumnIndexOrThrow(str))));
                    uri = uri2;
                }
            }
            Uri uri3 = uri;
            listAudio.add(new itemVideo(f, true, path));
            getAudio = this;
            uri = uri3;
        }
    }

    public String getleng() {
        int count = 0;
        int size = 0;
        int sizeRound = 0;
        for (itemVideo item : listAudio) {
            for (infoItemVideo video : item.getListVideo()) {
                if (video.isSelect()) {
                    count++;
                    size += video.getSize();
                    sizeRound = (int) (((double) sizeRound) + (((double) video.getSize()) * 1.0E-6d));
                }
            }
        }
        ClientActivity.SIZE_ALL_ITEM[7] = sizeRound;
        return "Selected : " +
                count +
                " item - " +
                String.format("%.02f", new Object[]{Double.valueOf(((double) size) * 1.0E-6d)}) +
                " MB";
    }
}
