package com.example.transferdata;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import com.example.transferdata.connect.ConnectActivity;
import com.jaeger.library.StatusBarUtil;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class MainActivity extends AppCompatActivity {
    String[] PERMISSIONS = {"android.permission.READ_CONTACTS", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.ACCESS_FINE_LOCATION", "android.permission.READ_CONTACTS", "android.permission.WRITE_CONTACTS", "android.permission.READ_SMS", "android.permission.READ_CALL_LOG", "android.permission.WRITE_CALL_LOG", "android.permission.INSTALL_PACKAGES", "android.permission.ACCESS_COARSE_LOCATION"};
    int PERMISSION_ALL = 1;
    private ConstraintLayout mImgSendData, mImgReceiveData;
    WifiP2pManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkAndRequestPermission();
        initView();
        initAction();
        turnOnWifi();
        StatusBarUtil.setTransparent(this);
        StatusBarUtil.setLightMode(this);
    }

    private void turnOnWifi() {
        if (! (((WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE)).isWifiEnabled())) {
            if (Build.VERSION.SDK_INT >= 29) {
                statusWifi(false);
            }
            ((WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE)).setWifiEnabled(true);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static PublicKey getPublicKey(String s) throws Exception {
        return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(s)));
    }

    private void initView() {
        mImgSendData = findViewById(R.id.img_send_data);
        mImgReceiveData = findViewById(R.id.img_receive_data);
        StatusBarUtil.setTransparent(this);
        StatusBarUtil.setLightMode(this);
    }

    private void initAction() {
        mImgSendData.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ConnectActivity.class);
            intent.putExtra("NewPhone", false);
            startActivity(intent);
        });
        mImgReceiveData.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ConnectActivity.class);
            intent.putExtra("NewPhone", true);
            MainActivity.this.startActivity(intent);
        });
    }

    public void checkAndRequestPermission() {
        if (!hasPermissions(this, this.PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, this.PERMISSIONS, this.PERMISSION_ALL);
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (!(context == null || permissions == null)) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != 0) {
                    return false;
                }
            }
        }
        return true;
    }
    public void statusWifi(boolean status) {
        if (!status) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getResources().getString(R.string.Wifinotenable));
            builder.setMessage(getResources().getString(R.string.needturnonwifi));
            builder.setPositiveButton(getResources().getString(R.string.OpenSetting), (dialog, which) -> MainActivity.this.startActivity(new Intent("android.settings.WIFI_SETTINGS")));
            builder.setNegativeButton(getResources().getString(R.string.ClientActivity_button_cancel), (dialog, which) -> MainActivity.this.finish());
            final AlertDialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
            alertDialog.setOnCancelListener(dialog -> {
                alertDialog.dismiss();
                MainActivity.this.finish();
            });
        }
    }
}
