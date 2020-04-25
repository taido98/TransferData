package com.example.transferdata.tranferdata;
import android.content.Intent;
import android.net.ParseException;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.transferdata.Interface.ClickCheckBoxListener;
import com.example.transferdata.Interface.ClickItemDataListener;
import com.example.transferdata.R;
import com.example.transferdata.adapter.DataItemAdapter;
import com.example.transferdata.adapter.DataItem;
import com.example.transferdata.connect.ConnectActivity;
import com.example.transferdata.media.getAudio;
import com.example.transferdata.security.AES;
import com.example.transferdata.media.DetailListVideo;
import com.example.transferdata.media.DetailListFile;
import com.example.transferdata.media.DetailListApp;
import com.example.transferdata.media.getDataImage;
import com.example.transferdata.media.getFile;
import com.example.transferdata.media.getVideo;
import com.example.transferdata.media.DetailListImage;
import com.example.transferdata.media.DetailMessage;
import com.example.transferdata.service.getApplication;
import com.example.transferdata.service.getCallLog;
import com.example.transferdata.service.getContact;
import com.example.transferdata.service.getMessenger;
import java.util.ArrayList;
import java.util.List;


public class ClientActivity extends AppCompatActivity implements ClickItemDataListener, ClickCheckBoxListener {
    public static long[] SIZE_ALL_ITEM;
    private DataItemAdapter mDataItemAdapter;
    public static List<DataItem> listItem;
    private String Address;
    private getApplication application;
    private TextView mSelectedItems;
    private getCallLog callLog;
    private getContact contact;
    private getFile file;
//    private getAudio audio;
    private final getDataImage getImage = new getDataImage();
    final String info = "0 MB";
    private DataItem item;
    private getMessenger messenger;
    private final getVideo video = new getVideo(this);
//    private final getAudio audio = new getAudio(this);
    CheckBox mCheckBoxSelectAll;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_data);
        initData();
        getData();
        updateSelectedItem(listItem);
    }

    private void initData() {
        listItem = new ArrayList();
        DataItem item2 = new DataItem(true, R.drawable.person, "Contact", "0 MB", true);
        this.item = item2;
        listItem.add(item2);
        DataItem item3 = new DataItem(true, R.drawable.call_log, "Call log", "0 MB", true);
        this.item = item3;
        listItem.add(item3);
        DataItem item4 = new DataItem(true, R.drawable.messages, "Messenger", "0 MB", true);
        this.item = item4;
        listItem.add(item4);
        DataItem item5 = new DataItem(true, R.drawable.images, "Photo", "0 MB", true);
        this.item = item5;
        listItem.add(item5);
        DataItem item6 = new DataItem(true, R.drawable.videos, "Video", "0 MB", true);
        this.item = item6;
        listItem.add(item6);
        DataItem item7 = new DataItem(true, R.drawable.ic_apps, "App", "0 MB", true);
        this.item = item7;
        listItem.add(item7);
        DataItem item8 = new DataItem(true, R.drawable.ic_file, "File", "0 MB", true);
        this.item = item8;
        listItem.add(item8);
//        DataItem Audio = new DataItem(true, R.drawable.musics, "Audio", "0 MB", true);
//        this.item = Audio;
//        listItem.add(Audio);
        mDataItemAdapter = new DataItemAdapter(this,listItem);
        RecyclerView mRcvListItem = findViewById(R.id.list_item);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRcvListItem.setLayoutManager(layoutManager);
        mDataItemAdapter.setClickItemData(this);
        mDataItemAdapter.setClickCheckBox(this);
        mRcvListItem.setAdapter(mDataItemAdapter);
        SIZE_ALL_ITEM = new long[listItem.size()];
        this.Address = getIntent().getStringExtra("address");
        click_button();
        AES encryptaes = new AES();
        encryptaes.createKey();
    }

    /* access modifiers changed from: 0000 */
    private void click_button() {
        mCheckBoxSelectAll = findViewById(R.id.select_all);
        mSelectedItems =findViewById(R.id.selected_items);
        mCheckBoxSelectAll.setOnClickListener(v -> {
//            int totalSize=0;
            for (DataItem item : listItem) {
                item.setChecked(((CheckBox) v).isChecked());
//                totalSize+=item.getSize();
                Log.d("Size??",""+item.getSize());
            }
            mDataItemAdapter.notifyDataSetChanged();
            updateSelectedItem(listItem);
//            mSelectedItems.setText((new item()).sizeToString(totalSize));
        });

        Button button = findViewById(R.id.btn_send);
        button.setOnClickListener(v -> ConnectActivity.manager.requestConnectionInfo(ConnectActivity.channel, info -> {
            if (info.groupFormed) {
                Intent intent = new Intent(ClientActivity.this, TransferActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("address", ClientActivity.this.Address);
                ClientActivity.this.startActivity(intent);
                ClientActivity.this.finish();
                return;
            }
            Toast.makeText(ClientActivity.this, "Failed to connect ", Toast.LENGTH_LONG).show();
            ClientActivity.this.finish();
        }));
        Button button2 = findViewById(R.id.btn_cancel);
        button2.setOnClickListener(v -> ConnectActivity.isConnect(ClientActivity.this));
    }

    public void onBackPressed() {
        ConnectActivity.isConnect(this);
    }

    /* access modifiers changed from: 0000 */
    private void getData() {
        new Thread(() -> {
            ClientActivity.this.contact = new getContact(ClientActivity.this);
            ClientActivity client = ClientActivity.this;
            client.item = new DataItem(true, R.drawable.person, "Contact", getContact.backupContacts(), Boolean.FALSE);
            listItem.set(0, ClientActivity.this.item);
            ClientActivity.this.updateUI();
        }).start();
        new Thread(() -> {
            ClientActivity.this.callLog = new getCallLog(ClientActivity.this);
            ClientActivity client = ClientActivity.this;
            client.item = new DataItem(true, R.drawable.call_log, "Call log", callLog.backupCallogs(), Boolean.FALSE);
            listItem.set(1, ClientActivity.this.item);
            ClientActivity.this.updateUI();
        }).start();
        new Thread(() -> {
            ClientActivity.this.messenger = new getMessenger(ClientActivity.this);
            try {
                ClientActivity client = ClientActivity.this;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    client.item = new DataItem(true, R.drawable.messages, "Messenger", getMessenger.backupMessages(), Boolean.FALSE);
                }
                listItem.set(2, ClientActivity.this.item);
                ClientActivity.this.updateUI();
            } catch (ParseException | java.text.ParseException e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            ClientActivity.this.getImage.getImagesPath(ClientActivity.this);
            ClientActivity client = ClientActivity.this;
            client.item = new DataItem(true, R.drawable.images, "Images", getImage.getSize(), false);
            listItem.set(3, ClientActivity.this.item);
            ClientActivity.this.video.getVideoPath();
            ClientActivity client2 = ClientActivity.this;
            client2.item = new DataItem(true, R.drawable.videos, "Video", video.getleng(), false);
            listItem.set(4, ClientActivity.this.item);
//            ClientActivity.this.audio.getAudioPath();
//            ClientActivity.this.item =  new DataItem(true, R.drawable.musics, "Audio", audio.getleng(), false);
//            listItem.set(7, ClientActivity.this.item);
            ClientActivity.this.updateUI();
        }).start();
        Thread threadApplication = new Thread(() -> {
            ClientActivity.this.application = new getApplication(ClientActivity.this);
            ClientActivity client = ClientActivity.this;
            client.item = new DataItem(true, R.drawable.ic_apps, "App", application.backupApps(), Boolean.FALSE);
            listItem.set(5, ClientActivity.this.item);
            ClientActivity.this.updateUI();
        });
        new Thread(() -> {
            ClientActivity.this.file = new getFile(ClientActivity.this);
            ClientActivity.this.file.getAllFile();
            ClientActivity client = ClientActivity.this;
            client.item = new DataItem(true, R.drawable.ic_file, "File", file.getSize(), Boolean.FALSE);
            listItem.set(6, ClientActivity.this.item);
        }).start();
        threadApplication.start();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == -1) {
            new Thread(() -> {
                ( ClientActivity.listItem.get(0)).setStatusLoad(true);
                ClientActivity.this.updateUI();
                ClientActivity client = ClientActivity.this;
                client.item = new DataItem(true, R.drawable.person, "Contact", getContact.backupContacts(), Boolean.FALSE);
                listItem.set(0, ClientActivity.this.item);
                ClientActivity.this.updateUI();
            }).start();
        } else if (requestCode == 2 && resultCode == -1) {
            new Thread(() -> {
                try {
                    ( ClientActivity.listItem.get(2)).setStatusLoad(true);
                    ClientActivity.this.updateUI();
                    ClientActivity client = ClientActivity.this;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        client.item = new DataItem(true, R.drawable.messages, "Messenger", getMessenger.backupMessages(), Boolean.FALSE);
                    }
                    listItem.set(2, ClientActivity.this.item);
                    ClientActivity.this.updateUI();
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }
            }).start();
        } else if (requestCode == 3 && resultCode == -1) {
            listItem.get(3).setStatusLoad(true);
            updateUI();
            DataItem item2 = new DataItem(true, R.drawable.images, "Images", getImage.getSize(), Boolean.FALSE);
            this.item = item2;
            listItem.set(3, item2);
            updateUI();
        } else if (requestCode == 4 && resultCode == -1) {
            listItem.get(4).setStatusLoad(true);
            updateUI();
            DataItem item3 = new DataItem(true, R.drawable.videos, "Video", this.video.getleng(), Boolean.FALSE);
            this.item = item3;
            listItem.set(4, item3);
            updateUI();
        } else if (requestCode == 5 && resultCode == -1) {
            listItem.get(5).setStatusLoad(true);
            updateUI();
            DataItem item4 = new DataItem(true, R.drawable.ic_apps, "App", DetailListApp.getInfo(), Boolean.FALSE);
            this.item = item4;
            listItem.set(5, item4);
            updateUI();
        } else if (requestCode == 6 && resultCode == -1) {
            listItem.get(6).setStatusLoad(true);
            updateUI();
            DataItem item5 = new DataItem(true, R.drawable.ic_file, "File", file.getSize(), Boolean.FALSE);
            this.item = item5;
            listItem.set(6, item5);
            updateUI();
        }
//        else if (requestCode == 7 && resultCode == -1) {
//            listItem.get(7).setStatusLoad(true);
//            updateUI();
//            DataItem audioItem = new DataItem(true, R.drawable.musics, "Audio", audio.getleng(), Boolean.FALSE);
//            this.item = audioItem;
//            listItem.set(7, audioItem);
//            updateUI();
//        }
    }

    @Override
    public void onItemData(int position) {
        Intent intent = null;
        switch (position) {
            case 0:
            case 1:
                Toast.makeText(this, "No action", Toast.LENGTH_LONG).show();
//                        intent = new Intent(this, contact.class);
//                        intent.putParcelableArrayListExtra("listCoantact", getContact.listItem);
                break;
            case 2:
                intent = new Intent(this, DetailMessage.class);
                intent.putParcelableArrayListExtra("listMessenger", getMessenger.listItem);
                break;
            case 3:
                intent = new Intent(this, DetailListImage.class);
                intent.putParcelableArrayListExtra("listImage", (ArrayList) getDataImage.listImage);
                break;
            case 4:
                intent = new Intent(this, DetailListVideo.class);
                intent.putParcelableArrayListExtra("listVideo", (ArrayList) getVideo.listVideo);
                break;
            case 5:
                intent = new Intent(this, DetailListApp.class);
                intent.putParcelableArrayListExtra("listApp", getApplication.listItem);
                break;
            case 6:
                intent = new Intent(this, DetailListFile.class);
                intent.putParcelableArrayListExtra("listFile", (ArrayList) getFile.listFile);
                break;
            case 7:
                intent = new Intent(this, DetailListVideo.class);
                intent.putParcelableArrayListExtra("listAudio", (ArrayList) getAudio.listAudio);
                break;
            default:
                intent = new Intent();
                break;
        }
        if (position != 1) {
            this.startActivityForResult(intent, position);
        }
    }
    public void updateUI() {
        runOnUiThread(() -> mDataItemAdapter.notifyDataSetChanged());
    }
    public void updateSelectedItem(List<DataItem> listItem) {
        long size = 0;
        int items = 0;
        for (DataItem dataItem : listItem) {
            if (dataItem.isChecked()) {
                items++;
                size += dataItem.getSize();
            }
        }
        mSelectedItems.setText(items + " " + this.getString(R.string.selected_item));
    }

    @Override
    public void onItem(int position) {
        mCheckBoxSelectAll.setChecked(true);
        for (DataItem dataItem : listItem) {
            if (!dataItem.isChecked())
                mCheckBoxSelectAll.setChecked(false);
        }
        updateSelectedItem(listItem);
    }
}
