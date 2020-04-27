package com.example.transferdata.adapter;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;

public class itemVideo implements Parcelable {
    public static final Creator<itemVideo> CREATOR = new Creator<itemVideo>() {
        public itemVideo createFromParcel(Parcel in) {
            return new itemVideo(in);
        }

        public itemVideo[] newArray(int size) {
            return new itemVideo[size];
        }
    };
    String folderName;
    List<infoItemVideo> listVideo;
    Boolean select;

    public itemVideo(String folderName, boolean select, List<infoItemVideo> listVideo) {
        this.folderName = folderName;
        this.select = select;
        this.listVideo = listVideo;
    }

    private itemVideo(Parcel in) {
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
        this.listVideo = in.createTypedArrayList(infoItemVideo.CREATOR);
    }

    public String getFolderName() {
        return this.folderName;
    }

    public void setFolderName(String folderName2) {
        this.folderName = folderName2;
    }

    public List<infoItemVideo> getListVideo() {
        return this.listVideo;
    }

    public void setListVideo(List<infoItemVideo> listVideo2) {
        this.listVideo = listVideo2;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.folderName);
        Boolean bool = this.select;
        int i = bool == null ? 0 : bool ? 1 : 2;
        dest.writeByte((byte) i);
        dest.writeTypedList(this.listVideo);
    }
}
