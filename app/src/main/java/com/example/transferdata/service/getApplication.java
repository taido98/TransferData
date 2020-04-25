package com.example.transferdata.service;

import android.app.Activity;
import android.app.DirectAction;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build.VERSION;
import androidx.core.content.FileProvider;

import com.example.transferdata.adapter.DataItem;
import com.example.transferdata.media.DetailListApp;
import com.example.transferdata.tranferdata.ClientActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class getApplication {
    public static ArrayList<DataItem> listItem = new ArrayList<>();
    private Activity context;
    DataItem dataItem = new DataItem();

    public getApplication(Activity context2) {
        this.context = context2;
    }

    public String backupApps() {
        Intent mainIntent2;
        int len;
        int countApp = 0;
        long sizeApp = 0;
        listItem.clear();
        Intent mainIntent3 = new Intent("android.intent.action.MAIN", null);
        mainIntent3.addCategory("android.intent.category.LAUNCHER");
        List<ResolveInfo> pkgAppList = this.context.getPackageManager().queryIntentActivities(mainIntent3, 0);
        File file = new File(this.context.getExternalFilesDir(null), "apps");
        if (!file.exists()) {
            file.mkdir();
        }
        for (ResolveInfo info : pkgAppList) {
            if (!info.activityInfo.applicationInfo.publicSourceDir.contains("/system/")) {
                int countApp2 = countApp + 1;
                try {
                    File f1 = new File(info.activityInfo.applicationInfo.publicSourceDir);
                    String fileName = info.loadLabel(this.context.getPackageManager()).toString();
                    String sb = fileName +
                            ".apk";
                    File fileApp = new File(file, sb);
                    if (!fileApp.exists()) {
                        fileApp.createNewFile();
                        InputStream inputStream = new FileInputStream(f1);
                        OutputStream outputStream = new FileOutputStream(fileApp);
                        byte[] buf = new byte[1024];
                        while (true) {
                            int read = inputStream.read(buf);
                            len = read;
                            if (read <= 0) {
                                break;
                            }
                            File f12 = f1;
                            mainIntent2 = mainIntent3;
                            try {
                                outputStream.write(buf, 0, len);
                                f1 = f12;
                                mainIntent3 = mainIntent2;
                            } catch (IOException e) {
                                e.printStackTrace();
                                countApp = countApp2;
                            }
                        }
                        inputStream.close();
                        outputStream.close();
                    }
                    Drawable icon = info.loadIcon(this.context.getPackageManager());
                    DataItem item = new DataItem(true, -1, fileName, "", Boolean.FALSE);
                    item.setImgDrawable(icon);
                    item.setSize((int) fileApp.length());
                    item.setSource(fileApp.getPath());
                    listItem.add(item);
                    if (DetailListApp.adapterdetail != null) {
                        this.context.runOnUiThread(() -> DetailListApp.adapterdetail.notifyDataSetChanged());
                    }
                    sizeApp += fileApp.length();

                    countApp = countApp2;
                } catch (IOException ignored) {

                }
            }
        }
        ClientActivity.SIZE_ALL_ITEM[5] = sizeApp;
        return dataItem.sizeToString(sizeApp);
    }

    public void restoreApps() {
        String sb = this.context.getExternalFilesDir(null) +
                "/apps";
        File directory = new File(sb);
        if (directory.exists()) {
            for (File file : Objects.requireNonNull(directory.listFiles())) {
                Intent intent = new Intent("android.intent.action.VIEW");
                String str = "application/vnd.android.package-archive";
                if (VERSION.SDK_INT >= 24) {
                    Uri data = FileProvider.getUriForFile(this.context, "com.example.transferdata.provider", file);
//                    intent.addFlags(1);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setDataAndType(data, str);
                    this.context.startActivity(intent);
                } else {
                    intent.setDataAndType(Uri.fromFile(file), str);
//                    intent.addFlags(1);
                    this.context.startActivity(intent);
                }
            }
        }
    }
}
