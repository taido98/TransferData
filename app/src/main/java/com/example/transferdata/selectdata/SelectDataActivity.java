package com.example.transferdata.selectdata;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.transferdata.R;
import com.example.transferdata.media.AudioModel;
import com.example.transferdata.media.TestMedia;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class SelectDataActivity extends AppCompatActivity implements ClickItemDataListener {

    public Button mBtnGetData;
    private RecyclerView mRcvData;
    private DataItemAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Context mContext;
    private ArrayList<String> mListVideo;
    private ArrayList<String> mListImage;
    private ArrayList<String> mListAudio;
    private ArrayList<String> mListContact;
    private ArrayList<Data> listData;
    private Button mBtnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_data);
        initData();
        initView();
        initAction();
    }

    private void initData() {
        getAllContact();
        getAllAudio();
        getAllImage();
        getAllVideo();
    }

    private void initView() {
        mBtnGetData = (Button) findViewById(R.id.get_data);
        mRcvData = (RecyclerView) findViewById(R.id.rcv_list_data);
        mBtnSend = findViewById(R.id.btn_send);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRcvData.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        mRcvData.setLayoutManager(layoutManager);
    }

    private void initAction() {
        mBtnGetData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pathContact = getVCF(SelectDataActivity.this);
                Toast.makeText(SelectDataActivity.this, pathContact, Toast.LENGTH_LONG).show();
//                String videoPath = "/storage/emulated/0/Download/VID_20200410_224413.mp4";
//                String contactPath = "/storage/emulated/0/Download/Contacts.vcf";
//
//                ArrayList<String> listVideos = new ArrayList<>();
//                ArrayList<String> listContacts = new ArrayList<>();
//                listVideos.add(videoPath);
//                listContacts.add(contactPath);
            }
        });
        listData = new ArrayList<>();

        Data data2 = new Data(DataType.IMAGES, mListImage);
        Data data = new Data(DataType.VIDEOS, mListVideo, false);
        Data data3 = new Data(DataType.AUDIOS, mListAudio);
        Data data4 = new Data(DataType.CONTACTS, mListContact);
        listData.add(data2);
        listData.add(data);
        listData.add(data3);
        listData.add(data4);

        // specify an adapter (see also next example)
        mAdapter = new DataItemAdapter(getApplicationContext(), listData);
        mAdapter.setClickItemData(this);
        mRcvData.setAdapter(mAdapter);
        mBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    public static String getVCF(Activity activity) {
        String results = "";
        Context mContext = activity.getApplicationContext();
        String[] PERMISSIONS = {
                android.Manifest.permission.READ_CONTACTS,
                android.Manifest.permission.WRITE_CONTACTS,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.GET_ACCOUNTS,
        };

        if (!hasPermissions(mContext, PERMISSIONS)) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS, 100);
        } else {
            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/Contacts.vcf";
            results = path;
            File fdelete = new File(path);
            if (fdelete.exists()) {
                if (fdelete.delete()) {
                    System.out.println("file Deleted :" + path);
                } else {
                    System.out.println("file not Deleted :" + path);
                }
            }
            Cursor phones = mContext.getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    null, null, null);
            phones.moveToFirst();
            for (int i = 0; i < phones.getCount(); i++) {
                String lookupKey = phones.getString(phones
                        .getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
                Uri uri = Uri.withAppendedPath(
                        ContactsContract.Contacts.CONTENT_VCARD_URI,
                        lookupKey);
                AssetFileDescriptor fd;
                try {
                    fd = mContext.getContentResolver().openAssetFileDescriptor(uri, "r");
                    FileInputStream fis = fd.createInputStream();
                    byte[] buf = new byte[(int) fd.getDeclaredLength()];
                    fis.read(buf);
                    String VCard = new String(buf);
                    FileOutputStream mFileOutputStream = new FileOutputStream(path, true);
                    mFileOutputStream.write(VCard.toString().getBytes());
                    phones.moveToNext();
                    Log.d("Contact", VCard);
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        }
        return results;
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public ArrayList<AudioModel> getAudioFromDevice(final Context context) {
        final ArrayList<AudioModel> tempAudioList = new ArrayList<>();

        ContentResolver contentResolver = getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(songUri, null, null, null, null);

        if (songCursor != null && songCursor.moveToFirst()) {

            int songId = songCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songPath = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);

            do {
                AudioModel audioModel = new AudioModel();
                long currentId = songCursor.getLong(songId);
                String currentTitle = songCursor.getString(songTitle);
                String currentPath = songCursor.getString(songPath);
                audioModel.setaPath(currentPath);
                tempAudioList.add(audioModel);
            } while (songCursor.moveToNext());
        }

        return tempAudioList;
    }

    public ArrayList<AudioModel> getImageFromDevice(final Context context) {
        final ArrayList<AudioModel> tempImageList = new ArrayList<>();

        ContentResolver contentResolver = getContentResolver();
        Uri imgUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Toast.makeText(this, imgUri.toString(), Toast.LENGTH_LONG).show();
        Cursor songCursor = contentResolver.query(imgUri, null, null, null, null);

        if (songCursor != null && songCursor.moveToFirst()) {

            int imgId = songCursor.getColumnIndex(MediaStore.Images.Media._ID);
            int imgTitle = songCursor.getColumnIndex(MediaStore.Images.Media.TITLE);
            int imgPath = songCursor.getColumnIndex(MediaStore.Images.Media.DATA);
            Log.d("img path?", "ada" + imgPath
            );
            do {
                AudioModel audioModel = new AudioModel();
                long currentId = songCursor.getLong(imgId);
                String currentTitle = songCursor.getString(imgTitle);
                String currentPath = songCursor.getString(imgPath);

                audioModel.setaPath(currentPath);
                tempImageList.add(audioModel);
            } while (songCursor.moveToNext());
        }

        return tempImageList;
    }

    public ArrayList<AudioModel> getVideoFromDevice(final Context context) {
        final ArrayList<AudioModel> tempVideoList = new ArrayList<>();

        ContentResolver contentResolver = getContentResolver();
        Uri VideoUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        Toast.makeText(this, VideoUri.toString(), Toast.LENGTH_LONG).show();
        Cursor songCursor = contentResolver.query(VideoUri, null, null, null, null);

        if (songCursor != null && songCursor.moveToFirst()) {

            int videoId = songCursor.getColumnIndex(MediaStore.Video.Media._ID);
            int videoTitle = songCursor.getColumnIndex(MediaStore.Video.Media.TITLE);
            int videoPath = songCursor.getColumnIndex(MediaStore.Video.Media.DATA);
            Log.d("img path?", "ada" + videoPath);
            do {
                AudioModel audioModel = new AudioModel();
                long currentId = songCursor.getLong(videoId);
                String currentTitle = songCursor.getString(videoTitle);
                String currentPath = songCursor.getString(videoPath);

                audioModel.setaPath(currentPath);
                tempVideoList.add(audioModel);
            } while (songCursor.moveToNext());
        }

        return tempVideoList;
    }

    private void getAllImage() {
        ArrayList<AudioModel> listImage = getImageFromDevice(SelectDataActivity.this);
        mListImage = new ArrayList<>();
        System.out.println("audio: " + listImage.size());
        for (AudioModel audio : listImage) {
            System.out.println("audio path: " + audio.getaPath());
//                    if(audio.getaPath() == )
            mListImage.add(audio.getaPath());
        }
    }

    private void getAllAudio() {
        ArrayList<AudioModel> listAudio = getAudioFromDevice(SelectDataActivity.this);
        mListAudio = new ArrayList<>();
        System.out.println("audio: " + listAudio.size());
        for (AudioModel audio : listAudio) {
            System.out.println("audio path: " + audio.getaPath());
//                    if(audio.getaPath() == )
            mListAudio.add(audio.getaPath());
        }
    }

    private void getAllVideo() {
        ArrayList<AudioModel> listVideo = getVideoFromDevice(SelectDataActivity.this);
        mListVideo = new ArrayList<>();
        for (AudioModel audio : listVideo) {
            System.out.println("audio path: " + audio.getaPath());
//                    if(audio.getaPath() == )
            mListVideo.add(audio.getaPath());
        }
    }

    private void getAllContact() {
        mListContact = new ArrayList<>();
        String pathContact = getVCF(this);
        Toast.makeText(this, pathContact, Toast.LENGTH_LONG).show();
        mListContact.add(pathContact);
    }

    @Override
    public void onItemData(int position) {
        Intent intent = new Intent(SelectDataActivity.this, TestMedia.class);
        intent.putStringArrayListExtra("List Data", listData.get(position).getUrlData());
        startActivity(intent);
    }
//    public static boolean hasPermissions(Context context, String... permissions) {
//        if (context != null && permissions != null) {
//            for (String permission : permissions) {
//                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
//                    return false;
//                }
//            }
//        }
//        return true;
//    }
}
