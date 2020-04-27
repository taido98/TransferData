package com.example.transferdata.adapter;

import android.os.Parcel;
import android.os.Parcelable;

public class infoItemAudio implements Parcelable {
    public static final Creator<infoItemAudio> CREATOR = new Creator<infoItemAudio>() {
        public infoItemAudio createFromParcel(Parcel in) {
            return new infoItemAudio(in);
        }

        public infoItemAudio[] newArray(int size) {
            return new infoItemAudio[size];
        }
    };
    String name;
    boolean select;
    int size;
    String source;

    public infoItemAudio(boolean select2, int size2, String name2, String source2) {
        this.select = select2;
        this.size = size2;
        this.name = name2;
        this.source = source2;
    }

    protected infoItemAudio(Parcel in) {
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
