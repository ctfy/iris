package com.itjiaozi.iris.service;

import com.itjiaozi.iris.about.TheContacts;

import android.app.Service;
import android.content.Intent;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.util.Log;

public class TheService extends Service {

    private static final String TAG = TheService.class.getSimpleName();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        getContentResolver().registerContentObserver(ContactsContract.Contacts.CONTENT_URI, true, mContactsObserver);
        getContentResolver().registerContentObserver(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, true, mContactsObserver);

    }

    private static ContentObserver mContactsObserver = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange) {
            Log.d(TAG, "contacts changed");
            TheContacts.syncContacts(true);
        }
    };

    public void onDestroy() {
        Log.d(TAG, "IrisService onDestroy()");
        getContentResolver().unregisterContentObserver(mContactsObserver);
    };
}
