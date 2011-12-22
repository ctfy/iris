package com.itjiaozi.iris.ai;

import java.util.List;

import android.content.Intent;

import com.itjiaozi.iris.about.TheApps;
import com.itjiaozi.iris.cmd.EUserCommand;
import com.itjiaozi.iris.db.TbAppCache;
import com.itjiaozi.iris.talk.BaseTalk;
import com.itjiaozi.iris.talk.ITalkCallback;

public class AiApp extends BaseTalk implements IAiCallback {
    List<TbAppCache> waitOpenApps;

    public AiApp() {
        AiManager.getInstance().putCmd(this, "打开({name}.*)", "name");
    }

    @Override
    public void say(String str, ITalkCallback iTalkCallback) {
        addToHistory(str);

    }

    @Override
    public void callback(Intent intent) {
        String appName = intent.getStringExtra("name");
        waitOpenApps = TheApps.query(appName);
    }

    @Override
    public void doUserCommand(EUserCommand cmd) {

    }

    @Override
    public void doSelectItem(int selectIndex) {

    }
}
