package com.example.transferdata.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class HeadlessSmsSendService extends Service {
    public IBinder onBind(Intent intent) {
        return null;
    }
}
