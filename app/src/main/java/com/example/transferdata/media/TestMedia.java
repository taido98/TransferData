package com.example.transferdata.media;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.transferdata.R;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TestMedia extends AppCompatActivity {

    private Button mBtnGetVideo;
    private Button mBtnGetAudio;
    private RecyclerView mRcvPathVideo;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);
        initView();
        initAction();
    }

    private void initView() {
        mBtnGetVideo = (Button) findViewById(R.id.get_video);
        mBtnGetAudio = (Button) findViewById(R.id.get_audio);
        mRcvPathVideo = (RecyclerView) findViewById(R.id.video_paths);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRcvPathVideo.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        mRcvPathVideo.setLayoutManager(layoutManager);

    }

    private void initAction() {
        mBtnGetVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, ArrayList<String>> listVideoByFolder = getVideo(TestMedia.this);
                ArrayList<String> listPath = new ArrayList<>();
                for (String key : listVideoByFolder.keySet()) {
                    ArrayList<String> value = listVideoByFolder.get(key);
                    System.out.println("With key: " + key);
                    for (String path : value) {
                        System.out.println("value path: " + path);
                        listPath.add(path);
                    }
                }

                // specify an adapter (see also next example)
                mAdapter = new MediaItemAdapter(listPath);
                mRcvPathVideo.setAdapter(mAdapter);
            }
        });

        mBtnGetAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<AudioModel> listAudio = getAllAudioFromDevice(TestMedia.this);
                ArrayList<String> listPath = new ArrayList<>();
                System.out.println("audio: " + listAudio.size());
                for (AudioModel audio : listAudio) {
                    System.out.println("audio path: " + audio.getaPath());
                    listPath.add(audio.getaPath());
                }

                // specify an adapter (see also next example)
                mAdapter = new MediaItemAdapter(listPath);
                mRcvPathVideo.setAdapter(mAdapter);
            }
        });
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

    public static HashMap getVideo(Activity activity) {
        ArrayList<String> allVideoFolder = new ArrayList<>();
        HashMap<String, ArrayList<String>> listVideoByFolder = new HashMap<>();

        Context mContext = activity.getApplicationContext();
        String[] PERMISSIONS = {
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.GET_ACCOUNTS,
        };

        if (!hasPermissions(mContext, PERMISSIONS)) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS, 100);
        } else {
            String[] projection = new String[]{MediaStore.Video.Media.DATA,
                    MediaStore.Video.Media._ID,
                    MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
                    MediaStore.Video.Media.DATE_TAKEN};
            Uri images = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            final String orderBy = MediaStore.Video.Media.DATE_TAKEN;
            Cursor cur = activity.getContentResolver().query(images, projection, // Which
                    // columns
                    // to return
                    null, // Which rows to return (all rows)
                    null, // Selection arguments (none)
                    orderBy + " DESC" // Ordering
            );
            ArrayList<String> imagePath;
            if (cur.moveToFirst()) {
                String bucket, date;
                int bucketColumn = cur.getColumnIndex(MediaStore.Video.Media.BUCKET_DISPLAY_NAME);
                int dateColumn = cur.getColumnIndex(MediaStore.Video.Media.DATE_TAKEN);
                do {
                    bucket = cur.getString(bucketColumn);
                    date = cur.getString(dateColumn);
                    if (!allVideoFolder.contains(bucket)) {
                        allVideoFolder.add(bucket);
                    }
                    imagePath = listVideoByFolder.get(bucket);
                    if (imagePath == null) {
                        imagePath = new ArrayList<String>();
                    }
                    imagePath.add(cur.getString(cur
                            .getColumnIndex(MediaStore.Images.Media.DATA)));
                    listVideoByFolder.put(bucket, imagePath);
                } while (cur.moveToNext());
            }
        }
        return listVideoByFolder;
    }

    public ArrayList<AudioModel> getAllAudioFromDevice(final Context context) {
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


}