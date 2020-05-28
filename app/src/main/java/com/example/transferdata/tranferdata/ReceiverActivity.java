package com.example.transferdata.tranferdata;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.example.transferdata.R;
import com.example.transferdata.connect.ConnectActivity;
import com.example.transferdata.contact.getAllContact;
import com.example.transferdata.service.getApplication;
import com.example.transferdata.service.getCallLog;
//import com.example.transferdata.service.getContact;
import com.example.transferdata.service.getMessenger;
import com.example.transferdata.socket.serverSocket;
import com.jaeger.library.StatusBarUtil;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import pl.droidsonroids.gif.GifImageView;

public class ReceiverActivity extends Activity {
    public static int countThreadStop = 0;
    private final int CODE_RESTORE_SMS = 121;
    Button btn_done;
    int estimated_time = 0;
    Handler handler;
    NumberProgressBar numberProgressBar;
    GifImageView restoreLoad;
    boolean running;
    int sizeNew = 0;
    int sizeOld = 0;
    int speed = 0;
    int[] time = new int[3];
    TextView txt_speed;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receive_data);
        this.numberProgressBar = findViewById(R.id.number_progress_bar);
        this.txt_speed = findViewById(R.id.txt_speed);
        this.btn_done = findViewById(R.id.btn_disconnect);
        this.restoreLoad = findViewById(R.id.restore_load);
        this.numberProgressBar.setMax(serverSocket.SIZE_ALL_FILE);
        this.numberProgressBar.setProgressTextSize(30.0f);
        this.numberProgressBar.setReachedBarHeight(30.0f);
        this.handler = new Handler();
        this.running = true;
        new Thread(() -> {
            while (serverSocket.size < serverSocket.SIZE_ALL_FILE && ReceiverActivity.this.running) {
                PrintStream printStream = System.out;
                String sb = "RUN....." +
                        serverSocket.size +
                        StringUtils.SPACE +
                        serverSocket.SIZE_ALL_FILE;
                printStream.println(sb);
                ReceiverActivity.this.speedTranfile();
                ReceiverActivity.this.sizeNew = serverSocket.size;
                ReceiverActivity.this.runOnUiThread(() -> ReceiverActivity.this.numberProgressBar.setProgress(serverSocket.size));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (serverSocket.listItem != null) {
                ReceiverActivity.this.restoreData();
            }
        }).start();
        clickButtonDone();
        StatusBarUtil.setTransparent(this);
        StatusBarUtil.setLightMode(this);
    }

    public void speedTranfile() {
        runOnUiThread(() -> {
            ReceiverActivity receivedata = ReceiverActivity.this;
            receivedata.speed = receivedata.sizeNew - ReceiverActivity.this.sizeOld;
            ReceiverActivity receivedata2 = ReceiverActivity.this;
            receivedata2.sizeOld = receivedata2.sizeNew;
            if (ReceiverActivity.this.speed != 0) {
                ReceiverActivity.this.estimated_time = (serverSocket.SIZE_ALL_FILE - serverSocket.size) / ReceiverActivity.this.speed;
                ReceiverActivity.this.time[0] = ReceiverActivity.this.estimated_time / 3600;
                ReceiverActivity.this.estimated_time -= ReceiverActivity.this.time[0] * 3600;
                ReceiverActivity.this.time[1] = ReceiverActivity.this.estimated_time / 60;
                ReceiverActivity.this.time[2] = ReceiverActivity.this.estimated_time - (ReceiverActivity.this.time[1] * 60);
                String str = " m ";
                String str2 = " s ";
                if (ReceiverActivity.this.time[0] != 0 && ReceiverActivity.this.time[1] != 0 && ReceiverActivity.this.time[2] != 0) {
                    TextView textView = ReceiverActivity.this.txt_speed;
                    String sb = ReceiverActivity.this.time[0] +
                            " h" +
                            ReceiverActivity.this.time[1] +
                            str +
                            ReceiverActivity.this.time[2] +
                            str2;
                    textView.setText(sb);
                } else if (ReceiverActivity.this.time[0] == 0 && ReceiverActivity.this.time[1] != 0 && ReceiverActivity.this.time[2] != 0) {
                    TextView textView2 = ReceiverActivity.this.txt_speed;
                    String sb2 = ReceiverActivity.this.time[1] +
                            str +
                            ReceiverActivity.this.time[2] +
                            str2;
                    textView2.setText(sb2);
                } else if (ReceiverActivity.this.time[0] == 0 && ReceiverActivity.this.time[1] == 0 && ReceiverActivity.this.time[2] != 0) {
                    TextView textView3 = ReceiverActivity.this.txt_speed;
                    String sb3 = ReceiverActivity.this.time[2] +
                            str2;
                    textView3.setText(sb3);
                }
            }
        });
    }

    /* access modifiers changed from: 0000 */
    public void restoreData() {
        countThreadStop = 0;
        for (int i = 0; i < serverSocket.listItem.length; i++) {
            if (!(i == 3 || i == 4 || i == 6|| i == 7 || !serverSocket.listItem[i])) {
                countThreadStop++;
            }
        }
        runOnUiThread(() -> {
            ReceiverActivity.this.restoreLoad.setVisibility(View.VISIBLE);
            ReceiverActivity.this.numberProgressBar.setMax(100);
            ReceiverActivity.this.numberProgressBar.setProgress(100);
            ReceiverActivity.this.numberProgressBar.setVisibility(View.GONE);
            ReceiverActivity.this.txt_speed.setText("Restore Data");
        });
        new Thread(() -> {
            if (serverSocket.listItem[0]) {
                new getAllContact().restoreVCF(ReceiverActivity.this);
                runOnUiThread(() -> {
                    this.txt_speed.setText(getResources().getString(R.string.restoring_contact));
                });
            }
            if (serverSocket.listItem[1]) {
                new getCallLog(ReceiverActivity.this).restoreCallogs();
                runOnUiThread(() -> {
                    this.txt_speed.setText(getResources().getString(R.string.restoring_calllog));
                });
            }
            if (serverSocket.listItem[2]) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    new getMessenger(ReceiverActivity.this).restoreMessages();
                }
                runOnUiThread(() -> {
                    this.txt_speed.setText(getResources().getString(R.string.restoring_message));
                });
            }
            if (serverSocket.listItem[5]) {
                new getApplication(ReceiverActivity.this).restoreApps();
            }
            ReceiverActivity.this.runOnUiThread(() -> {
                ReceiverActivity.this.txt_speed.setVisibility(View.GONE);
                ReceiverActivity.this.btn_done.setVisibility(View.VISIBLE);
            });
        }).start();
    }

    public void onBackPressed() {
        ConnectActivity.isConnect(this);
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 121) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                getMessenger.executeRestore();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /* access modifiers changed from: 0000 */
    public void clickButtonDone() {
//        StringBuilder sb = new StringBuilder();
//        sb.append(getExternalFilesDir(null));
//        sb.append("");
//        try {
//            FileUtils.deleteDirectory(new File(sb.toString()));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        this.btn_done.setOnClickListener(v -> ReceiverActivity.this.finish());
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        this.running = false;
//        StringBuilder sb = new StringBuilder();
//        sb.append(getExternalFilesDir(null));
//        sb.append("");
//        try {
//            FileUtils.deleteDirectory(new File(sb.toString()));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        super.onDestroy();
    }
}
