package com.itjiaozi.iris.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class TheService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
