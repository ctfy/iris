package com.itjiaozi.iris.ai;

import java.util.ArrayList;
import java.util.List;

import com.itjiaozi.iris.talk.BaseTalk;
import com.itjiaozi.iris.talk.ITalk;
import com.itjiaozi.iris.talk.ITalkCallback;

public class AiManager implements ITalk {
    private List<ITalk> talks = new ArrayList<ITalk>();

    public AiManager() {
        init();
    }

    public ITalk getAi() {
        return this;
    }

    private void init() {
        talks.add(new AiApp());
        talks.add(new AiCall());
        talks.add(new AiChat());
        talks.add(new AiDefault());
        talks.add(new AiEmail());
        talks.add(new AiMessage());
        talks.add(new AiMusic());
        talks.add(new AiSystemSetting());
    }

    @Override
    public void say(String str, ITalkCallback iTalkCallback) {
        for (ITalk t : talks) {
            t.say(str, iTalkCallback);
        }
    }
}
