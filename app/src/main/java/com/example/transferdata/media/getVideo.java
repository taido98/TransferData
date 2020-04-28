package com.example.transferdata.media;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.example.transferdata.adapter.DataItem;
import com.example.transferdata.adapter.infoItemVideo;
import com.example.transferdata.adapter.itemVideo;
import com.example.transferdata.tranferdata.ClientActivity;

import java.util.ArrayList;
import java.util.List;

public class getVideo {
    public static List<itemVideo> listVideo = new ArrayList();

    public void getVideosPath(Activity activity) {
        ArrayList<String> arrayList = new ArrayList<>();
        List<itemVideo> listItemVideo = new ArrayList<>();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String str = "_display_name";
        String str2 = "_size";
        String str3 = "_data";
        String str4 = "bucket_display_name";
        String[] projection = {str3, str4, str2, str};
        Cursor cursor = activity.getContentResolver().query(uri, projection, null, null, null);
        while (cursor.moveToNext()) {
            String folderName = cursor.getString(cursor.getColumnIndexOrThrow(str4));
            if (!arrayList.contains(folderName)) {
                arrayList.add(folderName);
            }
        }
        for (String folder : arrayList) {
            Cursor cursor2 = activity.getContentResolver().query(uri, projection, null, null, null);
            List<infoItemVideo> listPathVideo = new ArrayList<>();
            while (cursor2.moveToNext()) {
                String folder2 = folder;
                if (folder2.equals(cursor2.getString(cursor2.getColumnIndexOrThrow(str4)))) {
                    String pathVideo;
                    pathVideo = cursor2.getString(cursor2.getColumnIndexOrThrow(str3));
//                    Log.d("Path Image>>",pathVideo);
                    if(!pathVideo.contains("/storage/emulated/0/")){
                        pathVideo ="/storage/emulated/0" + pathVideo.substring(pathVideo.indexOf("/",pathVideo.indexOf("/", pathVideo.indexOf("/")+1)+1));
                    }
                    Log.d("Path Image>>",pathVideo);
                    listPathVideo.add(new infoItemVideo(true, cursor2.getInt(cursor2.getColumnIndexOrThrow(str2)), cursor2.getString(cursor2.getColumnIndexOrThrow(str)), pathVideo));
                    folder = folder2;
                } else {
                    folder = folder2;
                }
            }
            listItemVideo.add(new itemVideo(folder, Boolean.TRUE, listPathVideo));
        }
        listVideo = listItemVideo;
    }

    public String getSize() {
        DataItem dataItem = new DataItem();
        long size = 0;
        for (itemVideo item : listVideo) {
            for (infoItemVideo video : item.getListVideo()) {
                if (video.isSelect()) {
                    size += video.getSize();
                }
            }
        }
        ClientActivity.SIZE_ALL_ITEM[4] = size;
        return dataItem.sizeToString(size);
    }
}
