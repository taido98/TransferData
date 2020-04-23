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
        String str = null;
        int pathColumnIndex;
        ContentResolver contentResolver;
        String str2 = null;
        listFile.clear();
        ContentResolver contentResolver2 = this.context.getContentResolver();
        Uri uri = Files.getContentUri("external");
        List<String> extensions = new ArrayList<>();
        String str6 = "pdf";
        extensions.add(str6);
        String str7 = "csv";
        extensions.add(str7);
        String str8 = "doc";
        extensions.add(str8);
        String str9 = "docx";
        extensions.add(str9);
        String str10 = "xls";
        extensions.add(str10);
        String str11 = "xlsx";
        extensions.add(str11);
        String str12 = "zip";
        extensions.add(str12);
        String str13 = "ppt";
        extensions.add(str13);
        String str14 = "pptm";
        extensions.add(str14);
        String str15 = "pptx";
        extensions.add(str15);
        String str16 = str15;
        String str17 = str14;
        String str18 = str13;
        String str19 = str12;
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
                    DataItem item = new DataItem();
                    DataItem item2 = item;
                    item2.setName(fileTitle);
                    item2.setChecked(true);
                    item2.setSize(fileSize);
                    item2.setSource(filePath);
                    if (extension.contains(str8)) {
                    } else if (extension.contains(str9)) {
                    } else {
                        if (extension.contains(str7) || extension.contains(str10)) {
                            str2 = str16;
                        } else if (extension.contains(str11)) {
                            str2 = str16;
                        } else {
                            String str21 = str18;
                            if (!extension.contains(str21)) {
                                str18 = str21;
                                String str22 = str17;
                                if (!extension.contains(str22)) {
                                    str17 = str22;
                                    str2 = str16;
                                    if (extension.contains(Objects.requireNonNull(str2))) {
                                        item2.setImg_resource(R.drawable.ic_powerpoint);
                                        listFile.add(item2);
                                    } else {
                                        if (extension.contains(Objects.requireNonNull(str19))) {
                                            item2.setImg_resource(R.drawable.ic_zip);
                                        } else {
                                            if (extension.contains(str6)) {
                                                item2.setImg_resource(R.drawable.ic_pdf);
                                            }
                                        }
                                        listFile.add(item2);
                                    }
                                } else {
                                    str17 = str22;
                                }
                            } else {
                                str18 = str21;
                            }
                            str2 = str16;
                            str = str19;
                            item2.setImg_resource(R.drawable.ic_powerpoint);
                            listFile.add(item2);
                        }
                        item2.setImg_resource(R.drawable.ic_excel);
                        listFile.add(item2);
                    }
                    item2.setImg_resource(R.drawable.ic_word);
                    listFile.add(item2);
                } else {
                    contentResolver = contentResolver2;
                    pathColumnIndex = pathColumnIndex2;
                    str2 = str16;
                    str = str19;
                }
                pathColumnIndex2 = pathColumnIndex;
                str19 = str;
                str16 = str2;
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
        int count = 0;
        int size = 0;
        int sizeRound = 0;
        for (DataItem it : listFile) {
            if (it.isChecked()) {
                size += it.getSize();
                sizeRound = (int) (((double) sizeRound) + (((double) it.getSize()) * 1.0E-6d));
                count++;
            }
        }
        ClientActivity.SIZE_ALL_ITEM[6] = sizeRound;
        return "Selected : " +
                count +
                " item - " +
                String.format("%.02f", new Object[]{Double.valueOf(((double) size) * 1.0E-6d)}) +
                " MB";
    }
}
