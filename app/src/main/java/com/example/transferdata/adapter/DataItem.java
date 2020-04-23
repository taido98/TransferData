package com.example.transferdata.adapter;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

public class DataItem implements Parcelable {
    public static final Creator<DataItem> CREATOR = new Creator<DataItem>() {
        public DataItem createFromParcel(Parcel in) {
            return new DataItem(in);
        }

        public DataItem[] newArray(int size) {
            return new DataItem[size];
        }
    };
    boolean checked;
    Drawable imgDrawable;
    int img_resource;
    String info;
    String name;
    int size;
    String source;
    boolean statusLoad;

    protected DataItem(Parcel in) {
        boolean z = true;
        this.checked = in.readByte() != 0;
        if (in.readByte() == 0) {
            z = false;
        }
        this.statusLoad = z;
        this.img_resource = in.readInt();
        this.size = in.readInt();
        this.name = in.readString();
        this.info = in.readString();
        this.source = in.readString();
    }

    public Drawable getImgDrawable() {
        return this.imgDrawable;
    }

    public void setImgDrawable(Drawable imgDrawable2) {
        this.imgDrawable = imgDrawable2;
    }

    public boolean isChecked() {
        return this.checked;
    }

    public int getSize() {
        return this.size;
    }

    public void setSize(int size2) {
        this.size = size2;
    }

    public String getSource() {
        return this.source;
    }

    public void setSource(String source2) {
        this.source = source2;
    }

    public void setChecked(boolean checked2) {
        this.checked = checked2;
    }

    public int getImg() {
        return this.img_resource;
    }

    public void setImg(int img) {
        this.img_resource = img;
    }

    public String getTextView() {
        return this.name;
    }

    public void setTextView(String textView) {
        this.name = textView;
    }

    public boolean isStatusLoad() {
        return this.statusLoad;
    }

    public void setStatusLoad(boolean statusLoad2) {
        this.statusLoad = statusLoad2;
    }

    public int getImg_resource() {
        return this.img_resource;
    }

    public void setImg_resource(int img_resource2) {
        this.img_resource = img_resource2;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public String getInfo() {
        return this.info;
    }

    public void setInfo(String info2) {
        this.info = info2;
    }

    public DataItem(boolean checked2, int img_resource2, String name2, String info2, Boolean statusLoad2) {
        this.checked = checked2;
        this.img_resource = img_resource2;
        this.name = name2;
        this.info = info2;
        this.statusLoad = statusLoad2.booleanValue();
    }

    public DataItem() {
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.checked ? (byte) 1 : 0);
        dest.writeByte(this.statusLoad ? (byte) 1 : 0);
        dest.writeInt(this.img_resource);
        dest.writeInt(this.size);
        dest.writeString(this.name);
        dest.writeString(this.info);
        dest.writeString(this.source);
    }
}
