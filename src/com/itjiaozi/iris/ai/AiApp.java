package com.itjiaozi.iris.ai;

import java.util.List;

import android.content.Intent;

import com.itjiaozi.iris.TheApplication;
import com.itjiaozi.iris.about.TheApps;
import com.itjiaozi.iris.ai.AiManager.ERecognitionModel;
import com.itjiaozi.iris.cmd.EUserCommand;
import com.itjiaozi.iris.db.TbAppCache;
import com.itjiaozi.iris.talk.BaseTalk;
import com.itjiaozi.iris.talk.ITalkCallback;

public class AiApp extends BaseTalk implements IAiCallback {
    List<TbAppCache> mWaitOpenApps;
    int mHasSelectIndex;

    public AiApp() {
        AiManager.getInstance().putCmd(this, "打开({name}.*)", "name");
    }

    @Override
    public void say(String str, ITalkCallback iTalkCallback) {
        addToHistory(str);
    }

    @Override
    public void callback(Intent intent) {
        // 如果在前台
        String appName = "";
        if (this == AiManager.getInstance().lock()) {
            appName = intent.getAction();
            mWaitOpenApps = TheApps.query(appName);
        } else {
            appName = intent.getStringExtra("name");
            mWaitOpenApps = TheApps.query(appName);
        }

        int waitSize = null == mWaitOpenApps ? 0 : mWaitOpenApps.size();
        if (1 == waitSize) {
            AiManager.getInstance().startRecognition("是不是要打开" + mWaitOpenApps.get(0).Name + "？", ERecognitionModel.Cmd);
        } else if (1 < waitSize) {
            StringBuilder sb = new StringBuilder("未能准确识别，请选择:\r\n");
            for (int i = 0; i < mWaitOpenApps.size(); i++) {
                sb.append("  " + (i + 1) + " " + mWaitOpenApps.get(i).Name + "\r\n");
            }
            AiManager.getInstance().startRecognition(sb.toString(), ERecognitionModel.SelectIndex);
        } else if (1 > waitSize) {
            AiManager.getInstance().startRecognition("没有找到应用" + appName + ", 是不是没有安装, 请重说应用名", ERecognitionModel.Normal);
        }
    }

    @Override
    public void doUserCommand(EUserCommand cmd) {
        if (cmd == EUserCommand.Ok) {
            startApp(mWaitOpenApps.get(0).PackageName);
        } else if (cmd == EUserCommand.Cancel) {
            AiManager.getInstance().openMainTask();
        }
    }

    @Override
    public void doSelectItem(int selectIndex) {
        if (selectIndex < 1 || selectIndex > mWaitOpenApps.size()) {
            StringBuilder sb = new StringBuilder("范围无效，请选择:\r\n");
            for (int i = 0; i < mWaitOpenApps.size(); i++) {
                sb.append("  " + (i + 1) + " " + mWaitOpenApps.get(i).Name + "\r\n");
            }
            AiManager.getInstance().startRecognition(sb.toString(), ERecognitionModel.SelectIndex);
        } else {
            mHasSelectIndex = selectIndex;
            startApp(mWaitOpenApps.get(mHasSelectIndex).PackageName);
        }
    }

    private void startApp(String packageName) {
        Intent intent = TheApplication.getInstance().getPackageManager().getLaunchIntentForPackage(packageName);
        TheApplication.getInstance().startActivity(intent);
        AiManager.getInstance().openMainTask();
        AiManager.getInstance().startRecognition("成功打开应用" + mWaitOpenApps.get(mHasSelectIndex).Name, ERecognitionModel.Normal);
    }
}
