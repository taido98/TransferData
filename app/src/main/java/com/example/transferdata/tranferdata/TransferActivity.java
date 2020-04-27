package com.example.transferdata.tranferdata;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.transferdata.R;
import com.example.transferdata.adapter.adapterTransfer;
import com.example.transferdata.adapter.infoImage;
import com.example.transferdata.adapter.infoItemAudio;
import com.example.transferdata.adapter.infoItemVideo;
import com.example.transferdata.adapter.DataItem;
import com.example.transferdata.adapter.itemAudio;
import com.example.transferdata.adapter.itemImage;
import com.example.transferdata.adapter.itemVideo;
import com.example.transferdata.connect.ConnectActivity;
import com.example.transferdata.media.getAudio;
import com.example.transferdata.media.getDataImage;
import com.example.transferdata.media.getFile;
import com.example.transferdata.media.getVideo;
import com.example.transferdata.service.getApplication;
import com.example.transferdata.socket.clientSocket;
import com.jaeger.library.StatusBarUtil;

import org.apache.commons.lang3.StringUtils;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class TransferActivity extends Activity {
    public static adapterTransfer adapter;
    public static Button btn_done;
    public static List<DataItem> listItem;
    public static ListView listView_tranfer;
    public static LinkedList<Long> sizeItem;
    String Address;
    DataItem item;
    clientSocket socket;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transfer_data);
        btn_done = findViewById(R.id.btn_disconnect);
        this.Address = getIntent().getStringExtra("address");
        listItem = new ArrayList();
        sizeItem = new LinkedList<>();
        Boolean valueOf = Boolean.TRUE;
        DataItem item2 = new DataItem(true, R.drawable.person, "Contact", "info", true);
        this.item = item2;
        listItem.add(item2);
        DataItem item3 = new DataItem(true, R.drawable.call_log, "Call log", "info", true);
        this.item = item3;
        listItem.add(item3);
        DataItem item4 = new DataItem(true, R.drawable.person, "Messenger", "info", true);
        this.item = item4;
        listItem.add(item4);
        DataItem item5 = new DataItem(true, R.drawable.ic_image, "Photo", "info", true);
        this.item = item5;
        listItem.add(item5);
        DataItem item6 = new DataItem(true, R.drawable.person, "Video", "info", true);
        this.item = item6;
        listItem.add(item6);
        DataItem item7 = new DataItem(true, R.drawable.ic_apps, "App", "info", true);
        this.item = item7;
        listItem.add(item7);
        DataItem item8 = new DataItem(true, R.drawable.ic_file, "File", "info", true);
        this.item = item8;
        listItem.add(item8);
        for (int i = ClientActivity.listItem.size() - 1; i >= 0; i--) {
            if (!ClientActivity.listItem.get(i).isChecked()) {
                listItem.remove(i);
            } else {
                sizeItem.addFirst(ClientActivity.SIZE_ALL_ITEM[i]);
            }
        }
        adapter = new adapterTransfer(this, R.layout.list_item_transfer, listItem, sizeItem);
        ListView listView = findViewById(R.id.list_tranfer);
        listView_tranfer = listView;
        listView.setAdapter(adapter);
        tranferAllData();
        clickButtonDone();
        StatusBarUtil.setTransparent(this);
        StatusBarUtil.setLightMode(this);
    }

    /* access modifiers changed from: 0000 */
    public void tranferAllData() {
        new getDataImage();
        List<String> data = new ArrayList<>();
        if (ClientActivity.listItem.get(0).isChecked()) {
            String sb = getExternalFilesDir(null) +
                    "/contacts/contacts.csv";
            data.add(sb);
        }
        if (ClientActivity.listItem.get(1).isChecked()) {
            String sb2 = getExternalFilesDir(null) +
                    "/callogs/callogs.xml";
            data.add(sb2);
        }
        if (ClientActivity.listItem.get(2).isChecked()) {
            String sb3 = getExternalFilesDir(null) +
                    "/messages/messages.xml";
            data.add(sb3);
        }
        if (ClientActivity.listItem.get(3).isChecked()) {
            for (itemImage item2 : getDataImage.listImage) {
                for (infoImage info : item2.getListPathImage()) {
                    if (info.isSelect()) {
                        data.add(info.getSource());
                    }
                }
            }
        }
        if (( ClientActivity.listItem.get(4)).isChecked()) {
            for (itemVideo video : getVideo.listVideo) {
                for (infoItemVideo info2 : video.getListVideo()) {
                    if (info2.isSelect()) {
                        data.add(info2.getSource());
                    }
                }
            }
        }
        if (( ClientActivity.listItem.get(5)).isChecked()) {
            for (DataItem it2 : getApplication.listItem) {
                if (it2.isChecked()) {
                    data.add(it2.getSource());
                }
            }
        }
        if (( ClientActivity.listItem.get(6)).isChecked()) {
            for (DataItem it3 : getFile.listFile) {
                if (it3.isChecked()) {
                    data.add(it3.getSource());
                }
            }
        }
        if (( ClientActivity.listItem.get(7)).isChecked()) {
            for (itemAudio audio : getAudio.listAudio) {
                for (infoItemAudio info2 : audio.getListPathAudio()) {
                    if (info2.isSelect()) {
                        data.add(info2.getSource());
                    }
                }
            }
        }
        clientSocket clientsocket = new clientSocket(this.Address, this, data);
        this.socket = clientsocket;
        clientsocket.start();
    }

    public static void speedTranfer() {
        int size_transfer = 0;
        for (int i = 0; i < sizeItem.size(); i++) {
            size_transfer += sizeItem.get(i);
            PrintStream printStream = System.out;
            String sb = "SIZE " +
                    clientSocket.size +
                    StringUtils.SPACE +
                    size_transfer;
            printStream.println(sb);
            if (clientSocket.size >= size_transfer && listItem.get(i).isStatusLoad()) {
                listItem.get(i).setStatusLoad(false);
                listView_tranfer.post(() -> TransferActivity.adapter.notifyDataSetChanged());
            }
            if (clientSocket.size >= clientSocket.SIZE_ALL_FILE) {
                btn_done.post(() -> {
                    TransferActivity.btn_done.setVisibility(View.VISIBLE);
                    TransferActivity.listView_tranfer.setVisibility(View.GONE);
                });
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void clickButtonDone() {
        btn_done.setOnClickListener(v -> TransferActivity.this.finish());
    }

    public void onBackPressed() {
        ConnectActivity.isConnect(this);
    }
}
