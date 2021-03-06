package com.example.transferdata.media;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore.Images.Media;
import android.util.Log;

import com.example.transferdata.adapter.DataItem;
import com.example.transferdata.adapter.infoImage;
import com.example.transferdata.adapter.itemImage;
import com.example.transferdata.tranferdata.ClientActivity;

import java.util.ArrayList;
import java.util.List;
public class getDataImage {
    public static List<itemImage> listImage = new ArrayList();

    public void getImagesPath(Activity activity) {
        ArrayList<String> arrayList = new ArrayList<>();
        List<itemImage> listItemImage = new ArrayList<>();
        Uri uri = Media.EXTERNAL_CONTENT_URI;
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
            List<infoImage> listPathImage = new ArrayList<>();
            while (cursor2.moveToNext()) {
                String folder2 = folder;
                if (folder2.equals(cursor2.getString(cursor2.getColumnIndexOrThrow(str4)))) {
                    String pathImage;
                    pathImage = cursor2.getString(cursor2.getColumnIndexOrThrow(str3));
//                    Log.d("Path Image>>",pathImage);
                    if(!pathImage.contains("/storage/emulated/0/")){
                        pathImage ="/storage/emulated/0" + pathImage.substring(pathImage.indexOf("/",pathImage.indexOf("/", pathImage.indexOf("/")+1)+1));
                    }
                    Log.d("Path Image>>",pathImage);
                    listPathImage.add(new infoImage(true, cursor2.getInt(cursor2.getColumnIndexOrThrow(str2)), cursor2.getString(cursor2.getColumnIndexOrThrow(str)), pathImage));
                    folder = folder2;
                } else {
                    folder = folder2;
                }
            }
            listItemImage.add(new itemImage(folder, Boolean.TRUE, listPathImage));
        }
        listImage = listItemImage;
    }

    public String getSize() {
        DataItem dataItem = new DataItem();
        long size = 0;
        for (itemImage item : listImage) {
            for (infoImage info : item.getListPathImage()) {
                if (info.isSelect()) {
                    size += info.getSize();
                }
            }
        }
        ClientActivity.SIZE_ALL_ITEM[3] = size;
        return dataItem.sizeToString(size);
    }

}
