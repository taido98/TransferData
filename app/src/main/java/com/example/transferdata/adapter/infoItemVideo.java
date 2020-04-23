package com.example.transferdata.adapter;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class infoItemVideo implements Parcelable {
    public static final Creator<infoItemVideo> CREATOR = new Creator<infoItemVideo>() {
        public infoItemVideo createFromParcel(Parcel in) {
            return new infoItemVideo(in);
        }

        public infoItemVideo[] newArray(int size) {
            return new infoItemVideo[size];
        }
    };
    boolean select;
    int size;
    String source;
    String thumbnails;

    public infoItemVideo(String source2, String thumbnails2, boolean select2, int size2) {
        this.source = source2;
        this.thumbnails = thumbnails2;
        this.select = select2;
        this.size = size2;
    }

    public int getSize() {
        return this.size;
    }

    public void setSize(int size2) {
        this.size = size2;
    }

    public static Creator<infoItemVideo> getCREATOR() {
        return CREATOR;
    }

    protected infoItemVideo(Parcel in) {
        this.source = in.readString();
        this.thumbnails = in.readString();
        this.select = in.readByte() != 0;
    }

    public String getSource() {
        return this.source;
    }

    public void setSource(String source2) {
        this.source = source2;
    }

    public String getThumbnails() {
        return this.thumbnails;
    }

    public void setThumbnails(String thumbnails2) {
        this.thumbnails = thumbnails2;
    }

    public boolean isSelect() {
        return this.select;
    }

    public void setSelect(boolean select2) {
        this.select = select2;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.source);
        dest.writeString(this.thumbnails);
        dest.writeByte(this.select ? (byte) 1 : 0);
    }
}
