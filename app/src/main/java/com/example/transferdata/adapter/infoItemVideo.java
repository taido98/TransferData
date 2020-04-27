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
    String name;
    boolean select;
    int size;
    String source;

    public infoItemVideo(boolean select2, int size2, String name2, String source2) {
        this.select = select2;
        this.size = size2;
        this.name = name2;
        this.source = source2;
    }

    protected infoItemVideo(Parcel in) {
        this.select = in.readByte() != 0;
        this.size = in.readInt();
        this.name = in.readString();
        this.source = in.readString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.select ? (byte) 1 : 0);
        dest.writeInt(this.size);
        dest.writeString(this.name);
        dest.writeString(this.source);
    }

    public int describeContents() {
        return 0;
    }

    public String getSource() {
        return this.source;
    }

    public void setSource(String source2) {
        this.source = source2;
    }

    public boolean isSelect() {
        return this.select;
    }

    public void setSelect(boolean select2) {
        this.select = select2;
    }

    public int getSize() {
        return this.size;
    }

    public void setSize(int size2) {
        this.size = size2;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }
}
