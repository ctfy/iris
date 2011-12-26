package com.itjiaozi.iris.ai;

import android.content.Intent;

import com.itjiaozi.iris.cmd.EUserCommand;
import com.itjiaozi.iris.talk.BaseTalk;
import com.itjiaozi.iris.talk.ITalkCallback;

public class AiDefault extends BaseAi {
    @Override
    public void say(String str, ITalkCallback iTalkCallback) {
        iTalkCallback.callback(str);
    }

    @Override
    public String[] getXunFeiKeys() {
        return super.getXunFeiKeys();
    }

    @Override
    public void callback(Intent intent) {
        // TODO Auto-generated method stub
        super.callback(intent);
    }

    @Override
    public void doSelectItem(int selectedIndex) {
        // TODO Auto-generated method stub
        super.doSelectItem(selectedIndex);
    }

    @Override
    public void doUserCommand(EUserCommand cmd) {
        // TODO Auto-generated method stub
        super.doUserCommand(cmd);
    }


}
