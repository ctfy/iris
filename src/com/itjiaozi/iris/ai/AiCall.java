package com.itjiaozi.iris.ai;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.net.Uri;

import com.itjiaozi.iris.TheApplication;
import com.itjiaozi.iris.about.TheApps;
import com.itjiaozi.iris.about.TheContacts;
import com.itjiaozi.iris.ai.AiManager.ERecognitionModel;
import com.itjiaozi.iris.cmd.EUserCommand;
import com.itjiaozi.iris.db.TbContactCache;
import com.itjiaozi.iris.talk.ITalkCallback;
import com.itjiaozi.iris.util.AppLog;
import com.itjiaozi.iris.util.SPUtil;

public class AiCall extends BaseAi {
    private static final String TAG = AiCall.class.getSimpleName();
    List<TbContactCache> mWaitContactss;

    public AiCall() {
        AiManager.getInstance().putCmd(this, "打电话给({name}.*)", "name");
        AiManager.getInstance().putCmd(this, "给({name}.*?)打电话", "name");
        AiManager.getInstance().putCmd(this, "拨打({name}.*?)的电话", "name");
        AiManager.getInstance().putCmd(this, "拨({name}.*?)的电话", "name");
        AiManager.getInstance().putCmd(this, "打({name}.*?)的电话", "name");
    }

    @Override
    public void say(String str, ITalkCallback iTalkCallback) {
        iTalkCallback.callback(str);
    }

    @Override
    public void callback(Intent intent) {
        String name = intent.getStringExtra("name");
        mWaitContactss = TheContacts.query(name);

        if (mWaitContactss.size() > 1) {
            StringBuilder sb = new StringBuilder("未能准确识别，请选择:\r\n");
            for (int i = 0; i < mWaitContactss.size(); i++) {
                sb.append("  " + (i + 1) + " " + mWaitContactss.get(i).FullName + " " + mWaitContactss.get(i).Number + "\r\n");
            }
            AiManager.getInstance().startRecognition(sb.toString(), ERecognitionModel.SelectIndex);
        } else if (mWaitContactss.size() == 1) {
            String tmp = String.format("确定拨打%s (号码: %s)的电话", mWaitContactss.get(0).FullName, mWaitContactss.get(0).Number);
            AiManager.getInstance().startRecognition(tmp, ERecognitionModel.Cmd);
        } else if (mWaitContactss.size() < 1) {
            AiManager.getInstance().startRecognition("没有识别到通话目标", ERecognitionModel.Normal);
        }
    }

    @Override
    public void doSelectItem(int selectedIndex) {
        int index = selectedIndex - 1;
        if (mWaitContactss.size() > index && index >= 0) {
            startCallActivity(mWaitContactss.get(index).Number);
        } else {
            StringBuilder sb = new StringBuilder("未能准确识别，请选择:\r\n");
            for (int i = 0; i < mWaitContactss.size(); i++) {
                sb.append("  " + (i + 1) + " " + mWaitContactss.get(i).FullName + " " + mWaitContactss.get(i).Number + "\r\n");
            }
            AiManager.getInstance().startRecognition(sb.toString(), ERecognitionModel.SelectIndex);
        }
    }

    @Override
    public void doUserCommand(EUserCommand cmd) {
        if (cmd == EUserCommand.Ok) {
            if (null != mWaitContactss && mWaitContactss.size() > 0) {
                startCallActivity(mWaitContactss.get(0).Number);
            } else {
                AiManager.getInstance().startRecognition("没有识别到通话目标", ERecognitionModel.Normal);
            }
        } else if (cmd == EUserCommand.Cancel) {
            AiManager.getInstance().openMainTask();
        }
    }

    @Override
    public String[] getXunFeiKeys() {
        List<String> result = new ArrayList<String>();
        List<String> list = TheContacts.getAllContactsName();
        for (String t : list) {
            result.add("打电话给" + t);
            result.add("给" + t + "打电话");
            result.add("拨打" + t + "的电话");
            result.add("拨" + t + "的电话");
            result.add("打" + t + "的电话");
        }
        result.add("确定");
        result.add("取消");
        AppLog.d(TAG, "有" + result + "个与打电话相关的关键词需要上传");
        return result.toArray(new String[1]);
    }

    @Override
    public boolean needUpload() {
        int version = TheContacts.getVersion();
        int hasUploadVersion = SPUtil.getInt(TheContacts.class.getName() + "_has_upload_version", Integer.MIN_VALUE);
        if (hasUploadVersion < version) {
            AppLog.d(TAG, "打电话ai <<<需要>>> 上传关键词数据");
            return true;
        } else {
            AppLog.d(TAG, "打电话ai<<<不需要>>>上传关键词数据");
            return false;
        }
    }

    @Override
    public void uploadSuccess() {
        super.uploadSuccess();
        int version = TheApps.getVersion();
        SPUtil.put(TheContacts.class.getName() + "_has_upload_version", version);
    }

    private void startCallActivity(String phoneNumber) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction("android.intent.action.CALL");
        intent.setData(Uri.parse("tel:" + phoneNumber));
        TheApplication.getInstance().startActivity(intent);
    }

}
