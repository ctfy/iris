package com.itjiaozi.iris.ai;

import com.itjiaozi.iris.talk.BaseTalk;
import com.itjiaozi.iris.talk.ITalkCallback;

public class AiDefault extends BaseTalk{
    @Override
    public void say(String str, ITalkCallback iTalkCallback) {
        iTalkCallback.callback(str);
    }
}
