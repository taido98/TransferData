package com.example.transferdata.media;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.example.transferdata.adapter.DataItem;
import com.example.transferdata.adapter.infoItemAudio;
import com.example.transferdata.adapter.itemAudio;
import com.example.transferdata.tranferdata.ClientActivity;

import java.util.ArrayList;
import java.util.List;

public class getAudio {
    public static List<itemAudio> listAudio = new ArrayList();

    public void getAudiosPath(Activity activity) {
        String disPlayName = null;
        ArrayList<String> arrayList = new ArrayList<>();
        List<itemAudio> listItemImage = new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
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
            List<infoItemAudio> listPathAudio = new ArrayList<>();
            while (cursor2.moveToNext()) {
                String folder2 = folder;
                if (folder2.equals(cursor2.getString(cursor2.getColumnIndexOrThrow(str4)))) {
                    disPlayName = cursor2.getString( cursor2.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                    Log.d("Display audio >>>", disPlayName);
                    String pathAudio;
                    pathAudio = cursor2.getString(cursor2.getColumnIndexOrThrow(str3));
                    listPathAudio.add(new infoItemAudio(true, cursor2.getInt(cursor2.getColumnIndexOrThrow(str2)), cursor2.getString(cursor2.getColumnIndexOrThrow(str)), pathAudio));
                    folder = folder2;
                } else {
                    folder = folder2;
                }
            }
            listItemImage.add(new itemAudio(folder, Boolean.TRUE, listPathAudio,disPlayName));
        }
        listAudio = listItemImage;
    }

    public String getSize() {
        DataItem dataItem = new DataItem();
        long size = 0;
        for (itemAudio item : listAudio) {
            for (infoItemAudio info : item.getListPathAudio()) {
                if (info.isSelect()) {
                    size += info.getSize();
                }
            }
        }
        ClientActivity.SIZE_ALL_ITEM[7] = size;
        return dataItem.sizeToString(size);
    }
}
