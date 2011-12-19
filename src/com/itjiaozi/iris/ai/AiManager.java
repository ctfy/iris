package com.itjiaozi.iris.ai;

import com.itjiaozi.iris.talk.BaseTalk;

public class AiManager {
    public BaseTalk getAi() {
        return new AiDefault();
    }
}
