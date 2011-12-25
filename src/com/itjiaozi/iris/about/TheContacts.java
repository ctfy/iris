package com.itjiaozi.iris.about;

import java.util.List;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.provider.ContactsContract;

import com.itjiaozi.iris.Constant;
import com.itjiaozi.iris.TheApplication;
import com.itjiaozi.iris.db.TbAppCache;
import com.itjiaozi.iris.db.TbContactCache;
import com.itjiaozi.iris.util.AppLog;
import com.itjiaozi.iris.util.SPUtil;

public class TheContacts extends BaseTheAbout {

    protected static final String TAG = TheContacts.class.getSimpleName();

    public TheContacts() {
        syncContacts(false);
    }

    @Override
    public void filter(String str, String pinyin) {
        super.filter(str, pinyin);
    }

    /**
     * 同步联系人缓存数据
     * 
     * @param force
     *            联系人发生变化时，此参数需要指定为true
     */
    public static void syncContacts(boolean force) {
        boolean needSync = false;
        if (force) {
            needSync = true;
        }

        if (!SPUtil.contains(Constant.SP_KEY_HAS_SYNC_CONTACT_CACHE)) {
            needSync = true;
        }

        if (!needSync) {
            AppLog.d(TAG, "不需要同步联系人缓存，返回");
            return;
        }

        TbContactCache.deleteAll();
        Cursor c = null;
        try {
            AppLog.d(TAG, "开始同步联系人数据");
            c = TheApplication.getInstance().getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                String fullName = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String pinyin = com.itjiaozi.iris.util.Pinyin.getPingYin(fullName);
                String contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                Cursor phoneCursor = null;
                try {
                    phoneCursor = TheApplication.getInstance().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                    while (phoneCursor.moveToNext()) {
                        TbContactCache cc = new TbContactCache();
                        String strPhoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)); // 手机号码字段联系人可能不止一个
                        cc.FullName = fullName;
                        cc.Pinyin = pinyin;
                        cc.Number = strPhoneNumber;
                        cc.BeUsedCount = 0;
                        long count = TbContactCache.insertOrUpdateContact(cc);
                        AppLog.d(TAG, (count > -1 ? "成功>>" : "失败>>") + "同步联系人缓存: " + cc);
                    }
                    SPUtil.put(Constant.SP_KEY_HAS_SYNC_CONTACT_CACHE, true);
                    AppLog.d(TAG, "同步联系人缓存数据成功");
                } finally {
                    if (null != phoneCursor)
                        phoneCursor.close();
                }
            }
        } catch (Exception e) {
            AppLog.e(TAG, "同步联系人缓存数据失败");
            e.printStackTrace();
        } finally {
            if (null != c)
                c.close();
        }
    }

    public static List<String> getAllContactsName() {
        return TbContactCache.queryContactFullNames();
    }
}
