package com.example.transferdata;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ClipData;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.transferdata.contact.TestContacts;
import com.example.transferdata.selectdata.SelectDataActivity;

import java.io.File;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.ArrayList;

public class ClientActivity extends AppCompatActivity {
    private TextView txtAvailableDevices, txtSendData, txtClient;
    private RecyclerView rcvDevicesList, rcvSendFiles;
    private WifiP2pManager p2pManager;
    private WifiP2pManager.Channel channel;
    private WifiDirectBroadcastReceiver mWifiDirectBroadcastReceiver ;
    private ArrayList peerList = new ArrayList();
    private PeersAdapter peersAdapter;
    private InetAddress serverAddress;
    private ServerSocket serverSocket;
    private ServerSocket serverSocketDevice;
    private FileServerAsyncTask fileServerAsyncTask;
    private DeviceInfoServerAsyncTask deviceInfoServerAsyncTask;
    private Callback callbackSendThisDeviceName;
    private SharedPreferences sharedPreferences;
    private Button btnDiscoveryStart, btnDiscoveryEnd;
    private Callback callbackReInitDeviceServer;
    private Callback callbackReInitFileServer;
    private Boolean mCheckConnectionDevice = false;
    private FilesSendAdapter sendFilesAdapter;
    private FilesReceiveAdapter receiveFilesAdapter;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
        }
        initView();
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        initAction();
        p2pManager.discoverPeers(channel, null);
        //init sockets for transport servers and callbacks for reinit servers
        this.initSockets();
        callbackReInitFileServer = ClientActivity.this::initFileServer;
        callbackReInitDeviceServer = ClientActivity.this::initDeviceInfoServers;
        this.initFileServer(); // Init file server for receiving data
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void initAction() {
        txtAvailableDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p2pManager.discoverPeers(channel, null);
            }
        });
        txtSendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCheckConnectionDevice){
                    ChooseFile.fileChooser(ClientActivity.this);
                }
                else Toast.makeText(ClientActivity.this, "Not Connection", Toast.LENGTH_SHORT).show();
            }
        });

        WifiP2pManager.PeerListListener peerListListener = peers -> {
            peerList.clear();
            peerList.addAll(peers.getDeviceList());
            peersAdapter.updateList(peerList);
            peersAdapter.notifyDataSetChanged();
        };

        //Set callback for send device name to device, who was connected. (not available in Android’s official API)
        callbackSendThisDeviceName = () -> {
            TransferNameDevice transferNameDevice = new TransferNameDevice(serverAddress);
            transferNameDevice.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        };

        //Set callback for connected device, there I calling send device name who connected to me,
        // and another call it too, and call to start file manager
        WifiP2pManager.ConnectionInfoListener infoListener = info -> {
            Toast.makeText(this,"Connect success",Toast.LENGTH_LONG).show();
            txtClient.setVisibility(View.VISIBLE);
            mCheckConnectionDevice = true;
            serverAddress = info.groupOwnerAddress;
            if (serverAddress == null) return;
            callbackSendThisDeviceName.call();
            Intent intent = new Intent(this, SelectDataActivity.class);
            startActivity(intent);
            //ChooseFile.fileChooser(ClientActivity.this);
        };

        //Set p2pManger and channel
        p2pManager = (WifiP2pManager) getSystemService(WIFI_P2P_SERVICE);
        channel = p2pManager.initialize(this, getMainLooper(), null);

//        try {
////      DeviceUtil.callHiddenMethod(manager, channel, newName);
//            DeviceUtil.callHiddenMethod(p2pManager, channel,
//                    sharedPreferences.getString(Variables.NAME_DEVICE, null));
//            this.initFileServer();
//
//        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
//            e.printStackTrace();
//        }

        //On the specified device p2p are disabled, to enable it I use
        try {
            Class<?> wifiManager = Class
                    .forName("android.net.wifi.p2p.WifiP2pManager");

            Method method = wifiManager
                    .getMethod("enableP2p",
                            WifiP2pManager.Channel.class);
            method.invoke(p2pManager, channel);
        } catch (Exception ignored) {
        }

        //Just in case, I delete the group, since after an instant restart of the application,
        // Dalvik doesn't clean the application immediately, but with it both the manager and the channel
        p2pManager.removeGroup(channel, null);

        //My wrapper for defining peers
        mWifiDirectBroadcastReceiver = new WifiDirectBroadcastReceiver(p2pManager, channel,
                this, infoListener);
        mWifiDirectBroadcastReceiver.setPeerListListener(peerListListener);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);

        //Register receiver to ContextWrapper with filters
        registerReceiver(mWifiDirectBroadcastReceiver, intentFilter);

        peersAdapter = new PeersAdapter(peerList, this,
                p2pManager, channel, this, infoListener);
        rcvDevicesList.setAdapter(peersAdapter);

        // Init Device info server for receiving device name who connected
        this.initDeviceInfoServers();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false);
        rcvDevicesList.setLayoutManager(mLayoutManager);
    }

    private void initFileServer() {
        fileServerAsyncTask = new FileServerAsyncTask(
                (ClientActivity.this),
                (serverSocket),
                (ClientActivity.this.receiveFilesAdapter), callbackReInitFileServer);
        fileServerAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void initDeviceInfoServers() {
        deviceInfoServerAsyncTask = new DeviceInfoServerAsyncTask(
                (serverSocketDevice),
                (ClientActivity.this.peersAdapter), callbackReInitDeviceServer);
        deviceInfoServerAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void initView() {
        txtClient = findViewById(R.id.txt_client);
        txtAvailableDevices = findViewById(R.id.txt_available_devices);
        txtSendData = findViewById(R.id.txt_send_data);
        rcvDevicesList = findViewById(R.id.rcv_devices_list);
        rcvSendFiles = findViewById(R.id.rcv_send_files);
        LinearLayoutManager filesListLayoutManager = new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false);
        rcvSendFiles.setLayoutManager(filesListLayoutManager);
        sendFilesAdapter = new FilesSendAdapter();
        rcvSendFiles.setAdapter(sendFilesAdapter);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        p2pManager.cancelConnect(channel, null);
        unregisterReceiver(mWifiDirectBroadcastReceiver);
        p2pManager.stopPeerDiscovery(channel, null);

        try {
            serverSocket.close();
            serverSocketDevice.close();
            fileServerAsyncTask.cancel(true);
            deviceInfoServerAsyncTask.cancel(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        deletePersistentGroups();
    }

    private void deletePersistentGroups() {
        try {
            Method[] methods = WifiP2pManager.class.getMethods();
            for (Method method : methods) {
                if (method.getName().equals("deletePersistentGroup")) {
                    // Delete any persistent group
                    for (int netid = 0; netid < 32; netid++) {
                        method.invoke(p2pManager, channel, netid, null);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //transfer and get data
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ChooseFile.FILE_TRANSFER_CODE:
                if (data == null) return;

                ArrayList<Uri> uris = new ArrayList<>();
                ArrayList<Long> filesLength = new ArrayList<>();
                ArrayList<String> fileNames = new ArrayList<>();

                try {
                    ClipData clipData = data.getClipData();

                    if (clipData != null) {
                        for (int i = 0; i < clipData.getItemCount(); i++) {
                            uris.add(clipData.getItemAt(i).getUri());

                            String fileName =
                                    PathUtil.getPath(getApplicationContext(), clipData.getItemAt(i).getUri());
                            filesLength.add(new File(fileName).length());

                            fileName = FilesUtil.getFileName(fileName);
                            fileNames.add(fileName);

                            Log.d("File URI", clipData.getItemAt(i).getUri().toString());
                            Log.d("File Path", fileName);
                        }
                    }
                    else {
                        Uri uri = data.getData();
                        uris.add(uri);

                        String fileName = PathUtil.getPath(getApplicationContext(), uri);
                        filesLength.add(new File(fileName).length());

                        fileName = FilesUtil.getFileName(fileName);
                        fileNames.add(fileName);
                    }
                    sendFilesAdapter.notifyAdapter(uris, filesLength, fileNames);

                    TransferData transferData = new TransferData(ClientActivity.this,
                            uris, filesLength, fileNames, (sendFilesAdapter), serverAddress, p2pManager, channel);
                    transferData.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
    }

    private void initSockets() {
        try {
            serverSocketDevice = new ServerSocket(8887);
            serverSocket = new ServerSocket(8888);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
