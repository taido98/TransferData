package com.example.transferdata.selectdata;

import com.example.transferdata.R;

import java.io.File;
import java.util.ArrayList;

public class Data {
    private DataType mDataType;
    private ArrayList<String> mDataUrls;

    public Data(DataType mDataType, ArrayList<String> mUrlsData) {
        this.mDataType = mDataType;
        this.mDataUrls = mUrlsData;
    }

    public int getImageType() {
        switch (mDataType) {
            default:
                return R.drawable.d_icon_app;
        }
    }

    public String getTypeData() {
        switch (mDataType) {
            case IMAGES:
                return "Images";
            case VIDEOS:
                return "Videos";
            case MESSAGES:
                return "Messages";
            default:
                return "Contacts";
        }
    }

    public void setTypeData(DataType mTypeData) {
        this.mDataType = mTypeData;
    }

    public ArrayList<String> getUrlData() {
        return mDataUrls;
    }

    public void setUrlData(ArrayList<String> mUrlData) {
        this.mDataUrls = mUrlData;
    }

    public long getSize() {
        long mSize = 0;

        for (String path : mDataUrls) {
            mSize += (new File(path)).length();
        }

        return mSize;
    }

    public String sizeToString(long mSize) {
        int unit = 0;
        double size = mSize;
        while (size > 1000) {
            size /= 1024;
            unit++;
        }

        if (unit == 1)
            return round(size, 0) + "KB";
        else if (unit == 2)
            return round(size, 1) + "MB";
        else if (unit == 3)
            return round(size, 1) + "GB";
        else if (unit == 4)
            return round(size, 1) + "TB";

        return size + "B";
    }

    public static String round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        String result = "";
        if ((tmp % factor == 0)) {
            result = (tmp / factor) + "";
        } else {
            result = ((double) tmp / factor) + "";
        }
        return result;
    }
}

enum DataType {
    CONTACTS, MESSAGES, IMAGES, VIDEOS
}