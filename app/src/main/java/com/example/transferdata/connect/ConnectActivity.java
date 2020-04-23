package com.example.transferdata.connect;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import com.example.transferdata.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ConnectActivity extends Activity implements PeerListListener {
    public static Channel channel;
    public static WifiP2pManager manager;
    private Boolean NewPhone = Boolean.FALSE;
    private ArrayAdapter<String> arrayAdapter;
    Button btn_cancel;
    private ProgressDialog dialog = null;
    private IntentFilter intentFilter;
    private ListView list_device;
    private RecyclerView.LayoutManager layoutManager;
    private final List<String> list_device_near = new ArrayList();
    /* access modifiers changed from: private */
    private final List<WifiP2pDevice> peersList = new ArrayList();
    private BroadcastReceiver receiver;
    private TextView txtNotFound;
    public String DeviceConnected;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isGPSEnabled(this);
        Boolean valueOf = getIntent().getBooleanExtra("NewPhone", true);
        this.NewPhone = valueOf;
        if (valueOf) {
            setContentView(R.layout.activity_client);
            find_id();
            loadDataListView();
        } else {
            setContentView(R.layout.activity_host);
        }
        clickButtonCancel();
        WifiP2pManager wifiP2pManager = (WifiP2pManager) getSystemService(WIFI_P2P_SERVICE);
        manager = wifiP2pManager;
        channel = Objects.requireNonNull(wifiP2pManager).initialize(this, getMainLooper(), null);
        receiver = new WiFiDirectBroadcastReceiver(manager, channel, this, NewPhone);
        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter = intentFilter2;
        intentFilter2.addAction("android.net.wifi.p2p.STATE_CHANGED");
        intentFilter.addAction("android.net.wifi.p2p.PEERS_CHANGED");
        intentFilter.addAction("android.net.wifi.p2p.CONNECTION_STATE_CHANGE");
        intentFilter.addAction("android.net.wifi.p2p.THIS_DEVICE_CHANGED");
        manager.discoverPeers(channel, new ActionListener() {
            public void onSuccess() {
                System.out.println("Successs");
            }

            public void onFailure(int reason) {
                System.out.println("false");
            }
        });
    }

    /* access modifiers changed from: 0000 */
    private void find_id() {
        list_device = findViewById(R.id.rcv_list_device);
        txtNotFound = findViewById(R.id.txt_not_found);
    }

    /* access modifiers changed from: 0000 */
    private void loadDataListView() {
        if (list_device_near.size() == 0) {
            txtNotFound.setVisibility(View.VISIBLE);
        } else {
            txtNotFound.setVisibility(View.GONE);
        }

        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list_device_near);
        arrayAdapter = arrayAdapter2;
        list_device.setAdapter(arrayAdapter2);
        list_device.setOnItemClickListener((AdapterView.OnItemClickListener) (adapterView, view, position, id) -> {
            String Address = ((WifiP2pDevice) ConnectActivity.this.peersList.get(position)).deviceAddress;
            if (Address != null) {
                ConnectActivity.this.connect_device(Address);
            }
            DeviceConnected = peersList.get(position).deviceName;
            String message = "Connecting " +
                    (String) ConnectActivity.this.list_device_near.get(position) +
                    ". Please wait...";
            ConnectActivity ConnectActivity = ConnectActivity.this;
            ConnectActivity.dialog = ProgressDialog.show(ConnectActivity, "Connecting... ", message, true);
            ConnectActivity.this.dialog.setCanceledOnTouchOutside(false);
            ConnectActivity.this.dialog.setCancelable(true);
            ConnectActivity.this.dialog.setOnCancelListener(dialogInterface -> manager.cancelConnect(channel, new ActionListener() {
                public void onSuccess() {
                    ConnectActivity.this.dialog.dismiss();
                }

                public void onFailure(int reason) {
                }
            }));
        });
    }

    /* access modifiers changed from: 0000 */
    private void connect_device(String Address) {
        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = Address;
        config.groupOwnerIntent = 15;
        manager.connect(channel, config, new ActionListener() {
            public void onSuccess() {
                System.out.println("Connect Successs");
            }

            public void onFailure(int reason) {
                System.out.println("Connect false");
            }
        });
    }

    /* access modifiers changed from: 0000 */
    public void statusWifi(boolean status) {
        if (!status) {
            Builder builder = new Builder(this);
            builder.setTitle("Wifi not enable");
            builder.setMessage("Need turn on wifi to scan devices around");
            builder.setPositiveButton("Open Setting", (dialog, which) -> ConnectActivity.this.startActivity(new Intent("android.settings.WIFI_SETTINGS")));
            builder.setNegativeButton("Cancel", (dialog, which) -> ConnectActivity.this.finish());
            final AlertDialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
            alertDialog.setOnCancelListener(dialog -> {
                alertDialog.dismiss();
                ConnectActivity.this.finish();
            });
        }
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        registerReceiver(this.receiver, this.intentFilter);
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        unregisterReceiver(this.receiver);
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        ProgressDialog progressDialog = this.dialog;
        if (progressDialog != null && progressDialog.isShowing()) {
            this.dialog.dismiss();
        }
        super.onDestroy();
    }

    public void onPeersAvailable(WifiP2pDeviceList peers) {
        if (!peersList.equals(peers) && this.NewPhone) {
            peersList.clear();
            list_device_near.clear();
            peersList.addAll(peers.getDeviceList());
            for (WifiP2pDevice index : peersList) {
                this.list_device_near.add(index.deviceName);
            }
            this.arrayAdapter.notifyDataSetChanged();
            if (list_device_near.size() == 0) {
                txtNotFound.setVisibility(View.VISIBLE);
            } else {
                txtNotFound.setVisibility(View.GONE);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    private void clickButtonCancel() {
        if (NewPhone) {
//            this.btn_cancel = (Button) findViewById(R.id.btn_cancel_newphone);
//        } else {
//            this.btn_cancel = (Button) findViewById(R.id.btn_cancel_oldphone);
        }
//        this.btn_cancel.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                connect.this.finish();
//            }
//        });
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    private void isGPSEnabled(Context mContext) {
    boolean gpsStatus = ((LocationManager) Objects.requireNonNull(mContext.getSystemService(LOCATION_SERVICE))).isProviderEnabled("gps");
        if (!gpsStatus) {
            Builder builder = new Builder(this);
            builder.setTitle("GPS not enable");
            builder.setMessage("Need turn on GPS to scan devices around");
            builder.setPositiveButton("Open Setting", (dialog, which) -> ConnectActivity.this.startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS")));
            builder.setNegativeButton("Cancel", (dialog, which) -> ConnectActivity.this.finish());
            final AlertDialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
            alertDialog.setOnCancelListener(dialog -> {
                alertDialog.dismiss();
                ConnectActivity.this.finish();
            });
        }
    }

    private static void disconnect(final Activity context) {
        Builder builder = new Builder(context);
        builder.setTitle("Are you disconnect device ?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            ConnectActivity.manager.removeGroup(ConnectActivity.channel, new ActionListener() {
                public void onSuccess() {
                    Toast.makeText(context, "Disconnect", Toast.LENGTH_LONG).show();
                }

                public void onFailure(int reason) {
                }
            });
            context.finish();
        });
        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    public static void isConnect(final Activity context) {
        manager.requestConnectionInfo(channel, info -> {
            if (info.groupFormed) {
                ConnectActivity.disconnect(context);
            } else {
                context.finish();
            }
        });
    }

}
