package com.example.transferdata.selectdata;

import android.content.Context;

import com.example.transferdata.R;

import java.io.File;
import java.util.ArrayList;

public class Data {
    private DataType mDataType;
    private boolean mChecker;
    private ArrayList<String> mDataUrls;

    public Data(DataType mDataType, ArrayList<String> mUrlsData) {
        this.mDataType = mDataType;
        this.mDataUrls = mUrlsData;
        this.mChecker = true;
    }

    public Data(DataType mDataType, ArrayList<String> mUrlsData, boolean mChecker) {
        this.mDataType = mDataType;
        this.mDataUrls = mUrlsData;
        this.mChecker = mChecker;
    }

    public int getImageType() {
        switch (mDataType) {
            case IMAGES:
                return R.drawable.images;
            case VIDEOS:
                return R.drawable.videos;
            case AUDIOS:
                return R.drawable.musics;
            case MESSAGES:
                return R.drawable.messages;
            default:
                return R.drawable.person;
        }
    }

    public String getDataType(Context context) {
        switch (mDataType) {
            case IMAGES:
                return context.getString(R.string.images);
            case VIDEOS:
                return context.getString(R.string.videos);
            case AUDIOS:
                return context.getString(R.string.audios);
            case MESSAGES:
                return context.getString(R.string.messages);
            default:
                return context.getString(R.string.contacts);
        }
    }

    public boolean getChecker() {
        return mChecker;
    }

    public void setChecker(boolean mChecker) {
        this.mChecker = mChecker;
    }

    public void setDataType(DataType mTypeData) {
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
    CONTACTS, MESSAGES, IMAGES, VIDEOS, AUDIOS
}