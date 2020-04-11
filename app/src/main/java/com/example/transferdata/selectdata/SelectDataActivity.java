package com.example.transferdata.selectdata;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.transferdata.R;
import com.example.transferdata.media.MediaItemAdapter;
import com.example.transferdata.media.TestMedia;

import java.util.ArrayList;
import java.util.HashMap;

public class SelectDataActivity extends AppCompatActivity {

    public Button mBtnGetData;
    private RecyclerView mRcvData;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_data);
        initView();
        initAction();
    }

    private void initView() {
        mBtnGetData = (Button) findViewById(R.id.get_data);
        mRcvData = (RecyclerView) findViewById(R.id.rcv_list_data);

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

                String videoPath = "/storage/emulated/0/Download/VID_20200410_224413.mp4";
                String contactPath = "/storage/emulated/0/Download/Contacts.vcf";

                ArrayList<String> listVideos = new ArrayList<>();
                ArrayList<String> listContacts = new ArrayList<>();
                listVideos.add(videoPath);
                listContacts.add(contactPath);

                ArrayList<Data> listData = new ArrayList<>();

                Data data = new Data(DataType.VIDEOS, listVideos);
                Data data2 = new Data(DataType.CONTACTS, listContacts);
                listData.add(data);
                listData.add(data2);
                
                // specify an adapter (see also next example)
                mAdapter = new DataItemAdapter(listData);
                mRcvData.setAdapter(mAdapter);
            }
        });
    }
}
