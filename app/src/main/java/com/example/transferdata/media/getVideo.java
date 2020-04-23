package com.example.transferdata.media;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore.Video.Media;

import com.example.transferdata.adapter.infoItemVideo;
import com.example.transferdata.adapter.itemVideo;
import com.example.transferdata.tranferdata.ClientActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class getVideo {
    public static List<itemVideo> listVideo = new ArrayList();
    private Activity context;
    private List<String> folder = new ArrayList();

    public getVideo(Activity context2) {
        this.context = context2;
    }

    public void getVideoPath() {
        getVideo getvideo = this;
        getvideo.folder.clear();
        listVideo.clear();
        Uri uri = Media.EXTERNAL_CONTENT_URI;
        String str = "_size";
        String str2 = "_id";
        String str3 = "bucket_display_name";
        String str4 = "_data";
        String[] projection = {str4, str3, str2, str, str4};
        Cursor cursor = getvideo.context.getApplicationContext().getContentResolver().query(uri, projection, null, null, "datetaken DESC");
        int column_index_data = Objects.requireNonNull(cursor).getColumnIndexOrThrow(str4);
        int column_index_folder_name = cursor.getColumnIndexOrThrow(str3);
        int columnIndexOrThrow = cursor.getColumnIndexOrThrow(str2);
        int thum = cursor.getColumnIndexOrThrow(str4);
        while (cursor.moveToNext()) {
            String folderName = cursor.getString(column_index_folder_name);
            if (!getvideo.folder.contains(folderName)) {
                getvideo.folder.add(folderName);
            }
        }
        for (String f : getvideo.folder) {
            Cursor cursor2 = getvideo.context.getApplicationContext().getContentResolver().query(uri, projection, null, null, "datetaken DESC");
            List<infoItemVideo> path = new ArrayList<>();
            while (Objects.requireNonNull(cursor2).moveToNext()) {
                if (f.equals(cursor2.getString(column_index_folder_name))) {
                    Uri uri2 = uri;
                    path.add(new infoItemVideo(cursor2.getString(column_index_data), cursor2.getString(thum), true, cursor2.getInt(cursor2.getColumnIndexOrThrow(str))));
                    uri = uri2;
                }
            }
            Uri uri3 = uri;
            listVideo.add(new itemVideo(f, true, path));
            getvideo = this;
            uri = uri3;
        }
    }

    public String getleng() {
        int count = 0;
        int size = 0;
        int sizeRound = 0;
        for (itemVideo item : listVideo) {
            for (infoItemVideo video : item.getListVideo()) {
                if (video.isSelect()) {
                    count++;
                    size += video.getSize();
                    sizeRound = (int) (((double) sizeRound) + (((double) video.getSize()) * 1.0E-6d));
                }
            }
        }
        ClientActivity.SIZE_ALL_ITEM[4] = sizeRound;
        return "Selected : " +
                count +
                " item - " +
                String.format("%.02f", new Object[]{Double.valueOf(((double) size) * 1.0E-6d)}) +
                " MB";
    }
}
