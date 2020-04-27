package com.example.transferdata.adapter;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.List;

public class itemImage implements Parcelable {
    public static final Creator<itemImage> CREATOR = new Creator<itemImage>() {
        public itemImage createFromParcel(Parcel in) {
            return new itemImage(in);
        }

        public itemImage[] newArray(int size) {
            return new itemImage[size];
        }
    };
    String folderName;
    List<infoImage> listPathImage;
    Boolean select;

    public itemImage(String folderName2, Boolean select2, List<infoImage> listPathImage2) {
        this.folderName = folderName2;
        this.select = select2;
        this.listPathImage = listPathImage2;
    }

    private itemImage(Parcel in) {
        Boolean bool;
        this.folderName = in.readString();
        byte tmpSelect = in.readByte();
        if (tmpSelect == 0) {
            bool = null;
        } else {
            boolean check = true;
            if (tmpSelect != 1) {
                check = false;
            }
            bool = check;
        }
        this.select = bool;
        this.listPathImage = in.createTypedArrayList(infoImage.CREATOR);
    }

    public String getFolderName() {
        return this.folderName;
    }

    public void setFolderName(String folderName2) {
        this.folderName = folderName2;
    }

    public List<infoImage> getListPathImage() {
        return this.listPathImage;
    }

    public void setListPathImage(List<infoImage> listPathImage2) {
        this.listPathImage = listPathImage2;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.folderName);
        Boolean bool = this.select;
        int i = bool == null ? 0 : bool ? 1 : 2;
        dest.writeByte((byte) i);
        dest.writeTypedList(this.listPathImage);
    }
}
