package com.example.transferdata.media;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore.Files;

import com.example.transferdata.R;
import com.example.transferdata.adapter.DataItem;
import com.example.transferdata.tranferdata.ClientActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class getFile {
    public static List<DataItem> listFile = new ArrayList();
    private Activity context;

    public getFile(Activity context2) {
        this.context = context2;
    }

    public void getAllFile() {
        int pathColumnIndex;
        ContentResolver contentResolver;
        listFile.clear();
        ContentResolver contentResolver2 = this.context.getContentResolver();
        Uri uri = Files.getContentUri("external");
        List<String> extensions = new ArrayList<>();
        String filePdf = "pdf";
        extensions.add(filePdf);
        String fileCsv = "csv";
        extensions.add(fileCsv);
        String fileDoc = "doc";
        extensions.add(fileDoc);
        String fileDocx = "docx";
        extensions.add(fileDocx);
        String fileXls = "xls";
        extensions.add(fileXls);
        String fileXlsx = "xlsx";
        extensions.add(fileXlsx);
        String fileZip = "zip";
        extensions.add(fileZip);
        String filePpt = "ppt";
        extensions.add(filePpt);
        String filePptm = "pptm";
        extensions.add(filePptm);
        String filePptx = "pptx";
        extensions.add(filePptx);
        Cursor cursor = contentResolver2.query(uri, null, "media_type=0", null, null);
        if (cursor != null) {
            int pathColumnIndex2 = cursor.getColumnIndex("_data");
            while (cursor.moveToNext()) {
                String filePath = cursor.getString(pathColumnIndex2);
                String extension = getExtensionByPath(filePath);
                if (extensions.contains(extension)) {
                    int sizeColumnIndex = cursor.getColumnIndex("_size");
                    int titleColumnIndex = cursor.getColumnIndex("title");
                    contentResolver = contentResolver2;
                    int nameColumnIndex = cursor.getColumnIndex("_display_name");
                    pathColumnIndex = pathColumnIndex2;
                    int fileSize = cursor.getInt(sizeColumnIndex);
                    String string = cursor.getString(nameColumnIndex);
                    String fileTitle = cursor.getString(titleColumnIndex);
                    DataItem item2 = new DataItem();
                    item2.setName(fileTitle);
                    item2.setChecked(true);
                    item2.setSize(fileSize);
                    item2.setSource(filePath);
                    if (extension.contains(fileDoc) || extension.contains(fileDocx)) {
                        item2.setImg_resource(R.drawable.ic_word);
                        listFile.add(item2);
                    }
                    if (extension.contains(filePdf)) {
                        item2.setImg_resource(R.drawable.ic_pdf);
                        listFile.add(item2);
                    }
                    if (extension.contains(fileXls) || extension.contains(fileXlsx) || extension.contains(fileCsv) ) {
                        item2.setImg_resource(R.drawable.ic_excel);
                        listFile.add(item2);
                    }
                    if (extension.contains(filePpt) || extension.contains(filePptm) || extension.contains(filePptx)) {
                        item2.setImg_resource(R.drawable.ic_powerpoint);
                        listFile.add(item2);
                    }
                    if (extension.contains(fileZip)) {
                        item2.setImg_resource(R.drawable.ic_zip);
                        listFile.add(item2);
                    }
                } else {
                    contentResolver = contentResolver2;
                    pathColumnIndex = pathColumnIndex2;
                }
                pathColumnIndex2 = pathColumnIndex;
                contentResolver2 = contentResolver;
            }
        }
        Objects.requireNonNull(cursor).close();
    }

    private static String getExtensionByPath(String path) {
        String result = "%20";
        int i = path.lastIndexOf(46);
        if (i > 0) {
            return path.substring(i + 1);
        }
        return result;
    }

    public String getSize() {
        DataItem dataItem = new DataItem();
        int count = 0;
        int size = 0;
//        int sizeRound = 0;
        for (DataItem it : listFile) {
            if (it.isChecked()) {
                size += it.getSize();
//                sizeRound = (int) (((double) sizeRound) + (((double) it.getSize()) * 1.0E-6d));
                count++;
            }
        }
        ClientActivity.SIZE_ALL_ITEM[6] = size;
        return dataItem.sizeToString(size);
    }
}
