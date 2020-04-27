package com.example.transferdata.tranferdata;

import android.app.Activity;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.transferdata.R;
import com.example.transferdata.connect.ConnectActivity;
import com.example.transferdata.socket.serverSocket;
import com.jaeger.library.StatusBarUtil;


public class ServerActivity extends Activity {
    public static Channel channel;
    public static WifiP2pManager manager;
    Button btn_disconnect;
    serverSocket socket;
    TextView txtNameHost;
    String deviceConnected;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_connect);
        if (getIntent() != null) {
            deviceConnected = getIntent().getStringExtra("Device Connected");
        }
        WifiP2pManager wifiP2pManager = (WifiP2pManager) getSystemService(WIFI_P2P_SERVICE);
        manager = wifiP2pManager;
        channel = wifiP2pManager.initialize(this, getMainLooper(), null);
        Button button = findViewById(R.id.btn_disconnect);
        this.btn_disconnect = button;
        button.setOnClickListener(v -> ConnectActivity.isConnect(ServerActivity.this));
        serverSocket serversocket = new serverSocket(this);
        this.socket = serversocket;
        serversocket.execute(new String[0]);
        show_name_server();
        StatusBarUtil.setTransparent(this);
        StatusBarUtil.setLightMode(this);
    }

    public void onBackPressed() {
        ConnectActivity.isConnect(this);
    }

    private void show_name_server() {
        manager.requestGroupInfo(channel, group -> {
            ServerActivity server = ServerActivity.this;
            server.txtNameHost = server.findViewById(R.id.name_server);
            ServerActivity.this.txtNameHost.setText(deviceConnected);
        });
    }
}
