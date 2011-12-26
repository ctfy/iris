package com.itjiaozi.iris.ai;

import java.util.HashMap;

public class TheAiManager {
    private static TheAiManager instance;

    HashMap<ETheAiType, BaseTheAi> maps = new HashMap<ETheAiType, BaseTheAi>();

    private TheAiManager() {
        maps.put(ETheAiType.All, new TheAiAll());
        maps.put(ETheAiType.Call, new TheAiCall());
        maps.put(ETheAiType.Message, new TheAiMessage());
        maps.put(ETheAiType.App, new TheAiApp());
        
        for (BaseTheAi t : maps.values()) {
            t.onLoad();
        }
    }

    public static TheAiManager getInstance() {
        if (null == instance) {
            instance = new TheAiManager();
        }
        return instance;
    }

    public BaseTheAi getTheAi(ETheAiType eTheAiType) {
        BaseTheAi tBaseTheAi = maps.get(eTheAiType);
        return tBaseTheAi;
    }
}
