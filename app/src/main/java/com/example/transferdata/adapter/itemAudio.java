package com.example.transferdata.adapter;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class itemAudio implements Parcelable {
    public static final Creator<itemAudio> CREATOR = new Creator<itemAudio>() {
        public itemAudio createFromParcel(Parcel in) {
            return new itemAudio(in);
        }

        public itemAudio[] newArray(int size) {
            return new itemAudio[size];
        }
    };
    String folderName;
    List<infoItemAudio> listPathAudio;
    Boolean select;

    public itemAudio(String folderName2, Boolean select2, List<infoItemAudio> listPathAudio2) {
        this.folderName = folderName2;
        this.select = select2;
        this.listPathAudio = listPathAudio2;
    }

    private itemAudio(Parcel in) {
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
        this.listPathAudio = in.createTypedArrayList(infoItemAudio.CREATOR);
    }

    public String getFolderName() {
        return this.folderName;
    }

    public void setFolderName(String folderName2) {
        this.folderName = folderName2;
    }

    public List<infoItemAudio> getListPathAudio() {
        return this.listPathAudio;
    }

    public void setListPathAudio(List<infoItemAudio> listPathAudio2) {
        this.listPathAudio = listPathAudio2;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.folderName);
        Boolean bool = this.select;
        int i = bool == null ? 0 : bool ? 1 : 2;
        dest.writeByte((byte) i);
        dest.writeTypedList(this.listPathAudio);
    }
}

