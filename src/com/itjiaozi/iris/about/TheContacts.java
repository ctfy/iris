package com.itjiaozi.iris.about;

import java.util.List;

import android.database.Cursor;
import android.provider.ContactsContract;

import com.itjiaozi.iris.TheApplication;
import com.itjiaozi.iris.db.TbContactCache;
import com.itjiaozi.iris.util.AppLog;
import com.itjiaozi.iris.util.SPUtil;
import com.itjiaozi.iris.util.TheObservable;

public class TheContacts {

    public static TheObservable onChanged = new TheObservable();
    protected static final String TAG = TheContacts.class.getSimpleName();

    public TheContacts() {
        syncContacts(false);
    }

    /**
     * 同步联系人缓存数据
     * 
     * @param force
     *            联系人发生变化时，此参数需要指定为true
     */
    public static void syncContacts(boolean force) {
        boolean needSync = false;
        if (force || !SPUtil.contains(TheContacts.class.getName() + "_has_init_contact")) {
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
                        cc.PinYin = pinyin;
                        cc.Number = strPhoneNumber;
                        cc.BeUsedCount = 0;
                        long count = TbContactCache.insertOrUpdateContact(cc);
                        AppLog.d(TAG, (count > -1 ? "成功>>" : "失败>>") + "同步联系人缓存: " + cc);
                    }
                    AppLog.d(TAG, "同步联系人缓存数据成功");

                    autoAddVersion();
                    onChanged.notifyObservers();
                    SPUtil.put(TheContacts.class.getName() + "_has_init_contact", true);
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

    public static List<TbContactCache> query(String str) {
        return TbContactCache.queryContacts(str);
    }

    public static List<String> getAllContactsName() {
        return TbContactCache.queryContactFullNames();
    }

    public static void autoAddVersion() {
        int version = getVersion();
        version += 1;
        AppLog.d(TAG, "联系人缓存数据版本已经升级至：" + version);
        SPUtil.put(TheContacts.class.getName() + "_version", version);
    }

    public static int getVersion() {
        int version = SPUtil.getInt(TheContacts.class.getName() + "_version", 0);
        AppLog.d(TAG, "当前联系人缓存数据版本：" + version);
        return version;
    }
}
