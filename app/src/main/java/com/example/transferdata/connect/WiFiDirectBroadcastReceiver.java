package com.example.transferdata.connect;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.os.Build.VERSION;
import android.widget.TextView;

import com.example.transferdata.R;
import com.example.transferdata.tranferdata.ClientActivity;
import com.example.transferdata.tranferdata.ServerActivity;

import java.util.Objects;


public class WiFiDirectBroadcastReceiver extends BroadcastReceiver {
    Boolean NewPhone;
    WifiP2pDevice device;
    public ConnectActivity mActivity;
    private Channel mChannel;
    public WifiP2pManager mManager;
    String name = null;

    public WiFiDirectBroadcastReceiver(WifiP2pManager manager, Channel channel, ConnectActivity activity, Boolean NewPhone) {
        this.mManager = manager;
        this.mChannel = channel;
        this.mActivity = activity;
        this.NewPhone = NewPhone;
    }

    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if ("android.net.wifi.p2p.STATE_CHANGED".equals(action)) {
            if (intent.getIntExtra("wifi_p2p_state", -1) == 2) {
                System.out.println("WIFI enable...");
            } else {
                System.out.println("WIFI disable..");
                if (VERSION.SDK_INT >= 29) {
                    this.mActivity.statusWifi(false);
                }
                ((WifiManager) this.mActivity.getApplicationContext().getSystemService(Context.WIFI_SERVICE)).setWifiEnabled(true);
            }
        } else if ("android.net.wifi.p2p.PEERS_CHANGED".equals(action)) {
            if (mManager != null  && NewPhone) {
                System.out.println("start scan device...");
                mManager.requestPeers(mChannel, this.mActivity);
            }
        } else if (!"android.net.wifi.p2p.CONNECTION_STATE_CHANGE".equals(action) && "android.net.wifi.p2p.THIS_DEVICE_CHANGED".equals(action)) {
            WifiP2pDevice wifiP2pDevice = intent.getParcelableExtra("wifiP2pDevice");
            device = wifiP2pDevice;
            if (wifiP2pDevice != null) {
                name = wifiP2pDevice.deviceName;
            }
        }
        get_info_device();
        TextView txt_name_device = mActivity.findViewById(R.id.device_name);
        String str = name;
        if (str != null && txt_name_device != null) {
            txt_name_device.setText(str);
        }
    }

    /* access modifiers changed from: 0000 */
    public void get_info_device() {
        if (VERSION.SDK_INT >= 29) {
            mManager.requestDeviceInfo(mChannel, wifiP2pDevice -> WiFiDirectBroadcastReceiver.this.name = Objects.requireNonNull(wifiP2pDevice).deviceName);
        }
        mManager.requestConnectionInfo(mChannel, info -> {
            if (WiFiDirectBroadcastReceiver.this.mManager != null) {
                if (info.groupFormed && info.isGroupOwner) {
                    Intent intent1 = new Intent(WiFiDirectBroadcastReceiver.this.mActivity, ServerActivity.class);
                    intent1.putExtra("Device Connected",mActivity.DeviceConnected);
//                    Log.d("Device Connected",mActivity.DeviceConnected);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    WiFiDirectBroadcastReceiver.this.mActivity.startActivity(intent1);
                    WiFiDirectBroadcastReceiver.this.mActivity.finish();
                } else if (info.groupFormed) {
                    Intent intent1 = new Intent(WiFiDirectBroadcastReceiver.this.mActivity, ClientActivity.class);
                    intent1.putExtra("address", info.groupOwnerAddress.getHostAddress());
                    intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    WiFiDirectBroadcastReceiver.this.mActivity.startActivity(intent1);
                    WiFiDirectBroadcastReceiver.this.mActivity.finish();
                }
            }
        });
        if (VERSION.SDK_INT >= 29) {
            mManager.requestNetworkInfo(this.mChannel, networkInfo -> System.out.println("android 10"));
        }
    }
}
